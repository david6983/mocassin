package edu.upm.david.security.gamalcrypt.cryptography

/**
 * Utility class to encrypt and decrypt a text.
 *
 */
object Cryptorithm {
    /**
     * Encrypt a plaintext string with a custom cryptographic algorithm.
     *
     * @param plaintext text to encipher
     * @return ciphertext of the plaintext
     */
    fun encrypt(plaintext: String): String {
        // each character is represented by a binary string
        val asciiArray = arrayOfNulls<String>(plaintext.length)
        // use a string builder to join the result of each character
        val builder = StringBuilder()

        // for each characters
        for (i in asciiArray.indices) {
            // replace each alphabet with its equivalent 7-bit ASCII code
            asciiArray[i] = Integer.toBinaryString(plaintext[i].toInt())

            // Add a 0 bit at the leftmost bit to make each of the above bit patterns 8 position long.
            asciiArray[i] = "0".repeat(8 - asciiArray[i]!!.length) + asciiArray[i]

            // Swap the first four bits with the last four bits for each alphabet.
            asciiArray[i] = asciiArray[i]!!.substring(4, 8) + asciiArray[i]!!.substring(0, 4)

            // Write the hexadecimal equivalent of every for bits
            asciiArray[i] = Integer.toString(asciiArray[i]!!.toInt(2), 16)

            // Add a 0 bit at the leftmost bit to make each of the above hex patterns 2 position long.
            asciiArray[i] = "0".repeat(2 - asciiArray[i]!!.length) + asciiArray[i]

            // add the string to the builder for joining
            builder.append(asciiArray[i]!!.toUpperCase())
        }

        // join the resulted characters
        return builder.toString()
    }

    /**
     * Decrypt a ciphertext string with a custom cryptographic algorithm.
     *
     * @param ciphertext text to decipher
     * @return plaintext
     */
    fun decrypt(ciphertext: String): String {
        // use a string builder to join the result of each character
        val builder = StringBuilder()

        //separate the text in 2-position long hexadecimal number
        var hexArray = ciphertext.split(regex = "(?<=\\G..)".toRegex()).toTypedArray()
        hexArray.toMutableList().let {
            it.remove("")
            hexArray = it.toTypedArray()
        }

        // for every hexadecimal numbers
        for (str in hexArray) {
            var substr = str
            //get the binary string from hexadecimal string
            substr = Integer.toBinaryString(substr.toInt(16))

            // Add a 0 bit at the leftmost bit to make each of the above bit patterns 8 position long.
            substr = "0".repeat(8 - substr.length) + substr

            // Swap the first four bits with the last four bits for each alphabet.
            substr = substr.substring(4, 8) + substr.substring(0, 4)

            //retrieve ascii code of the binary string and convert it to plaintext
            substr = Character.toString(substr.toInt(2).toChar())

            // add the string to the builder for joining
            builder.append(substr)
        }

        // join the resulted characters
        return builder.toString()
    }
}