package com.tonysheng.http.client.request

import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: 基础请求封装
 * @User: tonysheng
 * @Date: 2024/3/20
 * @Time: 20:12
 * @version: V1.0
 */
class AbsRequest() {
    lateinit var funcName: String
        private set
    lateinit var req: Any
        private set

    lateinit var typeInfo: TypeInfo

    var callBack: CallBack? = null

    inline fun <reified T> setRsp() {
        typeInfo = typeInfo<T>()
    }

    fun setFunName(funcName: String) {
        this.funcName = funcName
    }

    fun setReq(request: Any) {
        req = request
    }

    interface CallBack {
        fun onSuccess(request: AbsRequest, res: Any?)
        fun onFailed(code: Int, message: String)
    }

    companion object {
        // val sa = AbsRequest().apply {
        // }
    }
}

