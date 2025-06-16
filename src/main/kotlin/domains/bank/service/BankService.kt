package org.example.domains.bank.service

import org.example.common.logging.Logging
import org.example.common.transaction.Transactional
import org.springframework.stereotype.Service
import org.slf4j.*

@Service
class BankService (
    private val transaction: Transactional,
    private val logger: Logger = Logging.getLogger(BankService::class.java)
){
    fun createAccount(userUlid: String) =Logging.logFor(logger){log->
        log["userUlid"] = userUlid;

        transaction.run {}
    }

    fun balance(userUlid: String, accountUlid: String)=Logging.logFor(logger) {log->
        log["ulid"] = userUlid;
        log["accountUlid"] = accountUlid;

        transaction.run {}
    }

    fun removeAccount(userUlid: String, accountUlid: String) =Logging.logFor(logger) {log ->
        log["ulid"] = userUlid;
        log["accountUlid"] = accountUlid;

        transaction.run {}
    }
}