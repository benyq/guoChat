package com.benyq.guochat.openeye.model.repository

import com.benyq.guochat.openeye.model.http.OpenEyeResponse
import com.benyq.guochat.openeye.model.http.OpenEyeService
import com.benyq.module_base.mvvm.BaseRepository
import com.benyq.guochat.openeye.bean.CommunityRecommend
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note open eye 公用 repository
 */
class OpenEyeRepository @Inject constructor(private val apiService: OpenEyeService): BaseRepository(){

    suspend fun refreshCommunityRecommend(url : String) : OpenEyeResponse<CommunityRecommend> {
        return launchIO {
            OpenEyeResponse.success(apiService.getCommunityRecommend(url))
        }
    }

    suspend fun getHotSearch(): OpenEyeResponse<List<String>> {
        return launchIO {
            OpenEyeResponse.success(apiService.getHotSearch())
        }
    }
}