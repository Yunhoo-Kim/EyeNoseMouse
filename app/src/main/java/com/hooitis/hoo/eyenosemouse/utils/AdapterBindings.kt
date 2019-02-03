package com.hooitis.hoo.eyenosemouse.utils

import android.animation.ObjectAnimator
import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.hooitis.hoo.eyenosemouse.R
import com.hooitis.hoo.eyenosemouse.utils.extension.getParentActivity


@BindingAdapter("adapter")
@Suppress("unused")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("itemPosition")
@Suppress("unused")
fun setItemPosition(view: RecyclerView, position: MutableLiveData<Int>){

    if(position!!.value == 0) {
        view.smoothScrollToPosition(0)
        return
    }

    try{
        view.smoothScrollToPosition(position.value!!)
    }catch (e: NullPointerException){
        e.printStackTrace()
    }
}

@BindingAdapter("android:text")
@Suppress("unused")
fun setText(view: TextView, data: MutableLiveData<Float>?){
    if (data != null)
        view.text = data.value.toString()
}

@BindingAdapter("answerText")
@Suppress("unused")
fun setText(view: Button, data: MutableLiveData<String>){
    view.text = data.value.toString()
}

@BindingAdapter("imageRes")
@Suppress("unused")
fun setImageResource(view: ImageView, image_id: MutableLiveData<Int>?){
    if(image_id!!.value == 0){
        view.setImageResource(R.drawable.microphone)
        return
    }

    view.setImageResource(image_id.value!!)
}

@BindingAdapter("imageResUri")
@Suppress("unused")
fun setImageResourceByURI(view: ImageView, image_id: MutableLiveData<String>?){
    if(image_id!!.value.isNullOrEmpty()) {
        view.setImageResource(R.drawable.microphone)
        return
    }
    val imageUrl = image_id.value!!

    if(imageUrl.startsWith("http"))
        Glide.with(view.getParentActivity()!!)
            .load(imageUrl)
            .into(view)
    else
        Glide.with(view.getParentActivity()!!)
            .load(FirebaseStorage.getInstance().reference.child(imageUrl))
            .into(view)
}

@BindingAdapter("countText")
@Suppress("unused")
fun setCountText(view: TextView, count: MutableLiveData<String>){
    view.text = count.value

    val startSize = view.resources.getDimension(R.dimen.textTitleSize)
    val endSize = view.resources.getDimension(R.dimen.textMediumSize)

    val animator = ObjectAnimator.ofFloat(view, "textSize", startSize, endSize)
    ObjectAnimator.ofFloat()

    animator.duration = 1000
    animator.start()
}
@BindingAdapter("playerText")
@Suppress("unused")
fun setPlayerText(view: TextView, count: MutableLiveData<Int>){
    view.text = "${view.resources.getString(R.string.player)} ${count.value!! + 1}"
}

@BindingAdapter("beforeCountText")
@Suppress("unused")
fun setBeforeCountText(view: TextView, count: MutableLiveData<String>){
    view.text = count.value
}

