package br.com.vedrus.vconnectmail.properties

import mu.KotlinLogging
import java.io.InputStream

object Properties {

    private val logger = KotlinLogging.logger {}
    private val properties = java.util.Properties()

    fun loadFromStream(inputStream : InputStream) {
        properties.load(inputStream)
        logger.info { "Properties loaded: ${properties.toMap()}"}
    }

    operator fun get (key : String) : String {
        return properties.getProperty(key) ?:
                throw IllegalArgumentException("Invalid property name: $key")
    }
}