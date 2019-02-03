package com.hooitis.hoo.eyenosemouse.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class DragManageAdapter(adapter: ImageDragListAdapter, context: Context, dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs)
{

    private val dragAdapter = adapter

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        dragAdapter.swapItems(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int  = 0

}