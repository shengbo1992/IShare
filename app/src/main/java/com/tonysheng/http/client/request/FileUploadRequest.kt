package com.tonysheng.http.client.request

import java.io.File
import java.io.InputStream

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/22
 * @Time: 10:17
 * @version: V1.0
 */
class FileUploadRequest(val fileName: String, val fileLength: Long, val inputStream: InputStream) {

    var upload: ((uploadSize: Long, totalSize: Long) -> Unit)? = null

    constructor(file: File) : this(file.name, file.length(), file.inputStream()) {
    }

    var callBack: CallBack? = null

    fun onUpload(upload: ((uploadSize: Long, totalSize: Long) -> Unit)? )
    {
        this.upload = upload
    }
    interface CallBack {
        fun onSuccess(request: FileUploadRequest, res: Any?)
        fun onFailed(code: Int, message: String)
    }
}



