package com.benyq.guochat.chat.model.rep

import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.model.bean.ContractSectionBean
import com.benyq.guochat.chat.model.net.ChatApiService
import com.benyq.module_base.mvvm.BaseRepository
import com.github.promeg.pinyinhelper.Pinyin
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note
 */
class ContractRepository @Inject constructor(private val apiService: ChatApiService) : BaseRepository() {

    suspend fun getAllContracts(id: String): ChatResponse<List<ContractSectionBean>> {
        return launchIO {
            val charMap = sortedMapOf<String, MutableList<ContractSectionBean>>()
            val contractEntityList = ChatObjectBox.getAllContracts(id)
            contractEntityList.forEach {
                val pinyin = Pinyin.toPinyin(it.nick, "").first().toString()
                charMap[pinyin] = charMap[pinyin]?.apply {
                    add(ContractSectionBean(it, headText = pinyin))
                } ?: mutableListOf(ContractSectionBean(it, headText = pinyin))
            }
            charMap.forEach {
                it.value.sortBy { bean ->
                    bean.headText
                }
            }
            ChatResponse.success(charMap.flatMap {
                it.value.apply {
                    add(0, ContractSectionBean(null, headText = it.key, header = true))
                }
            })
        }
    }

    suspend fun searchContract(key: String): ChatResponse<ContractBean> {
        return apiService.searchContract(key)
    }


    suspend fun applyContract(uid: String): ChatResponse<String> {
        return apiService.applyContract(uid)
    }
}