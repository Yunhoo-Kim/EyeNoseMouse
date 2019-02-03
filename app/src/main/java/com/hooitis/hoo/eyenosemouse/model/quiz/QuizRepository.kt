package com.hooitis.hoo.eyenosemouse.model.quiz

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.hooitis.hoo.eyenosemouse.model.SharedPreferenceHelper
import com.hooitis.hoo.eyenosemouse.utils.FIREBASE_DB_URL
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Suppress("unused")
class QuizRepository @Inject constructor(private val quizDao: QuizDao,
                                         private val firebaseStore: FirebaseFirestore,
                                         private val sharedPreferenceHelper: SharedPreferenceHelper){


    val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    init {
    }

    fun loadQuizFromStore(): Observable<List<Quiz>> = Observable.create { emitter ->
        firebaseStore.collection(FIREBASE_DB_URL).get().addOnCompleteListener { res ->
            if (res.isSuccessful) {
                for (doc in res.result!!) {
                    quizDao.insert(Quiz(
                        id = doc.data["id"].toString().toLong(),
                        answer = doc.data["answer"].toString(),
                        leftEye = doc.data["leftEye"].toString(),
                        rightEye = doc.data["rightEye"].toString(),
                        nose = doc.data["nose"].toString(),
                        mouse = doc.data["mouse"].toString(),
                        imageUrl = doc.data["imageUrl"].toString(),
                        sex = doc.data["sex"].toString().toInt()
                    ))
                }
                emitter.onNext(quizDao.get())
            }else{
                Log.d("LoadQuiz", "error")
            }
        }.addOnFailureListener {err ->
            Log.d("error", err.toString())
        }
    }

    private fun loadQuiz(): Observable<List<Quiz>>{
        return Observable.fromCallable { quizDao.get() }
            .concatMap {
                if(it.isEmpty())
                    loadQuizFromStore()
                else {
                    Observable.just(it)
                }
            }
    }

    fun saveQuizzes(quizzes : List<Quiz>): Observable<List<Quiz>> {
        for(q in quizzes)
            quizDao.insert(q)
        return Observable.just(quizzes)
    }

    fun saveQuiz(quiz: Quiz)  = quizDao.insert(quiz)

    private fun initQuizzes(){
//        mCompositeDisposable.dispose()
//        mCompositeDisposable.add(loadFoods()
//                .subscribe({
//                }, {
//                }, {
//                }))
    }

    fun getQuizzes(): Observable<List<Quiz>> = Observable.just(quizDao.get())
}
