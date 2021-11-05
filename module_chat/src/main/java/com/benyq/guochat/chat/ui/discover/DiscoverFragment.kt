package com.benyq.guochat.chat.ui.discover

import android.Manifest
import com.alibaba.android.arouter.launcher.ARouter
import com.benyq.guochat.chat.databinding.FragmentDiscoverBinding
import com.benyq.guochat.chat.ui.scan.BarcodeScanningActivity
import com.benyq.module_base.RouterPath
import com.benyq.module_base.ui.base.BaseFragment
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ui.PicturePuzzleConfirmDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
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
            XXPermissions.with(requireActivity())
                .permission(Manifest.permission.CAMERA)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        goToActivity<BarcodeScanningActivity>()
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        Toasts.show("权限拒绝")
                    }
                })
        }
        binding.ifOpenEye.setOnClickListener {
            ARouter.getInstance().build(RouterPath.OPEN_EYE_COMMUNITY).navigation()
        }

        binding.ifComic.setOnClickListener {
            PicturePuzzleConfirmDialog.newInstance("https://www.bing.com/th?id=OIP.kN6_heZkbrUFPDoaUUeaGQHaE-&w=137&h=94&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2") {
                ARouter.getInstance().build(RouterPath.COMIC_HOME).navigation()
            }.show(childFragmentManager)
        }
        binding.ifWanAndroid.setOnClickListener {
            ARouter.getInstance().build(RouterPath.WAN_ANDROID).navigation()
        }
    }
}
