package com.hooitis.hoo.eyenosemouse.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.hooitis.hoo.eyenosemouse.R
import com.hooitis.hoo.eyenosemouse.base.BaseActivity
import com.hooitis.hoo.eyenosemouse.databinding.ActivityResultBinding
import com.hooitis.hoo.eyenosemouse.di.ViewModelFactory
import com.hooitis.hoo.eyenosemouse.model.quiz.Quiz
import com.hooitis.hoo.eyenosemouse.vm.MainVM
import javax.inject.Inject


class QuizResultActivity: BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: MainVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        val resultList = intent.getSerializableExtra("resultList") as ArrayList<Quiz>

        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        binding.resultList.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
        Log.d("Result", "${resultList.size}")
        setContentView(binding.root)
    }

}

