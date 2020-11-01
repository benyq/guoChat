package com.benyq.guochat.ui.discover

import android.Manifest
import android.content.Intent
import android.net.Uri
import com.benyq.guochat.R
import com.benyq.guochat.comic.ComicActivity
import com.benyq.guochat.function.permissionX.PermissionX
import com.benyq.guochat.function.zxing.android.CaptureActivity
import com.benyq.mvvm.ui.base.BaseFragment
import com.benyq.guochat.ui.openeye.OpenEyeCommunityActivity
import com.benyq.mvvm.ext.goToActivity
import com.benyq.mvvm.ext.toast
import com.benyq.mvvm.ui.PicturePuzzleConfirmDialog
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
            PicturePuzzleConfirmDialog.newInstance("https://www.bing.com/th?id=OIP.kN6_heZkbrUFPDoaUUeaGQHaE-&w=137&h=94&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2") {
                goToActivity<ComicActivity>()
            }.show(childFragmentManager)
        }
    }
}
