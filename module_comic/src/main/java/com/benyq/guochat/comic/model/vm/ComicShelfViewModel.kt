package com.benyq.guochat.comic.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.comic.local.BookShelfTable
import com.benyq.guochat.comic.model.repository.ComicShelfRepository
import com.benyq.module_base.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicShelfViewModel @ViewModelInject constructor(private val repository: ComicShelfRepository): BaseViewModel(){

    val comicShelfBookResult = MutableLiveData<List<BookShelfTable>>()

    fun getShelfBook() {
        quickLaunch<List<BookShelfTable>> {

            onSuccess { comicShelfBookResult.value = it }

            request { repository.getShelfBook() }
        }
    }
}