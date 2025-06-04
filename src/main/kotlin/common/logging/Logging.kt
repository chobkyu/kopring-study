package org.example.common.logging

import org.example.common.exceptions.CustomException
import org.example.common.exceptions.Errcode
import org.slf4j.*

object Logging {
    fun<T: Any> getLogger(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

    fun<T> logFor(log: Logger, function: (MutableMap<String, Any>) -> T?): T {
        val logInfo = mutableMapOf<String, Any>()

        logInfo["start_at"] = now()

        val result = function.invoke(logInfo)

        logInfo["end_at"] = now()
        log.info(logInfo.toString())

        return result ?: throw CustomException(Errcode.FAILED_TO_INVOKE_IN_LOGGER)
//        result?.let {
//
//        } ?:
    }

    private fun now(): Long {
        return System.currentTimeMillis()
    }
}