package com.benyq.guochat.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author benyq
 * @time 2020/6/22
 * @e-mail 1520063035@qq.com
 * @note
 */
@Parcelize
data class RegisterBean(val userName: String, val pwd: String): Parcelable