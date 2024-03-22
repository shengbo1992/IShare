package com.tonysheng.share.server

import android.util.Log
import com.tonysheng.share.server.dispathcer.get.Dispatcher
import io.ktor.serialization.kotlinx.json.json

import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.request.httpMethod
import io.ktor.server.request.uri
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.application.install

import  io.ktor.server.plugins.contentnegotiation.*

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/20
 * @Time: 16:24
 * @version: V1.0
 */
object Server {
    private const val TAG = "Server"

    @JvmStatic
    fun main(args: Array<String>) {
        startServer()
    }

    @JvmStatic
    fun startServer() {
        val server = embeddedServer(Netty, port = 8080) {
            install(ContentNegotiation) {
                json()
            }
            routing {
                intercept(ApplicationCallPipeline.Monitoring) {
                    // 在所有请求处理完成后执行
                    Log.d(TAG, "请求上来了--- ${call.request.uri}  ${call.request.httpMethod}")
                }
                // get("/call") {
                //     Dispatcher.dispatch(call)
                // }
                post("/call")
                {
                    Dispatcher.dispatch(call)
                }
                post("/upload") {
                    Dispatcher.upload(call)
                }

            }
        }
        server.start(wait = true)
        Log.d("Server", "start server success")
    }
}
