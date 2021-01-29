package com.benyq.imageviewer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.benyq.imageviewer.Components
import com.benyq.imageviewer.PreviewPhoto
import com.benyq.imageviewer.PreviewTypeEnum
import com.benyq.imageviewer.anim.AnimBgHelper
import com.benyq.imageviewer.anim.AnimHelper

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 预览基类
 */

abstract class BasePreviewFragment : Fragment() {

    companion object {
        const val KEY_FRAGMENT_PARAMS = "com.benyq.imageviewer.fragment.param"
        const val KEY_FRAGMENT_PARAMS_POS = "com.benyq.imageviewer.fragment.position"
    }

    lateinit var mData : PreviewPhoto
    var mPosition : Int = -1
    lateinit var fragmentView: View
    lateinit var mFullView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mData = arguments?.getParcelable(KEY_FRAGMENT_PARAMS) ?: PreviewPhoto("", PreviewTypeEnum.IMAGE)
        mPosition = arguments?.getInt(KEY_FRAGMENT_PARAMS_POS) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(getLayoutId(), container, false)
        return fragmentView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFullView = view.findViewById(getFullViewId())

        AnimHelper.startEnterAnim(fragmentView, mFullView, Components.cacheView[mPosition], 200)
    }

    open fun setFragmentArguments(data: PreviewPhoto, position: Int): BasePreviewFragment {
        val bundle = Bundle()
        bundle.putInt(KEY_FRAGMENT_PARAMS_POS, position)
        bundle.putParcelable(KEY_FRAGMENT_PARAMS, data)
        arguments = bundle
        return this
    }

    abstract fun getLayoutId(): Int

    //完整的View的id
    abstract fun getFullViewId(): Int

}