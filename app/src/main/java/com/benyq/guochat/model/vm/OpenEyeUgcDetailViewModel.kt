package com.benyq.guochat.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.benyq.guochat.model.bean.openeye.CommunityRecommend
import com.benyq.guochat.model.rep.OpenEyeRepository
import com.benyq.module_base.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/9/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class OpenEyeUgcDetailViewModel @ViewModelInject constructor(private val mRepository: OpenEyeRepository) : BaseViewModel(){

    var dataList: List<CommunityRecommend.Item>? = null

    var itemPosition: Int = -1

}