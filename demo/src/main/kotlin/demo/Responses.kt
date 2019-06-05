package demo

interface Response {
    val status: Boolean
    val error: String?
}

data class SimpleResponse(
    override val status: Boolean,
    val errorCode: Int,
    override val error: String? = null
) : Response
