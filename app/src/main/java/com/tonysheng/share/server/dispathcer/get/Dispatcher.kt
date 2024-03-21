package com.tonysheng.share.server.dispathcer.get

import com.tonysheng.share.server.dispathcer.FileUploader
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.header

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/20
 * @Time: 20:35
 * @version: V1.0
 */
abstract class Dispatcher {

    abstract suspend fun dispatch(call: ApplicationCall)

    companion object {

        suspend fun dispatch(call: ApplicationCall) {
            when (call.request.header("funcName")) {
                "pm" -> {
                    PMDispatcher().dispatch(call)
                }
            }
        }

        suspend fun upload(call: ApplicationCall) {
            FileUploader().upload(call)
        }
    }
}