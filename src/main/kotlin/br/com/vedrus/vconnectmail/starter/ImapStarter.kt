package br.com.vedrus.vconnectmail.starter

import br.com.vedrus.vconnectmail.handler.MailHandler
import br.com.vedrus.vconnectmail.handler.impl.SaveToFolderHandler
import br.com.vedrus.vconnectmail.properties.Properties
import br.com.vedrus.vconnectmail.reader.MailReader
import br.com.vedrus.vconnectmail.reader.impl.MailReaderImpl
import br.com.vedrus.vconnectmail.reader.impl.data.Credentials
import br.com.vedrus.vconnectmail.reader.impl.data.Protocol
import mu.KotlinLogging

class ImapStarter {

    private val logger = KotlinLogging.logger {}

    fun start() {
        logger.info { "Starting up IMAP Mail ..." }
        setupImapReader().readMailForever(setupSaveToFolderHandler())
    }

    private fun setupImapReader() : MailReader {

        logger.info { "Setting up IMAP Mail Reader..." }

        val protocol = Protocol(Properties["imap.host"],
                                Properties["imap.port"],
                                Properties["imap.store"])

        val credentials = Credentials(Properties["imap.username"],
                                      Properties["imap.password"])

        return MailReaderImpl(protocol, credentials)
    }

    private fun setupSaveToFolderHandler() : MailHandler {
        logger.info { "Setting up SaveToFolder Mail Handler..." }
        return SaveToFolderHandler()
    }
}