package com.tonysheng.share

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tonysheng.http.client.request.AbsRequest
import com.tonysheng.request.PMReq
import com.tonysheng.request.PmRSp
import com.tonysheng.http.client.HttpClientProxy
import com.tonysheng.http.client.request.FileUploadRequest
import com.tonysheng.share.server.Server
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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

                uploadFile()
            }
        }
    }

    private fun uploadFile() {
        val fd = assets.openFd("1.apk")
        val fileUpload = FileUploadRequest("2.apk", fd.length, fd.createInputStream())
        val builder =    AlertDialog.Builder(this@MainActivity)
        val alertDialog =   builder.create()
        fileUpload.onUpload { uploadSize, totalSize ->
            MainScope().launch {
                alertDialog.setMessage("uploadSize:$uploadSize,totalSize:$totalSize")
                if(!alertDialog.isShowing) alertDialog.show()
            }

        }
        fileUpload.callBack = object : FileUploadRequest.CallBack {
            override fun onSuccess(request: FileUploadRequest, res: Any?) {
                MainScope().launch {
                    alertDialog.dismiss()
                    AlertDialog.Builder(this@MainActivity).setMessage("uploadSuccess").show()
                }
            }

            override fun onFailed(code: Int, message: String) {
                MainScope().launch {
                    alertDialog.dismiss()
                    AlertDialog.Builder(this@MainActivity).setMessage("failed").show()
                }
            }
        }

        HttpClientProxy.upload(fileUpload)
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
                    GlobalScope.launch(Dispatchers.Main) {
                        AlertDialog.Builder(this@MainActivity).setMessage("faild code$code message $message").show()
                    }
                }
            }
        }
        HttpClientProxy.send(request)
    }
}