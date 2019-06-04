package demo

import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "demo-config")
data class DemoConfig (
    val param1: String,
    val notRequiredParam: String? = "some default value",
    val subConfig: SubConfig
) {
    @PostConstruct
    fun init() {
        println(this)
    }
}

data class SubConfig(
    val subConfigParam1: SubParam1,
    val subConfigNotRequiredParam: String? = "default value"
)

enum class SubParam1 {
    VALUE1,
    VALUE2
}
