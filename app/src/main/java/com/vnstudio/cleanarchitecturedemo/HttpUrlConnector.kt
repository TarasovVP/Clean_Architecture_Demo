package com.vnstudio.cleanarchitecturedemo

import android.os.Handler
import com.vnstudio.cleanarchitecturedemo.MainActivity.Companion.ERROR
import com.vnstudio.cleanarchitecturedemo.MainActivity.Companion.SUCCESS_HTTPS_CONNECTION
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpUrlConnector {

    fun makeHttpUrlConnection(handler: Handler) {
        val thread = Thread {
            try {
                val url = "https://api.github.com/repos/octocat/Spoon-Knife/forks"
                val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode: Int = connection.responseCode
                if (responseCode == 200) {
                    val inputStream: InputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String? = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = reader.readLine()
                    }
                    val responseData = stringBuilder.toString()
                    val message = handler.obtainMessage(SUCCESS_HTTPS_CONNECTION, responseData)
                    handler.sendMessage(message)
                } else {
                    val message = handler.obtainMessage(ERROR, "Error responseCode $responseCode")
                    handler.sendMessage(message)
                }
            } catch (e: Exception) {
                val message = handler.obtainMessage(ERROR, e.localizedMessage)
                handler.sendMessage(message)
            }
        }
        thread.start()
    }
}