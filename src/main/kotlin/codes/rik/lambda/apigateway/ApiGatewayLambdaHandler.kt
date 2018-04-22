package codes.rik.lambda.apigateway

import codes.rik.lambda.LambdaHandler
import com.github.salomonbrys.kotson.typedToJson
import com.google.gson.stream.JsonWriter
import mu.KotlinLogging
import java.io.OutputStream
import java.io.OutputStreamWriter
import kotlin.text.Charsets.UTF_8

abstract class ApiGatewayLambdaHandler<O>: LambdaHandler<ApiGatewayInput>(ApiGatewayInput.Companion::read) {
    protected val poweredBy = javaClass.canonicalName!!

    override fun handle(input: ApiGatewayInput, output: OutputStream) {
        // Actually handle the request
        handleApi(input) { response ->
            // Got a response
            logger.info { "Response: $response" }

            // Encode a response body
            val apiOutput = ApiGatewayOutput.create(
                    headers = mapOf("X-Powered-By" to poweredBy),
                    output = encodeResponseBody(input, response))
            logger.debug { "Output: $apiOutput" }

            JsonWriter(OutputStreamWriter(output, UTF_8)).use {
                gson.typedToJson(apiOutput, it)
            }
        }
    }

    abstract fun handleApi(input: ApiGatewayInput, callback: (O) -> Unit)
    abstract fun encodeResponseBody(input: ApiGatewayInput, response: O): OutputBody
}

private val logger = KotlinLogging.logger {}
