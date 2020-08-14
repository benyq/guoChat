package com.benyq.guochat

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/8/13
 * @e-mail 1520063035@qq.com
 * @note
 */
@ActivityScoped
data class TestEntity(val name: String, val age: Int) {
    @Inject constructor(): this("benyq", 23)
}