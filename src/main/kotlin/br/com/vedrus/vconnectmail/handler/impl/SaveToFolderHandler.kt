package br.com.vedrus.vconnectmail.handler.impl

import br.com.vedrus.vconnectmail.handler.MailHandler
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Part
import javax.mail.internet.MimeBodyPart
import java.io.File
import javax.mail.internet.MimeMultipart
import java.time.LocalTime



class SaveToFolderHandler : MailHandler {

    override fun handleMessage(message: Message) {
        val fromAddress = message.from
        val from = fromAddress[0].toString()
        val subject = message.subject
        val sentDate = message.sentDate.toString()

        val contentType = message.contentType
        var messageContent = ""

        // store attachment file name, separated by comma
        var attachFiles = ""

        if (contentType.contains("multipart")) {
            // content may contain attachments
            val multiPart = message.content as Multipart
            val numberOfParts = multiPart.count
            for (partCount in 0..numberOfParts - 1) {
                val part = multiPart.getBodyPart(partCount) as MimeBodyPart
                if (Part.ATTACHMENT.equals(part.disposition, ignoreCase = true)) {
                    // this part is attachment
                    val fileName = part.fileName

                    if ((fileName.contains(".PDF")) || (fileName.contains(".XML")) || (fileName.contains(".pdf")) || (fileName.contains(".xml"))) {

                        var attachmentFile = File("." + File.separator + "attachments" + File.separator + getCurrentTime() + "_" + fileName)

                        if (!attachmentFile.parentFile.exists())
                            attachmentFile.parentFile.mkdirs()

                        attachFiles += fileName + ", "
                        part.saveFile(attachmentFile)
                    }
                } else {
                    // this part may be the message content
                    messageContent = part.content.toString()
                }
            }

            if (attachFiles.length > 1) {
                attachFiles = attachFiles.substring(0, attachFiles.length - 2)
            }
        } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
            val content = message.content
            if (content != null) {
                messageContent = content.toString()
            }
        }

// print out details of each message
        System.out.println("Message #" + ":")
        println("\t From: " + from)
        println("\t Subject: " + subject)
        println("\t Sent Date: " + sentDate)
        println("\t Message: " + messageContent)
        println("\t Attachments: " + attachFiles)

        var bodyContent = getTextFromMessage(message);

        println("\t Body content: " + bodyContent)

        var linkFile = File("." + File.separator + "links" + File.separator + getCurrentTime() + ".link")

        if (!linkFile.parentFile.exists())
            linkFile.parentFile.mkdirs()

        linkFile.printWriter().use { out ->
            out.println(bodyContent)
        }
    }

    private fun getCurrentTime(): String{
        return LocalTime.now().toString().replace("[^0-9]".toRegex(), "")
    }


    @Throws(Exception::class)
    private fun getTextFromMessage(message: Part): String {

        if (message.isMimeType("text/plain") && message.fileName == null)
            return message.content.toString()

        if (message.isMimeType("multipart/*")) {

            var result = ""
            val mimeMultipart = message.content as MimeMultipart

            for (i in 0..mimeMultipart.getCount() - 1) {



                result += getTextFromMessage(mimeMultipart.getBodyPart(i))

            }

            return result
        }

        return ""
    }
}