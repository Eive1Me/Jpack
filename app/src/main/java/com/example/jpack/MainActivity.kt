package com.example.jpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.*
import com.example.jpack.ui.theme.JpackTheme

class MainActivity : ComponentActivity() {

    lateinit var currMap: HashMap<String, String>
    // Instantiate the cache
    lateinit var cache: DiskBasedCache

    // Set up the network to use HttpURLConnection as the HTTP client.
    lateinit var network : BasicNetwork

    // Instantiate the RequestQueue with the cache and network. Start the queue.
    lateinit var requestQueue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        network = BasicNetwork(HurlStack())

        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        currMap = HashMap<String, String>()

        getList()

        setContent {
            JpackTheme {
                // A surface container using the 'background' color from the theme
                Row(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val text = remember { mutableStateOf("") }
                    TextField(
                        value = text.value,
                        onValueChange = {
                            getData(curr = it)
                            text.value = it
                        },
                        label = {Text(text="Введите криптовалюту")})
                }
            }
        }
    }

    private fun getData(curr: String){
        val url = "https://api.coingecko.com/api/v3/coins/"
        if (curr in currMap.keys){
            println(currMap[curr])
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url + currMap[curr], null,
                { response ->
                    println("Response: %s".format(response.toString()))

                    val tmpName = response.getString("name")
                    val tmpUsdPrice = response.getJSONObject("market_data").getJSONObject("current_price").getInt("usd")
                    val tmpImage = response.getJSONObject("image").getString("large")

                    println("$tmpName $tmpUsdPrice $tmpImage")

                },
                { error ->
                    println("L $error")
                }
            )
            requestQueue.add(jsonObjectRequest)
        }
    }

    private fun getList(){
        val url = "https://api.coingecko.com/api/v3/coins/list"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                currMap = HashMap<String, String>(response.length())
                println("Response: %s".format(response.toString()))
                val t = response.length()-1
                for (i in 0..t) {
                    val tmp = response.getJSONObject(i)
                    currMap.put(tmp.getString("name"), tmp.getString("id"))
                }
                println(currMap)

            },
            { error ->
                println("L $error")
            }
        )
        requestQueue.add(jsonArrayRequest)
    }
}

@Composable
fun Stat(status: String) {
    Text(text = "App is $status!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JpackTheme {
        Stat("loading")
    }
}