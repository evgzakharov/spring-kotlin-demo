package demo.payment

interface Response {}

data class ErrorResponse(
    val error: String
): Response

data class SuccessResponse(
    val status: Boolean = true
): Response

data class TransactionResponse(
    val transactions: List<TransactionInfo>
): Response
