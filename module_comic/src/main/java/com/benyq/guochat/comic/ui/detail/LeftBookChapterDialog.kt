package com.benyq.guochat.comic.ui.detail

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.databinding.ComicDialogLeftChapterBinding
import com.benyq.module_base.ext.changeSize
import com.benyq.module_base.ui.base.BaseDialogFragment

/**
 * @author benyq
 * @time 2020/10/13
 * @e-mail 1520063035@qq.com
 * @note  阅读界面左边目录
 */
class LeftBookChapterDialog : BaseDialogFragment<ComicDialogLeftChapterBinding>() {

    companion object {
        fun newInstance() : LeftBookChapterDialog{
            return LeftBookChapterDialog()
        }
    }

    private val mAdapter = LeftBookChapterAdapter()

    override fun provideViewBinding() = ComicDialogLeftChapterBinding.inflate(layoutInflater)

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