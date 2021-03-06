package np.com.sthahemant1st.socketdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import np.com.sthahemant1st.socketdemo.ui.theme.SocketDemoTheme

class MainActivity : ComponentActivity() {
    private lateinit var webSocketClient: WebSocketClient
    //you can get your socket key form by registering to pieSocket form here
    // https://www.piesocket.com/register?plan=free
    private val socketKey = "OoxcCdu52cCwxFKF3SqRd6ZlLJW2g9OpMokNsIlw"

    private val socketListener = object : WebSocketClient.SocketListener {
        override fun onMessage(message: String) {
            Log.e("socketCheck onMessage", message)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webSocketClient = WebSocketClient.getInstance()
        webSocketClient.setSocketUrl("wss://demo.piesocket.com/v3/1?api_key=$socketKey&notify_self")
        webSocketClient.setListener(socketListener)
//        webSocketClient.connect()

        setContent {
            SocketDemoTheme {
                MainScreen(
                    onConnect = {
                        webSocketClient.connect()
                    },
                    onDisconnect = {
                        webSocketClient.disconnect()
                    },
                    onSendMessage = {
                        webSocketClient.sendMessage(it)
                    },
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    onSendMessage: (String) -> Unit,
    onDisconnect: () -> Unit,
    onConnect: () -> Unit,
) {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Socket Demo", modifier = Modifier.weight(1f))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = message,
                onValueChange = {
                    message = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                onSendMessage(message)
                message = ""
            }) {
                Text(text = "Send Message")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onConnect() }) {
                Text(text = "Connect Socket")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onDisconnect() }) {
                Text(text = "Disconnect Socket")
            }
        }

    }
}

