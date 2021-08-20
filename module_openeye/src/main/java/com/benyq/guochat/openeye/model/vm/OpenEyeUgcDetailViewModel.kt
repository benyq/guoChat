package com.benyq.guochat.openeye.model.vm

import com.benyq.guochat.openeye.model.bean.CommunityRecommend
import com.benyq.guochat.openeye.model.repository.OpenEyeRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/9/7
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class OpenEyeUgcDetailViewModel @Inject constructor(private val mRepository: OpenEyeRepository) :
    BaseViewModel() {

    var dataList: List<CommunityRecommend.Item>? = null

    var itemPosition: Int = -1

}