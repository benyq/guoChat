package com.benyq.guochat.model.rep

import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.guochat.model.bean.openeye.CommunityRecommend
import com.benyq.guochat.model.net.OpenEyeService
import com.benyq.mvvm.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note open eye 公用 repository
 */
class OpenEyeRepository @Inject constructor(private val apiService: OpenEyeService): BaseRepository(){

    suspend fun refreshCommunityRecommend(url : String) : ChatResponse<CommunityRecommend> {
        return launchIO {
            ChatResponse.success(apiService.getCommunityRecommend(url))
        }
    }

    suspend fun getHotSearch(): ChatResponse<List<String>> {
        return launchIO {
            ChatResponse.success(apiService.getHotSearch())
        }
    }
}