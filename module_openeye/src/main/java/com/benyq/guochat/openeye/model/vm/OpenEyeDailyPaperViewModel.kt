package com.benyq.guochat.openeye.model.vm

import com.benyq.guochat.openeye.model.repository.OpenEyeRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class OpenEyeDailyPaperViewModel @Inject constructor(private val repository: OpenEyeRepository) :
    BaseViewModel() {
}