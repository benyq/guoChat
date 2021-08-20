package com.benyq.guochat.comic.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.comic.model.repository.ComicShelfRepository
import com.benyq.guochat.database.entity.comic.BookShelfTable
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class ComicShelfViewModel @Inject constructor(private val repository: ComicShelfRepository) :
    BaseViewModel() {

    val comicShelfBookResult = MutableLiveData<List<BookShelfTable>>()

    fun getShelfBook() {
        quickLaunch<List<BookShelfTable>> {

            onSuccess { comicShelfBookResult.value = it }

            request { repository.getShelfBook() }
        }
    }
}