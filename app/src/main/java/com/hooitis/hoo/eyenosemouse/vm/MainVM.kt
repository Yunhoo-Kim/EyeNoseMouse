package com.hooitis.hoo.eyenosemouse.vm

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.util.Log
import com.hooitis.hoo.eyenosemouse.base.BaseViewModel
import com.hooitis.hoo.eyenosemouse.model.SharedPreferenceHelper
import com.hooitis.hoo.eyenosemouse.model.quiz.Quiz
import com.hooitis.hoo.eyenosemouse.model.quiz.QuizRepository
import com.hooitis.hoo.eyenosemouse.ui.ImageDragListAdapter
import com.hooitis.hoo.eyenosemouse.ui.QuizAnswerListAdapter
import com.hooitis.hoo.eyenosemouse.ui.QuizListAdapter
import javax.inject.Inject


@Suppress("unused")
class MainVM @Inject constructor(
        private val quizRepository: QuizRepository,
        val sharedPreferenceHelper: SharedPreferenceHelper
): BaseViewModel() {

    private var index = 0
    private var touchable = true
    private lateinit var quizList: List<Quiz>



    val wrong: MutableLiveData<Boolean> = MutableLiveData()
    val animation: MutableLiveData<Boolean> = MutableLiveData()
    val quizIndex: MutableLiveData<Int> = MutableLiveData()
    val quizImage: MutableLiveData<String> = MutableLiveData()


    val imageListAdapter: ImageDragListAdapter by lazy {
        ImageDragListAdapter()
    }

    val quizListAdapter: QuizListAdapter by lazy {
        QuizListAdapter()
    }

    val quizAnswerListAdapter: QuizAnswerListAdapter by lazy {
        QuizAnswerListAdapter(this)
    }

    private val mDelayHandler: Handler by lazy {
        Handler()
    }



    private val mShowNext: Runnable = Runnable {
        if(wrong.value!!)
            return@Runnable

        animation.postValue(!(animation.value!!))
        quizIndex.postValue(index + 1)
        index++
        touchable = true
        setAnswerList()
    }

    init {
        wrong.value = false
        index = 0
    }


    fun loadWomenQuizData(){
        quizList = quizRepository.getQuizzes().blockingFirst().shuffled()
        quizListAdapter.updateQuizList(quizList)
        quizIndex.postValue(0)
        quizImage.postValue(quizList[0].imageUrl)
        setAnswerList()
    }

    fun loadManQuizData(){
        quizList = quizRepository.getQuizzes().blockingFirst().shuffled()
        quizListAdapter.updateQuizList(quizList)
        quizIndex.postValue(0)
        setAnswerList()
    }

    private fun setAnswerList(){
        val answerList = quizList.filterIndexed { index, _ ->
           index != this.index
        }.shuffled().subList(0, 2).toMutableList()

        quizImage.postValue(quizList[index].imageUrl)
        answerList.add(quizList[index])
        answerList.shuffle()
        quizAnswerListAdapter.updateResultList(answerList.toList())
    }



    fun showNextQuiz(){
        mDelayHandler.postDelayed(mShowNext, 1700)
    }


    fun checkResult(result: String): Boolean{
        if(!touchable)
            return false

        touchable = false
        val quiz = quizList[index]
        if (quiz.answer != result){
            wrong.value = true
            return false
        }

        return true
    }
}