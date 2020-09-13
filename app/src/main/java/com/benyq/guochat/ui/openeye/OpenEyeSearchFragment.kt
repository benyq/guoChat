package com.benyq.guochat.ui.openeye

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.getViewModel
import com.benyq.guochat.model.vm.OpenEyeSearchViewModel
import com.benyq.guochat.ui.base.LifecycleFragment
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.hideKeyBoard
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_open_eye_search.*

/**
 * @author benyq
 * @time 2020/9/7
 * @e-mail 1520063035@qq.com
 * @note 搜索界面
 */
@AndroidEntryPoint
class OpenEyeSearchFragment : LifecycleFragment<OpenEyeSearchViewModel>() {

    private val mAdapter: HotSearchAdapter by lazy { HotSearchAdapter() }

    override fun initVM(): OpenEyeSearchViewModel = getViewModel()

    override fun getLayoutId() = R.layout.fragment_open_eye_search

    override fun dataObserver() {
        mViewModel.mHotKeywordsData.observe(viewLifecycleOwner, Observer {
            mAdapter.setList(it.toMutableList()?.apply { add(0, getString(R.string.hot_keywords)) })
        })
    }

    override fun initView() {
        super.initView()
        tvCancel.setOnClickListener {
            it.hideKeyBoard()
            removeFragment(requireActivity(), this)
        }

        etQuery.setOnEditorActionListener(object: TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etQuery.text.toString().isEmpty()) {
                        Toasts.show(R.string.input_keywords_tips)
                        return false
                    }
                    Toasts.show(R.string.currently_not_supported)
                    return true
                }
                return true
            }
        })

        rvSearchResult.layoutManager = LinearLayoutManager(mContext)
        rvSearchResult.adapter = mAdapter
    }

    override fun initData() {
        super.initData()
        mViewModel.getHotSearch()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(activity, R.anim.anl_push_up_in)
        } else {
            AnimationUtils.loadAnimation(activity, R.anim.anl_push_top_out)
        }
    }

    companion object {

        /**
         * 切换Fragment，会加入回退栈。
         */
        fun switchFragment(activity: Activity) {
            (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, OpenEyeSearchFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        /**
         * 先移除Fragment，并将Fragment从堆栈弹出。
         */
        fun removeFragment(activity: Activity, fragment: Fragment) {
            (activity as FragmentActivity).supportFragmentManager.run {
                beginTransaction().remove(fragment).commitAllowingStateLoss()
                popBackStack()
            }
        }
    }
}