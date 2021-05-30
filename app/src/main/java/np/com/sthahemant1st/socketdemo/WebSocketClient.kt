package np.com.sthahemant1st.socketdemo

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener

class WebSocketClient {
    private lateinit var webSocket: okhttp3.WebSocket
    var socketUrl = ""
    var onReconnect: () -> Unit = {}
    var shouldReconnect = true

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
            //called when connection succeeded
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
            //called when text message received
            super.onMessage(webSocket, text)
        }

        override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
            //called when binary message received
            super.onClosing(webSocket, code, reason)
        }

        override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
            //called when no more messages and the connection should be released
            super.onClosed(webSocket, code, reason)
            if (shouldReconnect) onReconnect()
        }

        override fun onFailure(
            webSocket: okhttp3.WebSocket, t: Throwable, response: Response?
        ) {
            super.onFailure(webSocket, t, response)
//            loggerE("onFailure: localizedMessage=${t.localizedMessage}, response=${response?.body?.string()}")
            if (shouldReconnect) onReconnect()
        }
    }

    companion object {
        private lateinit var instance: WebSocketClient

        @JvmStatic
        @Synchronized
        /**
         * This function gives singleton instance of WebSocket.
         * */
        fun getInstance(): WebSocketClient {
            synchronized(WebSocketClient::class) {
                if (!::instance.isInitialized) {
                    instance = WebSocketClient()
                }
            }
            return instance
        }
    }

    private fun initWebSocket() {
        val request = Request.Builder().url(socketUrl).build()
        webSocket = OkHttpClient().newWebSocket(request, webSocketListener)
    }

    fun reconnect() {
        onReconnect()
        shouldReconnect = true
    }

    fun connect() {
        initWebSocket()
    }

    fun sendMessage(message: String){
        webSocket.send(message)
    }

    /*
    *We can close socket by two way:
    *
    * 1. websocket.webSocket.close(1000, "Dont need connection")
    *This attempts to initiate a graceful shutdown of this web socket.
    *Any already-enqueued messages will be transmitted before the close message is sent but
    *subsequent calls to send will return false and their messages will not be enqueued.
    *
    * 2. websocket.cancel()
    * This immediately and violently release resources held by this web socket,
    *  discarding any enqueued messages.
    *
    * Both does nothing if the web socket has already been closed or canceled.
    * */
    fun disconnect() {
        webSocket.close(1000, "Do not need connection anymore.")
        shouldReconnect = false
    }
}
