package com.benyq.mvvm

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.benyq.mvvm.ext.Log
import com.benyq.mvvm.ext.Toasts

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class ApplicationContextProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        context?.run {
            Toasts.init(this)
            Log.init()
        }
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri?  = null

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor?  = null



    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int  = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int  = 0

    override fun getType(uri: Uri): String? = null

}