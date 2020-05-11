package com.benyq.guochat.ui.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.benyq.mvvm.base.IFragment

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note 所有fragment的基类
 */
abstract class BaseFragment : Fragment(), IFragment{

    lateinit var mContext : Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

}