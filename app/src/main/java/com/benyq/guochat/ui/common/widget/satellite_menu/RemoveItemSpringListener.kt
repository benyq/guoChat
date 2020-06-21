package com.benyq.guochat.ui.common.widget.satellite_menu

import android.view.View
import android.view.ViewGroup
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringListener

/**
 * @author benyq
 * @time 2020/6/15
 * @e-mail 1520063035@qq.com
 * @note
 */
class RemoveItemSpringListener(private val group: ViewGroup, private val item : View) : SpringListener{
    override fun onSpringUpdate(spring: Spring?) {
    }

    override fun onSpringEndStateChange(spring: Spring?) {
    }

    override fun onSpringAtRest(spring: Spring?) {
        spring?.removeAllListeners()
        spring?.destroy()
//        group.removeView(item)
    }

    override fun onSpringActivate(spring: Spring?) {
        if (group.indexOfChild(item) == -1){
//            group.addView(item)
        }
    }
}