package com.tonysheng.http.client

import com.tonysheng.http.client.request.AbsRequest

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/21
 * @Time: 16:34
 * @version: V1.0
 */
object HttpClientProxy : IHttpClient {

    private val client = HttpClientImp()

    override fun send(absRequest: AbsRequest) {
        client.send(absRequest)
    }
}