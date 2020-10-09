package com.benyq.guochat.comic.ui.shelf

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.vm.ComicShelfViewModel
import com.benyq.mvvm.ext.getViewModel
import com.benyq.mvvm.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.comic_fragment_comic_shelf.*

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class ComicShelfFragment : LifecycleFragment<ComicShelfViewModel>(){



    override fun getLayoutId() = R.layout.comic_fragment_comic_shelf

    override fun initVM(): ComicShelfViewModel = getViewModel()

    override fun initView() {
        super.initView()
        cbSortByUpdate.setOnCheckedChangeListener { _, checked ->
            if (checked){
                //按更新时间排序
            }else {
                //阅读顺序
            }
        }
        ivArrange.setOnClickListener {

        }

    }

    override fun dataObserver() {

    }
}