package com.example.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.models.Article

class NewsAdapter(
    private val listener: OnClickListener
) : PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(ArticleDiffItemCallback), View.OnClickListener {

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
        holder.bind(item)
    }

    interface OnClickListener {
        fun onClickIcon(article: Article)
        fun onClickItem(article: Article)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

//    override fun onClick(view: View?) {
//        val article = view?.tag as Article
//        if ()
//    }

}

private object ArticleDiffItemCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}