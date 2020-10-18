package com.benyq.guochat.comic.ui.detail

import com.benyq.guochat.comic.R
import com.benyq.mvvm.ext.changeSize
import com.benyq.mvvm.ui.base.BaseDialogFragment

/**
 * @author benyq
 * @time 2020/10/13
 * @e-mail 1520063035@qq.com
 * @note  阅读界面左边目录
 */
class LeftBookChapterDialog : BaseDialogFragment() {

    companion object {
        fun newInstance() : LeftBookChapterDialog{
            return LeftBookChapterDialog()
        }
    }

    private val mAdapter = LeftBookChapterAdapter()

    override fun getLayoutId() = R.layout.comic_dialog_left_chapter

    override fun initView() {
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            mContext.changeSize(it, 0.3f, 1f)
            it.window?.let { win ->
                win.setDimAmount(0f)
                win.setWindowAnimations(R.style.comic_exist_chapter_menu_anim_style)
            }
        }
    }
}