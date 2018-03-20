package br.com.vedrus.vconnectmail.handler

import javax.mail.Message

interface MailHandler {
    fun handleMessage(message : Message)
}