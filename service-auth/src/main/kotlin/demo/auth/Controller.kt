package demo.auth

import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class AuthController(
    val authConfig: AuthConfig
) {
    private val successTokens = setOf(
        "auth-token1",
        "auth-token2",
        "auth-token3"
    )

    @GetMapping(value = ["{token}"])
    fun info(
        @PathVariable(name = "token") token: String,
        response: ServerHttpResponse
    ): Response {
        if (token !in successTokens) {
            response.statusCode = HttpStatus.FORBIDDEN
            return ForbiddenResponse(
                error = "access is forbidden for token='$token' (token not find)"
            )
        }

        return AuthResponse(
            cardAccess = Random.nextDouble() <= authConfig.successRate,
            paymentAccess = Random.nextDouble() <= authConfig.successRate,
            userAccess = Random.nextDouble() <= authConfig.successRate
        )
    }
}
