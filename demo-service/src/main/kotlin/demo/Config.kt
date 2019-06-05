package demo

import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "url")
@Validated
data class DemoConfig(
    val auth: String,
    val card: String,
    val payment: String,
    val user: String
)
