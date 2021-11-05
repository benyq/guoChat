package com.benyq.guochat.chat.model.rep

import com.benyq.guochat.chat.app.baseUrl
import com.benyq.guochat.chat.app.chatIdPrefix
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.model.bean.ContractSectionBean
import com.benyq.guochat.chat.model.net.ChatApiService
import com.benyq.guochat.database.entity.chat.ContractEntity
import com.benyq.module_base.mvvm.BaseRepository
import com.github.promeg.pinyinhelper.Pinyin
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
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
                val conversation = ChatObjectBox.getConversationId(id, it.contractId)
                charMap[pinyin] = charMap[pinyin]?.apply {
                    add(ContractSectionBean(it, conversation, headText = pinyin))
                } ?: mutableListOf(ContractSectionBean(it, conversation, headText = pinyin))
            }
            charMap.forEach {
                it.value.sortBy { bean ->
                    bean.headText
                }
            }
            ChatResponse.success(charMap.flatMap {
                it.value.apply {
                    add(0, ContractSectionBean(headText = it.key, header = true))
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

    suspend fun searchContractByCode(uid: String, code: String): ChatResponse<ContractBean> {
        return launchIO {
            val id = code.substringAfter(chatIdPrefix)
            val contract = ChatObjectBox.searchContractById(uid, id)
            if (contract == null) {
                apiService.searchContractByCode(code)
            }else {
                ChatResponse.success(ContractBean(contract.id, contract.ownUserId, contract.contractId, contract.chatNo, contract.nick, contract.remark, contract.gender, contract.avatarUrl))
            }

        }
    }

    suspend fun getApplyContractRecord(): ChatResponse<List<ContractBean>> {
        return apiService.getApplyContractRecord()
    }

    suspend fun agreeContract(id: String, applyId: String, uid: String): ChatResponse<String> {
        val response = apiService.agreeContract(id)
        if (response.isSuccess()) {
            val contractRes = apiService.searchContractByCode("chat-$applyId")
            if (contractRes.isSuccess()) {
                contractRes.data?.let { bean->
                    val box: Box<ContractEntity> = ChatObjectBox.boxStore.boxFor()
                    val contractEntity = ContractEntity(
                        contractId = bean.contractId,
                        ownUserId = uid,
                        avatarUrl = baseUrl + bean.avatar,
                        nick = bean.nick,
                        remark = bean.remark,
                        gender = bean.gender,
                        chatNo = bean.chatNo
                    )
                    box.put(contractEntity)
                }
            }
        }
        return response
    }

    suspend fun refuseContract(id: String): ChatResponse<String> {
        return apiService.refuseContract(id)
    }
}