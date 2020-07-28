package com.benyq.guochat.ui.discover

import android.Manifest
import com.benyq.guochat.R
import com.benyq.guochat.function.permissionX.PermissionX
import com.benyq.guochat.function.zxing.android.CaptureActivity
import com.benyq.guochat.ui.base.BaseFragment
import com.benyq.mvvm.ext.goToActivity
import com.benyq.mvvm.ext.toast
import kotlinx.android.synthetic.main.fragment_discover.*

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class DiscoverFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_discover

    override fun initListener() {
        ifFriendCircle.setOnClickListener {
            goToActivity<FriendCircleActivity>()
//            NotificationHelper.showMessageNotification(mContext)
        }
        ifScan.setOnClickListener {
            PermissionX.request(
                requireContext(),
                Manifest.permission.CAMERA
            ) { allGranted, _ ->
                if (allGranted) {
                    goToActivity<CaptureActivity>()
                } else {
                    toast("权限拒绝")
                }
            }
        }
    }
}
