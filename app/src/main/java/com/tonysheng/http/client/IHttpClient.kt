package com.tonysheng.http.client

import com.tonysheng.http.client.request.AbsRequest
import com.tonysheng.http.client.request.FileUploadRequest

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/21
 * @Time: 16:42
 * @version: V1.0
 */
interface IHttpClient {

    fun send(absRequest: AbsRequest)

    fun upload(fileUploadRequest: FileUploadRequest)
}