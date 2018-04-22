package codes.rik.lambda.apigateway
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.annotations.SerializedName
import java.util.*

data class ApiGatewayInput(
        val body: String,
        val headers: Headers?
) {
    companion object {
        fun read(string: String): ApiGatewayInput = gson.fromJson(string)
    }

    data class Headers(
            @SerializedName("Content-Type") val contentType: ContentType?,
            @SerializedName("Accept") val accept: ContentType?,
            @SerializedName("Operation") val operation: String?)
}

data class ApiGatewayOutput(
        val body: String,
        val statusCode: Int,
        val isBase64Encoded: Boolean,
        val headers: Map<String, String>) {

    companion object {
        fun create(output: OutputBody, statusCode: Int = 200, headers: Map<String, String>  = mapOf()) =
                ApiGatewayOutput(
                        body = output.body,
                        statusCode = statusCode,
                        isBase64Encoded = output.isBase64Encoded,
                        headers = headers)
    }
}

sealed class OutputBody(val body: String, val isBase64Encoded: Boolean)
class StringOutputBody(string: String): OutputBody(string, isBase64Encoded = false)
class BinaryOutputBody(bytes: ByteArray): OutputBody(Base64.getEncoder().encodeToString(bytes), isBase64Encoded = true)
