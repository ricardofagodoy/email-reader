package br.com.vedrus.vconnectmail.reader

import br.com.vedrus.vconnectmail.handler.MailHandler

interface MailReader {

    val DEFAULT_CHECK_FREQUENCY : Long
    val DEFAULT_FOLDER : String

    fun readMailOnce(mailHandler : MailHandler, folder : String = DEFAULT_FOLDER)
    fun readMailForever(mailHandler : MailHandler, checkFrequency : Long = DEFAULT_CHECK_FREQUENCY, folder : String = DEFAULT_FOLDER)
}