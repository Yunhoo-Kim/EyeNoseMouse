package com.hooitis.hoo.eyenosemouse.vm

import android.arch.lifecycle.MutableLiveData
import com.hooitis.hoo.eyenosemouse.base.BaseViewModel
import com.hooitis.hoo.eyenosemouse.model.quiz.Quiz


@Suppress("unused")
class QuizItemVM: BaseViewModel() {
    val answer: MutableLiveData<String> = MutableLiveData()
    val nose: MutableLiveData<String> = MutableLiveData()
    val leftEye: MutableLiveData<String> = MutableLiveData()
    val rightEye: MutableLiveData<String> = MutableLiveData()
    val mouse: MutableLiveData<String> = MutableLiveData()
    val imageUrl: MutableLiveData<String> = MutableLiveData()

    fun bind(quiz: Quiz){
        answer.value = quiz.answer
        nose.value = quiz.nose
        leftEye.value = quiz.leftEye
        rightEye.value = quiz.rightEye
        mouse.value = quiz.mouse
        imageUrl.value = quiz.imageUrl
    }
}