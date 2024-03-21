package com.tonysheng.request

import kotlinx.serialization.Serializable

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2024/3/20
 * @Time: 20:49
 * @version: V1.0
 */
@Serializable
data class PmRSp(val resultCode: Int, val result: String = "")