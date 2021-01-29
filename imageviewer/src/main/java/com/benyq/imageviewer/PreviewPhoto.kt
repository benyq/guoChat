package com.benyq.imageviewer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreviewPhoto(val url: String, val type: PreviewTypeEnum) : Parcelable