package br.com.vedrus.vconnectmail.reader.impl

import br.com.vedrus.vconnectmail.handler.MailHandler
import br.com.vedrus.vconnectmail.reader.MailReader
import br.com.vedrus.vconnectmail.reader.impl.data.Credentials
import br.com.vedrus.vconnectmail.reader.impl.data.Protocol
import mu.KotlinLogging
import java.util.*
import javax.mail.*
import javax.mail.search.*
import kotlin.concurrent.fixedRateTimer

private val logger = KotlinLogging.logger {}

class MailReaderImpl (protocol : Protocol,
                      credentials : Credentials) : MailReader {

    override val DEFAULT_CHECK_FREQUENCY = 10L // in seconds
    override val DEFAULT_FOLDER = "INBOX"

    val credentials = credentials
    val store = buildStore(protocol)

    override fun readMailOnce(mailHandler: MailHandler, folder : String) {

        logger.info { "Reading mail - Handler: $mailHandler" }

        if (!store.isConnected)
            store.connect(credentials.user,credentials.password)

        val inbox = store.getFolder(folder)

        inbox.open(Folder.READ_WRITE)

        searchMessages(inbox, Flags(Flags.Flag.SEEN)).forEach {
            mailHandler.handleMessage(it)
        }
    }

    override fun readMailForever(mailHandler: MailHandler, checkFrequency : Long, folder : String) {

        logger.info { "Reading mail forever - Freq: $checkFrequency seconds - Handler: $mailHandler" }

        fixedRateTimer(period = checkFrequency * 1000) {
            readMailOnce(mailHandler)
        }
    }

    private fun buildStore (protocol: Protocol) : Store {

        val properties = Properties(protocol.properties)
        properties.put("mail.host", protocol.host)
        properties.put("mail.port", protocol.port)
        properties.put("mail.imaps.ssl.trust", "*");

        val session = Session.getInstance(properties)
        return session.getStore(protocol.store)
    }

    private fun searchMessages(inbox : Folder, flag : Flags) : Array<Message> {

        val messages = inbox.search(FlagTerm(flag, false))

        logger.info { "Found ${messages.size} messages" }

        return messages
    }
}