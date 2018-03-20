package br.com.vedrus.vconnectmail.reader.impl.data

import java.util.Properties

data class Protocol (val host : String, val port : String, val store : String, val properties : Properties? = Properties())
