package com.benyq.guochat.model.rep

import com.benyq.guochat.local.ChatObjectBox
import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.guochat.model.bean.ContractSectionBean
import com.benyq.module_base.mvvm.BaseRepository
import com.github.promeg.pinyinhelper.Pinyin
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note
 */
class ContractsRepository @Inject constructor() : BaseRepository() {

    suspend fun getAllContracts(): ChatResponse<List<ContractSectionBean>> {
        return launchIO {
            val charMap = sortedMapOf<String, MutableList<ContractSectionBean>>()
            val contractEntityList = ChatObjectBox.getAllContracts()
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
}