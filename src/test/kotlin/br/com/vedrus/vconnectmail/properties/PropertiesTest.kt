package br.com.vedrus.vconnectmail.properties

import org.junit.Assert
import org.junit.Test

class PropertiesTest {

    val properties = "1=one\n2=two".byteInputStream()

    @Test
    fun loadPropertiesFromInputStream() {
        Properties.loadFromStream(properties)
        assertProperties()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getInvalidProperty() {
        Properties.loadFromStream(properties)
        Properties["3"]
    }

    private fun assertProperties() {
        Assert.assertEquals("one", Properties["1"])
        Assert.assertEquals("two", Properties["2"])
    }
}