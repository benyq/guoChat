package com.benyq.guochat.media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benyq.module_base.ext.loge

/**
 * @author benyq
 * @time 2020/5/5
 * @e-mail 1520063035@qq.com
 * @note 通知栏处理Activity
 */
class NotificationHandleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        val id = intent.getIntExtra("ids", 0)
        loge("NotificationHandleActivity id $id")
    }
}
