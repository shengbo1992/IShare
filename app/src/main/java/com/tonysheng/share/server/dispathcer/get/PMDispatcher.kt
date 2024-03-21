package com.tonysheng.share.server.dispathcer.get

import com.tonysheng.request.PMReq
import com.tonysheng.request.PmRSp
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import java.io.InputStream

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/20
 * @Time: 20:38
 * @version: V1.0
 */
class PMDispatcher : Dispatcher() {

    override suspend fun dispatch(call: ApplicationCall) {

        val request = call.receive(PMReq::class)
        request.params.let {
            val process = Runtime.getRuntime().exec(it)
            val result = process.waitFor()
            var res: String = ""
            res = if (result == 0) {
                readString(process.inputStream)
            } else {
                readString(process.errorStream)
            }
            call.respond(PmRSp(result, res))
        }
    }

    private fun readString(inputStream: InputStream): String {
        val stringBuilder = StringBuilder()
        var result: String?
        inputStream.bufferedReader().use {
            while (it.readLine().also { result = it } != null) {
                stringBuilder.append(result)
            }
        }

        return stringBuilder.toString()
    }
}