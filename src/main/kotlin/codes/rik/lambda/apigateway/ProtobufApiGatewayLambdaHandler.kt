package codes.rik.lambda.apigateway

import com.google.protobuf.AbstractMessage
import com.google.protobuf.util.JsonFormat
import java.util.*

abstract class ProtobufApiGatewayLambdaHandler<in I: AbstractMessage, out O: AbstractMessage>(private val prototype: I):
ApiGatewayLambdaHandler<AbstractMessage>() {

    abstract fun handle0(input: I, callback: (O) -> Unit)

    override fun handleApi(input: ApiGatewayInput, callback: (AbstractMessage) -> Unit) {
        val request = parseRequest(input)
        handle0(request, callback)
    }

    override fun encodeResponseBody(input: ApiGatewayInput, response: AbstractMessage) = when(input.headers?.accept ?: ContentType.PROTOBUF) {
        ContentType.PROTOBUF -> BinaryOutputBody(response.toByteArray())
        ContentType.JSON -> StringOutputBody(JsonFormat.printer().print(response))
    }

    private fun parseRequest(input: ApiGatewayInput) = parseRequestBody(input.body, input.headers?.contentType)

    @Suppress("UNCHECKED_CAST")
    private fun parseRequestBody(body: String, accept: ContentType?) = when(accept ?: ContentType.PROTOBUF) {
            ContentType.PROTOBUF -> prototype.parserForType.parseFrom(Base64.getDecoder().decode(body)) as I
            ContentType.JSON -> prototype.newFromJson(body) as I
        }


    private fun AbstractMessage.newFromJson(json: String) = with(newBuilderForType()) {
        com.google.protobuf.util.JsonFormat.parser().merge(json, this)
        build()
    }
}