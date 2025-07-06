package org.example.common.cache

object RedisKeyProvider {
    private const val BANK_MUTAX_KEY = "bankMutex";
    private const val HISTORY_CACHE_KEY = "history";

    fun bankMutexKey(ulid:String, accountUlid: String): String {
        return "$BANK_MUTAX_KEY:$ulid:$accountUlid";
    }

    fun historyCacheKey(ulid: String, accountUlid: String): String {
        return "$HISTORY_CACHE_KEY:$ulid:$accountUlid";
    }
}