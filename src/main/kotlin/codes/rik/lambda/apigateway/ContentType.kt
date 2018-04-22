package codes.rik.lambda.apigateway

import com.github.salomonbrys.kotson.string
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import java.lang.reflect.Type

@JsonAdapter(ContentTypeDeserializer::class)
enum class ContentType(val httpContentType: String) {
    JSON("application/json"),
    PROTOBUF("application/protobuf");
}

private object ContentTypeDeserializer: JsonDeserializer<ContentType> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
            json?.asJsonPrimitive?.string
                    ?.trim()
                    ?.let { string -> ContentType.values().firstOrNull { it.httpContentType == string } }
}
