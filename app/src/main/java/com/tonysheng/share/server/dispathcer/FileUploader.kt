package com.tonysheng.share.server.dispathcer

import com.tonysheng.share.ContextHolder
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import java.io.File

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/20
 * @Time: 21:06
 * @version: V1.0
 */
class FileUploader {

    suspend fun upload(call: ApplicationCall) = kotlin.runCatching {
        val multipart = call.receiveMultipart()
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    val fileName = part.originalFileName
                    val file = File(getUploadPath(), fileName)
                    part.streamProvider().use { input ->
                        file.outputStream().buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                }

                is PartData.BinaryItem -> {
                }

                else -> {}
            }
            part.dispose()
        }
        call.respond(HttpStatusCode.OK, "File uploaded successfully")
    }.onFailure {
        it.printStackTrace()
    }

    private fun getUploadPath(): File {
        val file = File(ContextHolder.context.filesDir, "upload")
        if(!file.exists())
        {
            file.mkdirs()
        }
        return  file
    }
}