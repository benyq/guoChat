package com.benyq.imageviewer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.benyq.imageviewer.fragment.ImageFragment
import com.benyq.imageviewer.fragment.VideoFragment

internal class PreviewPageAdapter(fg: FragmentManager,
                         lifecycle: Lifecycle,
                         private val data: List<PreviewPhoto>?,) : FragmentStateAdapter(fg, lifecycle) {

    override fun getItemCount() = data?.size ?: 0

    override fun createFragment(position: Int): Fragment {
        return when(data?.get(position)?.type) {
            PreviewTypeEnum.IMAGE -> ImageFragment().setFragmentArguments(data[position], position)
            PreviewTypeEnum.LARGE_IMAGE -> ImageFragment().setFragmentArguments(data[position], position)
            PreviewTypeEnum.VIDEO -> VideoFragment().setFragmentArguments(data[position], position)
            else -> Fragment()
        }
    }
}