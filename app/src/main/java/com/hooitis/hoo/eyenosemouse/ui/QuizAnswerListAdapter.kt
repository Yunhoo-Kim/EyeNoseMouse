package com.hooitis.hoo.eyenosemouse.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hooitis.hoo.eyenosemouse.R
import com.hooitis.hoo.eyenosemouse.databinding.ItemAnswerBinding
import com.hooitis.hoo.eyenosemouse.model.quiz.Quiz
import com.hooitis.hoo.eyenosemouse.vm.MainVM
import com.hooitis.hoo.eyenosemouse.vm.QuizItemVM


class QuizAnswerListAdapter constructor(private val mainVm: MainVM): RecyclerView.Adapter<QuizAnswerListAdapter.ViewHolder>() {
    private lateinit var quizList: List<Quiz>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAnswerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_answer,
                parent, false)
        return ViewHolder(binding, mainVm)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(quizList[position])
    }

    fun updateResultList(quizList: List<Quiz>){
        this.quizList = quizList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if(::quizList.isInitialized) quizList.size else 0

    class ViewHolder(private val binding: ItemAnswerBinding, private val mainVm: MainVM): RecyclerView.ViewHolder(binding.root){
        private val quizVM = QuizItemVM()

        fun bind(quiz: Quiz){
            quizVM.bind(quiz)
            binding.viewModel = quizVM
            binding.result.setOnClickListener {
                if(mainVm.checkResult(quiz.answer)) {
                    mainVm.animation.value = true
                    mainVm.showNextQuiz()
                }
            }
        }
    }
}
