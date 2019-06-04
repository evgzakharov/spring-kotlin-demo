package demo.payment

import kotlinx.coroutines.sync.Mutex
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.math.MathContext
import java.util.concurrent.ConcurrentHashMap

data class TransactionInfo(
    val transactionInfo: String,
    val historyCardAmount: BigDecimal
)

data class CardInfo(
    val cardId: Long,
    var amount: BigDecimal,
    val cardMutex: Mutex
)

@RestController
class PaymentController(
) {
    val cardsInfo = ConcurrentHashMap<Long, CardInfo>(
        mapOf(
            1L to BigDecimal(1000L, MathContext.DECIMAL32),
            2L to BigDecimal(1000L, MathContext.DECIMAL32),
            3L to BigDecimal(1000L, MathContext.DECIMAL32)
        ).mapValues { (cardId, amount) -> amount.toCardInfo(cardId) }
    )

    val transactionHistory = ConcurrentHashMap<Long, MutableList<TransactionInfo>>()

    @GetMapping(value = ["{cardId}"])
    fun info(
        @PathVariable("cardId") cardId: Long,
        response: ServerHttpResponse
    ): Response {
        val transactions: List<TransactionInfo> = transactionHistory[cardId] ?: emptyList()
        return TransactionResponse(transactions)
    }

    @PostMapping
    suspend fun payment(
        @RequestBody paymentRequest: PaymentRequest,
        response: ServerHttpResponse
    ): Response {
        if (paymentRequest.cardIdFrom.equals(paymentRequest.cardIdTo)) {
            response.statusCode = HttpStatus.BAD_REQUEST
            return ErrorResponse("card id same")
        }

        val fromCardInfo = cardsInfo[paymentRequest.cardIdFrom] ?: run {
            response.statusCode = HttpStatus.BAD_REQUEST
            return ErrorResponse("card with id='${paymentRequest.cardIdFrom}' not found")
        }

        val toCardInfo = cardsInfo[paymentRequest.cardIdTo] ?: run {
            response.statusCode = HttpStatus.BAD_REQUEST
            return ErrorResponse("card with id='${paymentRequest.cardIdFrom}' not found")
        }

        listOf(fromCardInfo, toCardInfo)
            .sortedBy { it.cardId }
            .forEach { it.cardMutex.lock() }

        try {
            val newFromAmount = fromCardInfo.amount - paymentRequest.amount
            if (newFromAmount.signum() == -1) {
                response.statusCode = HttpStatus.BAD_REQUEST
                return ErrorResponse("card with id='${paymentRequest.cardIdFrom}' doesn't have money to send '${paymentRequest.amount}' to another card")

            }

            val newToAmount = toCardInfo.amount + paymentRequest.amount

            transactionHistory.getOrPut(fromCardInfo.cardId) { ArrayList() } += TransactionInfo(
                transactionInfo = "move '${paymentRequest.amount}' to card '${toCardInfo.cardId}'",
                historyCardAmount = newFromAmount
            );

            transactionHistory.getOrPut(toCardInfo.cardId) { ArrayList() } += TransactionInfo(
                transactionInfo = "get '${paymentRequest.amount}' from card '${fromCardInfo.cardId}'",
                historyCardAmount = newToAmount
            );

            fromCardInfo.amount = newFromAmount
            toCardInfo.amount = newToAmount

            return SuccessResponse()
        } finally {
            listOf(fromCardInfo, toCardInfo)
                .forEach { ignoreError { it.cardMutex.unlock() } }
        }
    }
}

private fun ignoreError(action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {

    }
}

private fun BigDecimal.toCardInfo(cardId: Long) = CardInfo(cardId, this, Mutex())
