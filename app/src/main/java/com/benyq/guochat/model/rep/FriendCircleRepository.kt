package com.benyq.guochat.model.rep

import com.benyq.guochat.app.CIRCLE__TYPE_TEXT
import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.guochat.model.bean.CircleComment
import com.benyq.guochat.model.bean.FriendCircleBean
import com.benyq.module_base.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/7/11
 * @e-mail 1520063035@qq.com
 * @note
 */
class FriendCircleRepository @Inject constructor() : BaseRepository() {

    suspend fun queryFriendCircles(): ChatResponse<List<FriendCircleBean>> {
        return launchIO {
            ChatResponse.success(
                listOf(
                    FriendCircleBean(
                        "2",
                        "李四",
                        "",
                        "https://tse1-mm.cn.bing.net/th?id=OIP.S_VYyBrGXZWoTqDajgEHjgHaHa&w=175&h=160&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                        CIRCLE__TYPE_TEXT,
                        "这是第二个朋友圈内容，嘿嘿",
                        listOf(
                            "https://tse1-mm.cn.bing.net/th?id=OIP.lsFnx8PbbOhcFyLs_qczqQHaGY&w=236&h=204&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                            "https://tse1-mm.cn.bing.net/th?id=OIP.cUs1IaUOoJ6UhggdvqzW6gHaER&w=160&h=100&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                            "https://tse1-mm.cn.bing.net/th?id=OIP.ZwatLnpo7avdvBtWRHB8TwHaE8&w=150&h=106&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2"
                        ),
                        "",
                        "",
                        false,
                        listOf(),
                        null
                    ),
                    FriendCircleBean(
                        "1",
                        "张三",
                        "",
                        "https://tse1-mm.cn.bing.net/th?id=OIP.FYHKQ4QDg7JI02RToq-CqgHaHM&w=180&h=160&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                        CIRCLE__TYPE_TEXT,
                        "这是第一个朋友圈内容，嘿嘿",
                        null,
                        "",
                        "",
                        false,
                        listOf("我","李四", "王五"),
                        mutableListOf(
                            CircleComment("1", "你在说你妈呢", "1", "张三", "", "", ""),
                            CircleComment("2", "说你呢傻逼", "2", "李四", "1", "张三", "1")
                        )
                    ),
                    FriendCircleBean(
                        "3",
                        "张三",
                        "",
                        "https://tse1-mm.cn.bing.net/th?id=OIP.FYHKQ4QDg7JI02RToq-CqgHaHM&w=180&h=160&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                        CIRCLE__TYPE_TEXT,
                        "这是第三个朋友圈内容，嘿嘿",
                        null,
                        "",
                        "",
                        false,
                        listOf(),
                        null
                    ),
                    FriendCircleBean(
                        "4",
                        "张三",
                        "",
                        "https://tse1-mm.cn.bing.net/th?id=OIP.FYHKQ4QDg7JI02RToq-CqgHaHM&w=180&h=160&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                        CIRCLE__TYPE_TEXT,
                        "这是第四个朋友圈内容，嘿嘿",
                        null,
                        "",
                        "",
                        false,
                        listOf(),
                        null
                    ),
                    FriendCircleBean(
                        "4",
                        "张三",
                        "",
                        "https://tse1-mm.cn.bing.net/th?id=OIP.FYHKQ4QDg7JI02RToq-CqgHaHM&w=180&h=160&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                        CIRCLE__TYPE_TEXT,
                        "这是第四个朋友圈内容，嘿嘿",
                        null,
                        "",
                        "",
                        false,
                        listOf(),
                        null
                    ),
                    FriendCircleBean(
                        "4",
                        "张三",
                        "",
                        "https://tse1-mm.cn.bing.net/th?id=OIP.FYHKQ4QDg7JI02RToq-CqgHaHM&w=180&h=160&c=8&rs=1&qlt=90&dpr=1.25&pid=3.1&rm=2",
                        CIRCLE__TYPE_TEXT,
                        "这是第四个朋友圈内容，嘿嘿",
                        null,
                        "",
                        "",
                        false,
                        listOf(),
                        null
                    )
                )
            )
        }
    }

    suspend fun friendCircleLike(like: Boolean): ChatResponse<Boolean> {
        return launchIO {
            ChatResponse.success(!like)
        }
    }
}