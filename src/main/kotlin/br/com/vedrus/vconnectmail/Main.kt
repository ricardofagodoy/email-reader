package br.com.vedrus.vconnectmail

import br.com.vedrus.vconnectmail.properties.Properties
import br.com.vedrus.vconnectmail.starter.ImapStarter
import mu.KotlinLogging
import java.io.File
import java.io.IOException

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {

    logger.info { "Started vconnect-mail" }

    loadPropertiesFromArgs(args);

    ImapStarter().start()
}

private fun loadPropertiesFromArgs(args: Array<String>) {
    try {

        if (args.size == 0){
            logger.error { "Falha ao ler arquivo de configuracao. Adicione o caminho do arquivo de configuracao no argumento." }
            throw Exception("Falha ao ler arquivo de configuracao. Adicione o caminho do arquivo de configuracao no argumento.");
        }

        var file = File(args[0]);

        Properties.loadFromStream(file.inputStream());
    }catch(e: IOException){
        logger.error { "Falha ao ler arquivo de configuracao." }
        throw e
    }
}