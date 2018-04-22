package codes.rik.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.google.common.io.CharStreams
import mu.KotlinLogging
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream

private val logger = KotlinLogging.logger {}

abstract class LambdaHandler<in T>(private val parser: (String) -> T): RequestStreamHandler {
    override fun handleRequest(input: InputStream, output: OutputStream, context: Context) {
        // Receive input string
        val inputString = CharStreams.toString(InputStreamReader(input, Charsets.UTF_8))
        logger.debug { "Received input: $inputString" }

        // Parse input
        val inputContainer = parser(inputString)
        logger.debug { "Parsed input: $input" }

        // Handle input
        handle(inputContainer, output)
    }

    abstract fun handle(input: T, output: OutputStream)
}