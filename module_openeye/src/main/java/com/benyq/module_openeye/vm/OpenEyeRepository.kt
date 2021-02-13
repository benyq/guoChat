package com.benyq.module_openeye.vm

import com.benyq.module_base.mvvm.BaseRepository
import com.benyq.module_openeye.ComicResponse
import com.benyq.module_openeye.OpenEyeService
import com.benyq.module_openeye.bean.CommunityRecommend
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note open eye 公用 repository
 */
class OpenEyeRepository @Inject constructor(private val apiService: OpenEyeService): BaseRepository(){

    suspend fun refreshCommunityRecommend(url : String) : ComicResponse<CommunityRecommend> {
        return launchIO {
            ComicResponse.success(apiService.getCommunityRecommend(url))
        }
    }

    suspend fun getHotSearch(): ComicResponse<List<String>> {
        return launchIO {
            ComicResponse.success(apiService.getHotSearch())
        }
    }
}