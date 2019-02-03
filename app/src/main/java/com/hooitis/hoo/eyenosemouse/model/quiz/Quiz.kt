package com.hooitis.hoo.eyenosemouse.model.quiz

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.hooitis.hoo.eyenosemouse.utils.MALE


@Entity
data class Quiz(
        @PrimaryKey(autoGenerate = false)
        val id: Long = 0,
        val sex: Int = MALE,
        val answer: String,
        val leftEye: String,
        val rightEye: String,
        val nose: String,
        val imageUrl: String,
        val mouse: String
)