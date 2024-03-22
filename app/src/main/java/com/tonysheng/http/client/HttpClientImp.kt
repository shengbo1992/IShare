package com.tonysheng.http.client

import com.tonysheng.http.client.request.AbsRequest
import com.tonysheng.http.client.request.FileUploadRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.streams.asInput
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/21
 * @Time: 16:43
 * @version: V1.0
 */
class HttpClientImp : IHttpClient {

    // val contontion = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).
    private val httpclient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    override fun send(absRequest: AbsRequest) {

        GlobalScope.launch {
            val response = httpclient.post(ConstConfig.funcServerUrl) {
                contentType(ContentType.Application.Json)
                setBody(absRequest.req)
                header(ConstConfig.head_funcName, absRequest.funcName)
            }
            if (response.status.value == 200) {
                val resultData = response.call.bodyNullable(absRequest.typeInfo)
                absRequest.callBack?.onSuccess(absRequest, resultData)
            } else {
                absRequest.callBack?.onFailed(response.status.value, response.status.description)
            }
        }
    }

    override fun upload(fileUploadRequest: FileUploadRequest) {
        GlobalScope.launch {
            val response = httpclient.submitFormWithBinaryData(ConstConfig.uploadServerUrl, formData {
                appendInput(
                    "file", headers = Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${fileUploadRequest.fileName}")
                    }, fileUploadRequest.fileLength
                ) {
                    fileUploadRequest.inputStream.asInput()
                }
            }) {
                onUpload { bytesSentTotal, contentLength ->
                    fileUploadRequest.upload?.invoke(bytesSentTotal, contentLength)
                }
            }
            if (response.status.value == 200) {
                fileUploadRequest.callBack?.onSuccess(fileUploadRequest, null)
            } else {
                fileUploadRequest.callBack?.onFailed(response.status.value, response.status.description)
            }
        }
    }
}