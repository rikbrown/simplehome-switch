package codes.rik.lambda.apigateway

abstract class DelegatingApiGatewayLambdaHandler<O>: ApiGatewayLambdaHandler<O>() {
    abstract fun getDelegate(input: ApiGatewayInput): ApiGatewayLambdaHandler<O>

    override fun handleApi(input: ApiGatewayInput, callback: (O) -> Unit) = getDelegate(input).handleApi(input, callback)
    override fun encodeResponseBody(input: ApiGatewayInput, response: O) = getDelegate(input).encodeResponseBody(input, response)
}