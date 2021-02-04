package com.benyq.imageviewer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.benyq.imageviewer.*
import com.benyq.imageviewer.anim.AnimBgHelper
import com.benyq.imageviewer.anim.AnimHelper
import com.benyq.mvvm.ext.loge

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 预览基类
 */

abstract class BasePreviewFragment : Fragment(), OnAnimatorListener {

    companion object {
        const val KEY_FRAGMENT_PARAMS = "com.benyq.imageviewer.fragment.param"
        const val KEY_FRAGMENT_PARAMS_POS = "com.benyq.imageviewer.fragment.position"
    }

    lateinit var mData: PreviewPhoto
    var mPosition: Int = -1
    lateinit var fragmentView: View

    protected val mViewModel by lazy { ViewModelProvider(requireActivity()).get(PreviewViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mData =
            arguments?.getParcelable(KEY_FRAGMENT_PARAMS) ?: PreviewPhoto("", PreviewTypeEnum.IMAGE)
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

        mViewModel.exitAnimPosition.observe(viewLifecycleOwner) { position ->
            if (position == mPosition) {
                if (mViewModel.isExiting) {
                    return@observe
                }
                beforeExitAnim()
                AnimHelper.startExitAnim(viewLifecycleOwner, fragmentView,
                    view.findViewById(getFullViewId()),
                    Components.getView(mPosition),
                    300, 1f,
                    this)
            }
        }

        beforeEnterAnim(view.findViewById(getFullViewId()), Components.getView(mPosition))
        if (Components.isLoad) {
            //已经加载过了
            AnimBgHelper.onDrag(fragmentView, 1f)
            AnimHelper.startEnterAnim(
                fragmentView,
                view.findViewById(getFullViewId()),
                Components.getView(mPosition),
                0,
                this
            )

            return
        }

        AnimHelper.startEnterAnim(
            fragmentView,
            view.findViewById(getFullViewId()),
            Components.getView(mPosition),
            300,
            this
        )
        Components.isLoad = true
    }

    override fun onExitAnimEnd() {
        loge("onExitAnimEnd $this")
        requireActivity().finish()
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

    abstract fun beforeEnterAnim(fullView: View, thumbnailView: View?)
}