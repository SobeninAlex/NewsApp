package com.example.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.data.NewsRepository
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.models.Article

class NewsAdapter(
    private val clickListener: (Article) -> Unit,
) : PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(ArticleDiffItemCallback) {

    inner class NewsViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            with(binding) {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .into(articleImage)
                articleImage.clipToOutline = true
                articleDate.text = article.publishedAt
                articleTitle.text = article.title
            }
        }
    }

    val differ = AsyncListDiffer(this, ArticleDiffItemCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)!!

        holder.itemView.setOnClickListener {
            clickListener(item)
        }

        holder.bind(item)
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