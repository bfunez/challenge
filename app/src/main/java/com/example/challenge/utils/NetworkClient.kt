package com.example.challenge.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class NetworkClient {

    /**
     * Function to create a HttpURLConnection to retrieve data as a string
     * @param URL String
     * @return String
     */
    fun request(url: String): String {
        val sb = StringBuilder()
        val urlConnection = URL(url).openConnection() as HttpURLConnection
        try {
            val stream: InputStream = BufferedInputStream(urlConnection.inputStream)
            val reader = BufferedReader(InputStreamReader(stream))
            // temp string to append each line from the buffer
            var inputLine: String?
            while (reader.readLine().also { inputLine = it } != null) {
                sb.append(inputLine)
            }
            stream.close()
            reader.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
            throw IOException(exception.localizedMessage)
        } finally {
            // regardless of success or failure,  disconnect
            urlConnection.disconnect()
        }
        return sb.toString()
    }

    /**
     * Request to retrieve a the image as a bitmap
     * Credits to https://github.com/rpandey1234/ImageLoadingAndroid
     * @param url String
     * @return Bitmap?
     */
    fun requestToDownloadImage(url: String): Bitmap? {
        val urlConnection = URL(url).openConnection()
        return try {
            urlConnection.connect()
            val stream = urlConnection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(stream)
            stream.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}