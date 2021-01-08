package com.benyq.guochat.comic.ui.search

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benyq.guochat.comic.ComicIntentExtra
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.vm.ComicSearchBookViewModel
import com.benyq.guochat.comic.ui.detail.BookDetailActivity
import com.benyq.mvvm.ext.*
import com.benyq.mvvm.ui.base.BaseActivity
import com.benyq.mvvm.ui.base.LifecycleFragment
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.gyf.immersionbar.ktx.immersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.comic_fragment_search_book.*

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class ComicSearchBookFragment : LifecycleFragment<ComicSearchBookViewModel>() {

    private val mHotKeyAdapter = ComicSearchHotKeyAdapter()
    private val mSearchHistoryAdapter = ComicSearchHistoryAdapter()
    private val mSearchResultAdapter = ComicSearchResultAdapter()

    override fun initVM(): ComicSearchBookViewModel = getViewModel()

    override fun getLayoutId() = R.layout.comic_fragment_search_book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersionBar {
            statusBarColor(R.color.darkgrey)
            statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        }
    }

    override fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                removeFragment(requireActivity(), this@ComicSearchBookFragment)
            }
        })

        tvCancel.setOnClickListener {
            removeFragment(requireActivity(), this)
        }

        rvHotKey.layoutManager = FlexboxLayoutManager(context).apply {
            flexWrap = FlexWrap.WRAP
        }
        rvHotKey.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val space = requireContext().dip2px(5f).toInt()
                outRect.set(space, space, space, space)
            }
        })
        rvHotKey.adapter = mHotKeyAdapter
        mHotKeyAdapter.setOnItemClickListener { adapter, view, position ->
            val comic = mHotKeyAdapter.data[position]
            mViewModel.addSearchHistory(comic.comicId, comic.name)
            goToActivity<BookDetailActivity>(ComicIntentExtra.comicId to comic.comicId)
        }

        rvSearchHistory.layoutManager = LinearLayoutManager(mContext)
        rvSearchHistory.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        rvSearchHistory.adapter = mSearchHistoryAdapter
        mSearchHistoryAdapter.setOnItemChildClickListener { adapter, view, position ->
            val comic = mSearchHistoryAdapter.data[position]
            mViewModel.deleteHistoryRecord(comic.id, position)
        }
        mSearchHistoryAdapter.setOnItemClickListener { adapter, view, position ->
            val comic = mSearchHistoryAdapter.data[position]
            mViewModel.addSearchHistory(comic.comicId, comic.name)
            goToActivity<BookDetailActivity>(ComicIntentExtra.comicId to comic.comicId)
        }
        rvSearchResult.layoutManager = LinearLayoutManager(mContext)
        rvSearchResult.adapter = mSearchResultAdapter
        rvSearchResult.addItemDecoration(object: RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = mContext.dip2px(10).toInt()
            }
        })
        mSearchResultAdapter.loadMoreModule.setOnLoadMoreListener {
            mViewModel.comicSearchMore()
        }

        etQuery.setOnEditorActionListener(object: TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etQuery.text.toString().isEmpty()) {
                        Toasts.show(R.string.comic_input_keywords_tips)
                        return false
                    }
                    showSearchResult()
                    mViewModel.comicSearch(etQuery.textTrim())
                    return true
                }
                return true
            }
        })
        etQuery.addTextChangedListener {
            if (it?.toString().isNullOrEmpty()) {
                hideSearchResult()
            }
        }
    }

    override fun dataObserver() {
        with(mViewModel) {
            searchHotKey.observe(viewLifecycleOwner) {
                etQuery.hint = it.defaultSearch
                mHotKeyAdapter.setList(it.hotItems)
            }

            searchHistory.observe(viewLifecycleOwner) {
                mSearchHistoryAdapter.setList(it)
            }

            deleteHistoryResult.observe(viewLifecycleOwner){
                mSearchHistoryAdapter.removeAt(it)
            }

            comicSearchResult.observe(viewLifecycleOwner) {
                it.isSuccess?.let { result->
                    tvSearchResult.text = String.format(getString(R.string.comic_search_info_tip), etQuery.textTrim(), result.comicNum)
                    if (mSearchResultAdapter.data.isEmpty()) {
                        mSearchResultAdapter.setList(result.comics)
                    }else {
                        mSearchResultAdapter.addData(result.comics)
                    }
                    if (result.hasMore) {
                        mSearchResultAdapter.loadMoreModule.loadMoreComplete()
                    }else {
                        mSearchResultAdapter.loadMoreModule.loadMoreEnd()
                    }
                }

                it?.isError?.run {
                    mSearchResultAdapter.loadMoreModule.loadMoreFail()
                }
            }
        }
    }

    override fun initData() {
        mViewModel.comicSearchHot()
        mViewModel.getSearchHistory()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(activity, R.anim.anl_push_up_in)
        } else {
            AnimationUtils.loadAnimation(activity, R.anim.anl_push_top_out)
        }
    }

    /**
     * 先移除Fragment，并将Fragment从堆栈弹出。
     */
    private fun removeFragment(activity: Activity, fragment: Fragment) {
        runOnUiThread(200) {
            (activity as BaseActivity).initImmersionBar()
        }
        (activity as FragmentActivity).supportFragmentManager.run {
            beginTransaction().remove(fragment).commitAllowingStateLoss()
            popBackStack()
        }
    }

    private fun showSearchResult(){
        llSearchResult.visible()
        mSearchResultAdapter.setList(null)
        nsSearch.gone()
    }

    private fun hideSearchResult() {
        llSearchResult.gone()
        nsSearch.visible()
    }

    companion object {
        /**
         * 切换Fragment，会加入回退栈。
         */
        fun switchFragment(activity: Activity) {
            (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, ComicSearchBookFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }
}