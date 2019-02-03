package com.hooitis.hoo.eyenosemouse.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hooitis.hoo.eyenosemouse.R
import com.hooitis.hoo.eyenosemouse.databinding.ItemImageBinding
import com.hooitis.hoo.eyenosemouse.databinding.ItemQuizBinding
import com.hooitis.hoo.eyenosemouse.model.quiz.Quiz
import com.hooitis.hoo.eyenosemouse.vm.QuizItemVM


class QuizListAdapter: RecyclerView.Adapter<QuizListAdapter.ViewHolder>() {
    private lateinit var quizList: List<Quiz>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemQuizBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_quiz,
                parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(quizList[position])
    }

    fun updateQuizList(quizList: List<Quiz>){
        this.quizList = quizList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if(::quizList.isInitialized) quizList.size else 0

    class ViewHolder(private val binding: ItemQuizBinding): RecyclerView.ViewHolder(binding.root){
        private val quizVM = QuizItemVM()

        fun bind(quiz: Quiz){
            quizVM.bind(quiz)
            binding.viewModel = quizVM
        }
    }
}
