package org.example.common.exceptions

interface CodeInterface{
    val code: Int
    var message: String
}

enum class Errcode(
    override val code: Int,
    override var message: String
): CodeInterface {
    AUTH_CONFIG_NOT_FOUND(-100,"auth config not found"),
    FAILED_TO_CALL_CLINET(-101, "failed to call client"),
    CALL_RESULT_BODY_NULL(-102,"body is nil" )

}