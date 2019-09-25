package com.mercariapp.testutils

import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Assertions.assertNotNull
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

fun stringResource(name: String): String = readStringResourceFile(name)

private fun readStringResourceFile(
    file: String,
    loader: ClassLoader = ClassLoader.getSystemClassLoader()
): String {
    try {
        return IOUtils.toString(getInputStream(file, loader), Charset.forName("UTF-8"))
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}

private fun getInputStream(file: String, loader: ClassLoader): InputStream {
    val inputStream = loader.getResourceAsStream(file)
    assertNotNull(inputStream, "File not found $file")
    return inputStream
}
