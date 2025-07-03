package org.example.domains.bank.service

import com.github.f4b6a3.ulid.UlidCreator
import okhttp3.internal.http2.ErrorCode
import org.example.common.exceptions.CustomException
import org.example.common.exceptions.Errcode
import org.example.common.logging.Logging
import org.example.common.transaction.Transactional
import org.example.domains.auth.repository.AuthUserRepository
import org.example.domains.bank.repository.BankAccountRepository
import org.example.domains.bank.repository.BankUserRepository
import org.example.types.dto.Response
import org.example.types.dto.ResponseProvider
import org.example.types.entitiy.Account

import org.springframework.stereotype.Service
import org.slf4j.*
import java.lang.Math.random
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class BankService (
    private val transaction: Transactional,
    private val bankUserRepository: BankUserRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val logger: Logger = Logging.getLogger(BankService::class.java)
){
    fun createAccount(userUlid: String): Response<String> = Logging.logFor(logger){ log->
        log["userUlid"] = userUlid;

        transaction.run {
            val user = bankUserRepository.findByUlid(userUlid);

            val ulid = UlidCreator.getUlid().toString();
            val accountNumber = generateRandomAccount()

            val account = Account(
                ulid = ulid,
                user = user,
                account_number = accountNumber
            );

            try{
                bankAccountRepository.save(account);
            }catch (e: Exception) {
                throw CustomException(Errcode.FAILED_TO_SAVE_DATA , e.message);
            }
        }

        return@logFor ResponseProvider.success("SUCCESS");
    }

    fun balance(userUlid: String, accountUlid: String): Response<BigDecimal> =Logging.logFor(logger) {log->
        log["ulid"] = userUlid;
        log["accountUlid"] = accountUlid;

        // TODO -> 동시성 처리 고려
        transaction.run {
            val account = bankAccountRepository.findByUlid(accountUlid) ?: throw CustomException(Errcode.FAILED_TO_FIND_ACCOUNT, accountUlid)

            if(account.user.ulid != userUlid) throw CustomException(Errcode.MISSING_MATCH_ACCOUNT_ULID_AND_USER_ULID, userUlid);

            ResponseProvider.success(account.balance);
        }
    }

    fun removeAccount(userUlid: String, accountUlid: String): Response<String> =Logging.logFor(logger) { log ->
        log["ulid"] = userUlid;
        log["accountUlid"] = accountUlid;

        return@logFor transaction.run {
            val user = bankUserRepository.findByUlid(userUlid);
            val account = bankAccountRepository.findByUlid(accountUlid) ?: throw CustomException(Errcode.FAILED_TO_FIND_ACCOUNT, accountUlid);

            if(account.user.ulid != user.ulid) throw CustomException(Errcode.MISSING_MATCH_ACCOUNT_ULID_AND_USER_ULID, userUlid);
            if(account.balance.compareTo(BigDecimal.ZERO)!= 0) throw CustomException(Errcode.ACCOUNT_BALANCE_IS_NOT_ZERO, accountUlid);

            val updateAccount = account.copy(
                isDeleted = true,
                deletedAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            bankAccountRepository.save(updateAccount);

            ResponseProvider.success("SUCCESS");
        }


    }

    private fun generateRandomAccount(): String{
        val bankCode = "003";
        val section = "12";

        val number = random().toString();

        return "$bankCode-$section-$number";
    }
}