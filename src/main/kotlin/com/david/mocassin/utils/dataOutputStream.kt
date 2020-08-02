package com.david.mocassin.utils

import java.io.*
import java.lang.StringBuilder

fun saveStringToBinaryFile(pathName: String, content: String) {
    DataOutputStream(FileOutputStream(pathName)).use { dos ->
        with(dos) {
            writeInt(content.length)
            writeChars(content)
        }
    }
}

fun readStringFromBinaryFile(file: File): String {
    val outputStr = StringBuilder()

    DataInputStream(FileInputStream(file)).use { dos ->
        with(dos) {
            val txtSize = readInt()
            for (i in 0 until txtSize) {
                outputStr.append(readChar())
            }
        }
    }

    return outputStr.toString()
}