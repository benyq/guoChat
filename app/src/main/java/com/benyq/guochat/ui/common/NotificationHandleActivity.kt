package com.benyq.guochat.ui.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benyq.guochat.R
import com.benyq.mvvm.ext.loge

/**
 * @author benyq
 * @time 2020/5/5
 * @e-mail 1520063035@qq.com
 * @note 通知栏处理Activity
 */
class NotificationHandleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
        val id = intent.getIntExtra("ids", 0)
        loge("NotificationHandleActivity id $id")
    }
}
