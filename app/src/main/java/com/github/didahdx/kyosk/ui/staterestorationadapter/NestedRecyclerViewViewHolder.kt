package com.github.didahdx.kyosk.ui.staterestorationadapter

import androidx.recyclerview.widget.RecyclerView

interface NestedRecyclerViewViewHolder {
    fun getId(): String
    fun getLayoutManager(): RecyclerView.LayoutManager?
}