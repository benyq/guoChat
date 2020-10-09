package com.benyq.guochat.comic.ui.detail

import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.comic.ComicIntentExtra
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.local.ComicLocalStorage
import com.benyq.guochat.comic.model.bean.Chapter
import com.benyq.guochat.comic.model.vm.ReadComicBookViewModel
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.getViewModel
import com.benyq.mvvm.ext.runOnUiThreadDelayed
import com.benyq.mvvm.ui.base.LifecycleActivity
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.comic_activity_read_comic_book.*

/**
 * @author benyq
 * @time 2020/10/6
 * @e-mail 1520063035@qq.com
 * @note 阅读界面
 */
@AndroidEntryPoint
class ReadComicBookActivity : LifecycleActivity<ReadComicBookViewModel>() {

    private var isPreviewHorizontal : Boolean = ComicLocalStorage.isPreviewHorizontalModel
    private lateinit var mChapterId: String
    private val mAdapter = BookPreviewImageAdapter()

    override fun initImmersionBar() {
        immersionBar {
            statusBarColor(R.color.darkgrey)
            statusBarDarkFont(true, 0.2f)
        }
    }

    override fun initWidows() {
        super.initWidows()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun getLayoutId() = R.layout.comic_activity_read_comic_book

    override fun initVM(): ReadComicBookViewModel = getViewModel()

    override fun initView() {
        isSupportSwipeBack = false
        val chapterPosition = intent.getIntExtra(ComicIntentExtra.chapterPosition, -1)
        val chapterList = intent.getParcelableArrayListExtra(ComicIntentExtra.chapterList) ?: emptyList<Chapter>()
        if (chapterPosition != -1 && chapterList.isNotEmpty()) {
            headView.setToolbarTitle(chapterList[chapterPosition].name)
            mChapterId = chapterList[chapterPosition].chapter_id
        }
        headView.setBackAction { finish() }

        //默认竖直方向
        rvBookContent.layoutManager = LinearLayoutManager(this)
        rvBookContent.adapter = mAdapter

        hideMenu()

        runOnUiThreadDelayed(3000) {
            switchTAndBMenu()
        }
        runOnUiThreadDelayed(6000) {
            switchTAndBMenu()
        }
        runOnUiThreadDelayed(9000) {
            switchTAndBMenu()
        }
    }

    override fun dataObserver() {
        with(viewModelGet()) {
            previewResult.observe(this@ReadComicBookActivity) {
                if (it.isLoading) {
                    showLoading("")
                }else {
                    hideLoading()
                }
                it?.isSuccess?.run {
                    mAdapter.setList(image_list)
                }
                it?.isError?.run {
                    Toasts.show(this)
                }
            }
        }
    }

    override fun initData() {
        viewModelGet().comicPreView(mChapterId)
    }

    private fun switchTAndBMenu() {
        if (clBottom.translationY != 0f) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            headView.animate().translationY(ImmersionBar.getStatusBarHeight(this).toFloat()).setDuration(300).start()
            clBottom.animate().translationY(0f).setDuration(300).start()
        }else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            headView.animate().translationY(-headView.height.toFloat()).setDuration(300).start()
            clBottom.animate().translationY(clBottom.height.toFloat()).setDuration(300).start()
        }
    }

    private fun hideMenu() {
        calculateViewMeasure(headView)
        calculateViewMeasure(clBottom)

        headView.translationY = -headView.measuredHeight.toFloat()
        clBottom.translationY = clBottom.measuredHeight.toFloat()

    }

    private fun calculateViewMeasure(view: View) {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        view.measure(w, h)
    }
}