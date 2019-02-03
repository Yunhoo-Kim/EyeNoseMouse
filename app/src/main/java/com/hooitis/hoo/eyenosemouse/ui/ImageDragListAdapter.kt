package com.hooitis.hoo.eyenosemouse.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hooitis.hoo.eyenosemouse.R
import com.hooitis.hoo.eyenosemouse.databinding.ItemAnswerBinding
import com.hooitis.hoo.eyenosemouse.model.quiz.Quiz
import com.hooitis.hoo.eyenosemouse.utils.UiUtils
import com.hooitis.hoo.eyenosemouse.vm.MainVM
import com.hooitis.hoo.eyenosemouse.vm.QuizItemVM


class ImageDragListAdapter : RecyclerView.Adapter<ImageDragListAdapter.ViewHolder>() {
    private lateinit var imageList: MutableList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAnswerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_answer,
                parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    fun updateResultList(imageList : MutableList<String>){
        this.imageList = imageList
        notifyDataSetChanged()
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                imageList[i + 1] = imageList[i]
                imageList[i] = imageList[i + 1]
            }
        } else {
            for (i in fromPosition..toPosition + 1) {
                imageList[i - 1] = imageList[i]
                imageList[i] = imageList[i + 1]
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }
    override fun getItemCount(): Int = if(::imageList.isInitialized) imageList.size else 0

    class ViewHolder(private val binding: ItemAnswerBinding): RecyclerView.ViewHolder(binding.root){
        private val quizVM = QuizItemVM()

        fun bind(imageUrl: String){
            quizVM.answer.postValue(imageUrl)
            binding.viewModel = quizVM
//            binding.result.setOnLongClickListener {
//                UiUtils.makeSnackbar(it.rootView, "long click")
//                return@setOnLongClickListener true
//            }
        }
    }
}
