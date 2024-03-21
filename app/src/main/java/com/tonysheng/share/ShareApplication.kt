package com.tonysheng.share

import android.app.Application

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/20
 * @Time: 21:11
 * @version: V1.0
 */
class ShareApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        ContextHolder.context = this
    }
}