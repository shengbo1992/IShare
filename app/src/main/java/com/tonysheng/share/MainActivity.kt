package com.tonysheng.share

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tonysheng.http.client.request.AbsRequest
import com.tonysheng.request.PMReq
import com.tonysheng.request.PmRSp
import com.tonysheng.http.client.HttpClientProxy
import com.tonysheng.share.server.Server
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.streams.asInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            Server.startServer()
        }
    }

    fun OnClick(view: View) {
        when (view.id) {
            R.id.pm -> {
                basicRequest()
            }

            R.id.upload -> {
                GlobalScope.launch {

                    HttpClient(Android) {
                        install(ContentNegotiation) {
                        }
                    }.use {
                        val fd = assets.openFd("1.apk")
                        val response = it.submitFormWithBinaryData("http://localhost:8080/upload", formData {
                            appendInput(
                                "file",
                                headers = Headers.build {
                                    append(HttpHeaders.ContentDisposition, "filename=1.apk")
                                }, fd.length
                            ) {
                                fd.createInputStream().asInput()
                            }
                        }
                        ) {
                            onUpload { bytesSentTotal, contentLength ->
                                Log.d("UPLOAD", "${bytesSentTotal} / ${contentLength}")
                            }
                        }

                        withContext(Dispatchers.Main) {
                            AlertDialog.Builder(this@MainActivity).setMessage(response.bodyAsText()).show()
                        }
                    }
                }
            }
        }
    }

    private fun basicRequest() {
        val request = AbsRequest().apply {
            setFunName("pm")
            setReq(PMReq("pm list"))
            setRsp<PmRSp>()
            callBack = object : AbsRequest.CallBack {
                override fun onSuccess(request: AbsRequest, res: Any?) {
                    GlobalScope.launch(Dispatchers.Main) {
                        AlertDialog.Builder(this@MainActivity).setMessage(request.toString()).show()
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    AlertDialog.Builder(this@MainActivity).setMessage("faild code$code message $message").show()
                }
            }
        }
        HttpClientProxy.send(request)
    }
}