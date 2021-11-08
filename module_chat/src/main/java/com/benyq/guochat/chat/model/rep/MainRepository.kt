package com.benyq.guochat.chat.model.rep

import com.benyq.guochat.chat.app.baseUrl
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.net.ChatApiService
import com.benyq.guochat.database.entity.chat.ContractEntity
import com.benyq.module_base.mvvm.BaseRepository
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class MainRepository @Inject constructor(private val apiService: ChatApiService) :
    BaseRepository() {


    suspend fun checkToken(): ChatResponse<Boolean> {
        return apiService.checkToken()
    }


    //刷新用户本地信息
    //目前是 联系人
    suspend fun refreshUserData(uid: String): ChatResponse<Boolean> {
        val response = apiService.getAllContracts()
        withContext(Dispatchers.IO) {
            if (response.isSuccess()) {
                response.getRealData()?.let { beans ->
                    val box: Box<ContractEntity> = ChatObjectBox.boxStore.boxFor()
                    val contracts = ChatObjectBox.getAllContracts(uid).toMutableList()
                    beans.forEach { bean ->
                        bean.avatar = baseUrl + bean.avatar
                        val contract = contracts.find { it.contractId == bean.contractId }
                        contract?.let {
                            contracts.remove(it)
                            contract.avatarUrl = bean.avatar
                            contract.nick = bean.nick
                            contract.remark = bean.remark
                            contract.gender = bean.gender
                            contract.chatNo = bean.chatNo
                            box.put(it)
                        } ?: run {
                            val contractEntity = ContractEntity(
                                contractId = bean.contractId,
                                ownUserId = uid,
                                avatarUrl = bean.avatar,
                                nick = bean.nick,
                                remark = bean.remark,
                                gender = bean.gender,
                                chatNo = bean.chatNo
                            )
                            box.put(contractEntity)
                        }
                    }
                    //删除联系人与关联的聊天记录
                    contracts.forEach {
                        ChatObjectBox.deleteChatRecordByContractId(it.id)
                    }
                }
            }
        }
        return ChatResponse.success(true)
    }

}