package demo.card

import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class Card(
    val cardId: Long,
    val cardNumber: String,
    val validTo: String
)

@RestController
class AuthController(
) {
    private val cards: Map<String, Card> = listOf(
        Card(1L, "5559 **** 3478", "03/21"),
        Card(2L, "5559 **** 2020", "03/21"),
        Card(3L, "5559 **** 4509", "03/21")
    ).associateBy { it.cardNumber }

    @GetMapping
    fun info(
        @RequestBody cardNumber: String,
        response: ServerHttpResponse
    ): Response {
        val card = cards[cardNumber] ?: run {
            response.statusCode = HttpStatus.NOT_FOUND
            return NotFoundResponse(
                error = "card with number='$cardNumber' not found"
            )
        }

        return CardResponse(
            cardId = card.cardId,
            cardNumber = card.cardNumber,
            validTo = card.validTo
        )
    }
}
