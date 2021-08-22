package com.benyq.guochat.wanandroid.model

import java.io.Serializable


data class UserData(val username: String, val id: Int, val score: Int, val level: Int)

data class LoginData(val username: String, val id: Int, val type: Int, val coinCount: Int): Serializable {
    companion object {
        fun empty(): LoginData {
            return LoginData("", 0, 0, 0)
        }
    }

    fun isEmpty(): Boolean {
        return username.isEmpty() || id <= 0
    }
}

data class PersonScoreData(val coinCount: Int, val rank: Int, val userId: Int, val username: String)

