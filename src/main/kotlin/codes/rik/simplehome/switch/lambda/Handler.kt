package codes.rik.simplehome.switch.lambda

import codes.rik.lambda.apigateway.ApiGatewayInput
import codes.rik.lambda.apigateway.DelegatingApiGatewayLambdaHandler
import codes.rik.lambda.apigateway.ProtobufApiGatewayLambdaHandler
import codes.rik.simplehome.switches.api.Switch.SetSwitchRequest
import codes.rik.simplehome.switches.api.Switch.SetSwitchResponse
import com.google.protobuf.AbstractMessage

class SwitchDelegatingLambdaHander: DelegatingApiGatewayLambdaHandler<AbstractMessage>() {
    override fun getDelegate(input: ApiGatewayInput) = when(input.headers?.operation) {
        "SetSwitch" -> SetSwitchLambdaHandler
        else -> throw IllegalArgumentException("Unknown operation: ${input.headers?.operation}")
    }
}

object SetSwitchLambdaHandler: ProtobufApiGatewayLambdaHandler<SetSwitchRequest, SetSwitchResponse>(SetSwitchRequest.getDefaultInstance()) {
    override fun handle0(input: SetSwitchRequest, callback: (SetSwitchResponse) -> Unit) {
        callback(SetSwitchResponse.newBuilder()
                .setStatus(true)
                .build())
    }

}

