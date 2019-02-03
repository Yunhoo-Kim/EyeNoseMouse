package com.hooitis.hoo.eyenosemouse.model.eyenosemouse

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class Versions(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val appVersion: Long,
        val dbVersion : Long
)