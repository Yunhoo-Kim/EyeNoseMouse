package com.hooitis.hoo.eyenosemouse.vm

import android.arch.lifecycle.MutableLiveData
import com.hooitis.hoo.eyenosemouse.base.BaseViewModel
import com.hooitis.hoo.eyenosemouse.model.eyenosemouse.VersionsRepository
import javax.inject.Inject


@Suppress("unused")
class VersionVM @Inject constructor(
        val versionsRepository: VersionsRepository
): BaseViewModel() {

    val beforeQuizText: MutableLiveData<String> = MutableLiveData()

    init {
//        checkVersion()
    }

    fun checkVersion() = versionsRepository.checkVersion()
}