package com.benyq.guochat.comic.ui.detail

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benyq.guochat.comic.ComicIntentExtra
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.databinding.ComicActivityReadComicBookBinding
import com.benyq.guochat.comic.local.ComicLocalStorage
import com.benyq.guochat.comic.model.bean.Chapter
import com.benyq.guochat.comic.model.vm.ReadComicBookViewModel
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.base.LifecycleActivity
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/10/6
 * @e-mail 1520063035@qq.com
 * @note 阅读界面
 */
@AndroidEntryPoint
class ReadComicBookActivity : LifecycleActivity<ReadComicBookViewModel, ComicActivityReadComicBookBinding>() {

    private var isPreviewHorizontal: Boolean = ComicLocalStorage.isPreviewHorizontalModel

    private lateinit var mChapterId: String
    private lateinit var mComicId: String
    private lateinit var mChapterList: List<Chapter>
    private val mAdapter = BookPreviewImageAdapter()
    private var mCurrentPosition = 0

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

    override fun provideViewBinding() = ComicActivityReadComicBookBinding.inflate(layoutInflater)

    override fun isSupportSwipeBack() = true

    override fun initVM(): ReadComicBookViewModel = getViewModel()

    override fun initView() {
        mComicId = intent.getStringExtra(ComicIntentExtra.comicId) ?: ""
        mCurrentPosition = intent.getIntExtra(ComicIntentExtra.chapterPosition, -1)

        binding.headView.setBackAction { finish() }

        //默认竖直方向
        val orientation = if (isPreviewHorizontal) {
            binding.tvSwitchModel.text = getString(R.string.comic_switch_model_horizontal)
            RecyclerView.HORIZONTAL
        }else {
            binding.tvSwitchModel.text = getString(R.string.comic_switch_model_vertical)
            RecyclerView.VERTICAL
        }
        binding.rvBookContent.layoutManager = LinearLayoutManager(this, orientation, false)
        binding.rvBookContent.adapter = mAdapter
        binding.rvBookContent.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val findFirstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    mAdapter.getItem(findFirstVisibleItemPosition).also {
                        binding.sbPages.progress = it.currentIndex - 1
                    }
                }
            }
        })
        binding.rvBookContent.setTouchCallback {
            switchTAndBMenu()
        }

        binding.llChapter.setOnClickListener {
            LeftBookChapterDialog.newInstance().show(supportFragmentManager)
        }

        binding.llBrightness.setOnClickListener {

        }

        binding.llSwitchModel.setOnClickListener {
            isPreviewHorizontal = !isPreviewHorizontal
            ComicLocalStorage.isPreviewHorizontalModel = isPreviewHorizontal
            binding.tvSwitchModel
            if (isPreviewHorizontal) {
                (binding.rvBookContent.layoutManager as LinearLayoutManager).orientation = RecyclerView.HORIZONTAL
                binding.tvSwitchModel.text = getString(R.string.comic_switch_model_horizontal)
            }else {
                (binding.rvBookContent.layoutManager as LinearLayoutManager).orientation = RecyclerView.VERTICAL
                binding.tvSwitchModel.text = getString(R.string.comic_switch_model_vertical)
            }
            Toasts.show("有些漫画应该是不支持修改的，嘿嘿")
        }

        binding.tvLastChapter.setOnClickListener {
            if (mCurrentPosition == 0) {
                Toasts.show("已经是第一回了，别点了")
                return@setOnClickListener
            }
            mCurrentPosition --
            loadNewChapter()
        }

        binding.tvNextChapter.setOnClickListener {
            if (mCurrentPosition == mChapterList.size - 1) {
                Toasts.show("已经是最后一回了，有底线的")
                return@setOnClickListener
            }
            mCurrentPosition ++
            loadNewChapter()
        }
        binding.sbPages.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                binding.rvBookContent.scrollToPosition(binding.sbPages.progress)
            }

        })
        hideMenu()
        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun finish() {
        val backData = Intent()
        backData.putExtra(ComicIntentExtra.comicId, mComicId)
        backData.putExtra(ComicIntentExtra.chapterPosition, mCurrentPosition)
        setResult(Activity.RESULT_OK, backData)
        super.finish()
    }

    override fun dataObserver() {
        with(viewModelGet()) {
            loadingResult.observe(this@ReadComicBookActivity) {
                if (it) {
                    showLoading("")
                } else {
                    hideLoading()
                }
            }
            previewResult.observe(this@ReadComicBookActivity) {
                loge("dataObserver previewResult $it")
                it?.isSuccess?.run {
                    image_list.forEachIndexed { index, imageListBean ->
                        imageListBean.currentIndex = index + 1
                    }
                    mAdapter.setList(image_list)
                    binding.sbPages.max = image_list.size - 1
                    binding.sbPages.progress = 0
                }
                it?.isError?.message?.run {
                    Toasts.show(this)
                }
            }
            bookDetailResult.observe(this@ReadComicBookActivity) {
                it.isSuccess?.chapterList?.let { chapterList->
                    mChapterList = chapterList

                    if (mCurrentPosition != -1) {
                        binding.headView.setToolbarTitle(mChapterList[mCurrentPosition].name)
                        mChapterId = mChapterList[mCurrentPosition].chapter_id
                        viewModelGet().comicPreView(mChapterId)
                    }

                }
            }
        }
    }

    override fun initData() {
        viewModelGet().getComicDetail(mComicId)
        viewModelGet().updateBookShelf(mComicId, mCurrentPosition, 0)
    }

    private fun loadNewChapter() {
        binding.headView.setToolbarTitle( mChapterList[mCurrentPosition].name)
        mChapterId = mChapterList[mCurrentPosition].chapter_id
        viewModelGet().updateBookShelf(mComicId, mCurrentPosition, mChapterList.size)
        mAdapter.setList(null)
        viewModelGet().comicPreView(mChapterId)
    }

    private fun switchTAndBMenu() {
        if (binding.clBottom.translationY != 0f) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            binding.headView.animate().translationY(ImmersionBar.getStatusBarHeight(this).toFloat())
                .setDuration(300).start()
            binding.clBottom.animate().translationY(0f).setDuration(300).start()
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            binding.headView.animate().translationY(-binding.headView.height.toFloat()).setDuration(300).start()
            binding.clBottom.animate().translationY(binding.clBottom.height.toFloat()).setDuration(300).start()
        }
    }

    private fun hideMenu() {
        calculateViewMeasure(binding.headView)
        calculateViewMeasure(binding.clBottom)

        binding.headView.translationY = -binding.headView.measuredHeight.toFloat()
        binding.clBottom.translationY = binding.clBottom.measuredHeight.toFloat()

    }

    private fun calculateViewMeasure(view: View) {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        view.measure(w, h)
    }
}