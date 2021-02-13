package com.benyq.guochat.ui.discover

import android.Manifest
import com.benyq.guochat.R
import com.benyq.guochat.comic.ComicActivity
import com.benyq.guochat.databinding.FragmentDiscoverBinding
import com.benyq.guochat.function.permissionX.PermissionX
import com.benyq.guochat.function.zxing.android.CaptureActivity
import com.benyq.module_base.ui.base.BaseFragment
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ui.PicturePuzzleConfirmDialog
import com.benyq.module_openeye.OpenEyeCommunityActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class DiscoverFragment : BaseFragment<FragmentDiscoverBinding>() {

    override fun provideViewBinding() = FragmentDiscoverBinding.inflate(layoutInflater)

    override fun initListener() {
        binding.ifFriendCircle.setOnClickListener {
            goToActivity<FriendCircleActivity>()
//            NotificationHelper.showMessageNotification(mContext)
        }
        binding.ifScan.setOnClickListener {
            PermissionX.request(
                requireActivity(),
                Manifest.permission.CAMERA
            ) { allGranted, _ ->
                if (allGranted) {
                    goToActivity<CaptureActivity>()
                } else {
                    Toasts.show("权限拒绝")
                }
            }
        }
        binding.ifOpenEye.setOnClickListener {
            goToActivity<OpenEyeCommunityActivity>()
        }

        binding.ifComic.setOnClickListener {
            PicturePuzzleConfirmDialog.newInstance("https://www.bing.com/th?id=OIP.kN6_heZkbrUFPDoaUUeaGQHaE-&w=137&h=94&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2") {
                goToActivity<ComicActivity>()
            }.show(childFragmentManager)
        }
    }
}
