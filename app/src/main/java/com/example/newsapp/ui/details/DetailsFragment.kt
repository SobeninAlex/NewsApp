package com.example.newsapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentDetailsBinding
import com.example.newsapp.models.Article
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs by navArgs<DetailsFragmentArgs>()
    private val viewModel by viewModels<DetailsViewModel>()

    private lateinit var article: Article

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        article = navigationArgs.article
        bindArticle(article)

        binding.visitSiteButton.setOnClickListener {
            launchBrowser(article)
        }

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModelObservers()

    }

    private fun viewModelObservers() {
        viewModel.getAllFavoriteArticle.observe(viewLifecycleOwner) { listArticles ->
            if (listArticles.contains(article)) {
                binding.iconHeart.setImageResource(R.drawable.icon_favorite_added)
                binding.iconHeart.setOnClickListener {
                    viewModel.deleteArticle(article)
                }
            } else {
                binding.iconHeart.setImageResource(R.drawable.icon_favorite)
                binding.iconHeart.setOnClickListener {
                    viewModel.saveArticle(article)
                }
            }
        }
    }

    private fun bindArticle(article: Article) {
        article.title?.let {
            binding.title.text = it
        }
        article.description?.let {
            binding.description.text = it
        }
        article.urlToImage?.let {
            Glide.with(this).load(it).into(binding.image)
        }
        binding.image.clipToOutline = true
    }

    private fun launchBrowser(article: Article) {
        try {
            Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(article.url ?: "https://google.com"))
                .let {
                    ContextCompat.startActivity(requireContext(), it, null)
                }
        } catch (exception: Exception) {
            Toast.makeText(
                context,
                getString(R.string.the_device_doesn_t_have_any_browser),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

/**
 * try {
 *             Intent()
 *                 .setAction(Intent.ACTION_VIEW)
 *                 .addCategory(Intent.CATEGORY_BROWSABLE)
 *                 .setData(Uri.parse(takeIf { URLUtil.isValidUrl(article.url) }
 *                     ?.let {
 *                         article.url
 *                     } ?: "https://google.com"))
 *                     .let {
 *                     ContextCompat.startActivity(requireContext(), it, null)
 *                 }
 *         } catch (exception: Exception) {
 *             Toast.makeText(
 *                 context,
 *                 getString(R.string.the_device_doesn_t_have_any_browser),
 *                 Toast.LENGTH_SHORT
 *             ).show()
 *         }
 */