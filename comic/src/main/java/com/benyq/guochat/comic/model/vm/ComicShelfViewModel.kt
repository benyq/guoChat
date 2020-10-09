package com.benyq.guochat.comic.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.benyq.guochat.comic.model.repository.ComicShelfRepository
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicShelfViewModel @ViewModelInject constructor(private val repository: ComicShelfRepository): BaseViewModel(){
}