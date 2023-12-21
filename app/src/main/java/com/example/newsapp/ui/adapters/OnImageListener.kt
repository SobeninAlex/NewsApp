package com.example.newsapp.ui.adapters

import android.widget.ImageView
import com.example.newsapp.models.Article

interface OnImageListener {
    fun setImage(article: Article, imageView: ImageView)
}