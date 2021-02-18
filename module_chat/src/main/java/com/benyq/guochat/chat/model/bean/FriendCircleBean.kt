package com.benyq.guochat.chat.model.bean

import com.benyq.guochat.chat.app.CIRCLE__TYPE_TEXT

/**
 * @author benyq
 * @time 2020/7/11
 * @e-mail 1520063035@qq.com
 * @note
 */
data class FriendCircleBean(
    val circleId: String, val publishName: String, val publishUid: String,val publishAvatar: String,
    val circleType: Int = CIRCLE__TYPE_TEXT, val content: String?, val imgList: List<String>?,
    val videoUrl: String?, val publishTime: String, var like: Boolean = false, var likeList: List<String>?,
    var commentsList: MutableList<CircleComment>?
)

data class CircleComment(val commentId: String,
                         val content: String,
                         val publishId: String ,
                         val publishName: String,
                         /**
                          * 回复的评论
                          */
                         val applyId: String?,
                         /**
                          * 回复评论的联系人的名字
                          */
                         val applyContractName: String?,
                         val applyContractId: String?
)