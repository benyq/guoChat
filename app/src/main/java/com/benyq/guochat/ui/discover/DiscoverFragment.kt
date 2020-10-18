package com.benyq.guochat.ui.discover

import android.Manifest
import com.benyq.guochat.R
import com.benyq.guochat.comic.ComicActivity
import com.benyq.guochat.function.permissionX.PermissionX
import com.benyq.guochat.function.zxing.android.CaptureActivity
import com.benyq.mvvm.ui.base.BaseFragment
import com.benyq.guochat.ui.openeye.OpenEyeCommunityActivity
import com.benyq.mvvm.ext.goToActivity
import com.benyq.mvvm.ext.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_discover.*

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class DiscoverFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_discover

    override fun initListener() {
        ifFriendCircle.setOnClickListener {
            goToActivity<FriendCircleActivity>()
//            NotificationHelper.showMessageNotification(mContext)
        }
        ifScan.setOnClickListener {
            PermissionX.request(
                requireActivity(),
                Manifest.permission.CAMERA
            ) { allGranted, _ ->
                if (allGranted) {
                    goToActivity<CaptureActivity>()
                } else {
                    toast("权限拒绝")
                }
            }
        }
        ifOpenEye.setOnClickListener {
            goToActivity<OpenEyeCommunityActivity>()
        }

        ifComic.setOnClickListener {
            goToActivity<ComicActivity>()
        }
    }
}
