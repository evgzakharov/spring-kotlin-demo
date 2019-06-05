package demo

import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "url")
@Validated
class DemoConfig {
    @NotNull
    lateinit var auth: String

    @NotNull
    lateinit var card: String

    @NotNull
    lateinit var payment: String

    @NotNull
    lateinit var user: String
}
