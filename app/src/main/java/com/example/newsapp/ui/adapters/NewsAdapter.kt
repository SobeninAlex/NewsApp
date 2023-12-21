package com.example.newsapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.models.Article

class NewsAdapter(
    private val listener: NewsActionClickListener
) : PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(ArticleDiffItemCallback), View.OnClickListener {

    lateinit var onImageListener: OnImageListener

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        val article = view.tag as Article
        when (view.id) {
            R.id.icon_heart_item -> {
                listener.onFavoriteClick(article)
                notifyDataSetChanged()
            }
            else -> {
                listener.onArticleClick(article)
            }
        }
    }

    inner class NewsViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.iconHeartItem.setOnClickListener(this)

        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)!!

        with(holder.binding) {
            holder.itemView.tag = article
            iconHeartItem.tag = article

            Glide.with(articleImage.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.icon_newspaper)
                .into(articleImage)
            articleImage.clipToOutline = true

            articleDate.text = article.publishedAt
            articleTitle.text = article.title

            onImageListener.setImage(article, iconHeartItem)
        }
    }

}

private object ArticleDiffItemCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}