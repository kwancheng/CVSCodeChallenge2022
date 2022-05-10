package com.gk.cvscodechallenge.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gk.cvscodechallenge.R
import com.gk.cvscodechallenge.api.dto.Item
import com.gk.cvscodechallenge.databinding.FragmentSearchBinding
import com.gk.cvscodechallenge.databinding.LayoutSearchResultItemBinding
import com.gk.cvscodechallenge.repository.FlickrRepository
import com.gk.cvscodechallenge.repository.SearchStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchFragmentViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private val resultAdapter = ItemResultsAdapter {
        activityViewModel.selectedItem = it
        findNavController().navigate(R.id.action_searchFragment_to_TSOFragment)
    }
    private val recentSearchTerm = mutableListOf<String>()
    private lateinit var recentSearchTermAdapter : ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchResultsList.adapter = resultAdapter

        recentSearchTermAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, recentSearchTerm)
        recentSearchTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.searchTermTextView.setAdapter(recentSearchTermAdapter)

        viewModel.recentSearchUpdates.observe(viewLifecycleOwner) {
            recentSearchTermAdapter.clear()
            recentSearchTermAdapter.addAll(it)
        }

        viewModel.searchTermStatus.observe(viewLifecycleOwner) {
            when (it) {
                is SearchStatus.Ready -> {
                    binding.emptyStateLabel.visibility = View.VISIBLE
                    binding.searchResultsList.visibility = View.GONE
                    binding.loadingIndicator.visibility = View.GONE
                }

                is SearchStatus.Loading -> {
                    binding.emptyStateLabel.visibility = View.GONE
                    binding.searchResultsList.visibility = View.VISIBLE
                    binding.searchButton.isEnabled = false
                    binding.loadingIndicator.visibility = View.VISIBLE
                }

                is SearchStatus.LoadComplete -> {
                    binding.emptyStateLabel.visibility = View.GONE
                    binding.loadingIndicator.visibility = View.GONE
                    binding.searchButton.isEnabled = true
                    resultAdapter.submitList(it.response.items)
                }

                is SearchStatus.LoadError -> {
                    binding.searchButton.isEnabled = true
                    binding.loadingIndicator.visibility = View.GONE
                    binding.emptyStateLabel.visibility = View.GONE

                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage(it.errorMsg)
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }

        binding.searchButton.setOnClickListener {
            viewModel.search(binding.searchTermTextView.text.toString())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val flickrRepository: FlickrRepository
) : ViewModel() {
    val searchTermStatus = flickrRepository.loadStatus.asLiveData()
    val recentSearchUpdates = flickrRepository.recentSearchUpdates.asLiveData()

    init {
        viewModelScope.launch {
            flickrRepository.primeRecentSearchUpdates()
        }
    }

    fun search(term: String) {
        viewModelScope.launch {
            flickrRepository.searchTerm(term)
        }
    }
}

class ItemResultsAdapter(
    val onItemClicked: (item: Item) -> Unit
) :
    ListAdapter<Item, ItemResultsAdapter.ItemViewHolder>(Differ)
{
    object Differ : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.media?.m == newItem.media?.m && oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.media?.m == newItem.media?.m && oldItem.title == newItem.title
        }
    }

    class ItemViewHolder(
        private val binding: LayoutSearchResultItemBinding,
        private val onItemClicked: (item: Item) -> Unit
    ):  RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: Item) {
            Picasso.get()
                .load(item.media?.m)
                .resize(200,200)
                .centerCrop()
                .placeholder(R.drawable.hourglass)
                .error(R.drawable.icons8_broken_heart_96)
                .into(binding.resultImage, object: Callback {
                    override fun onSuccess() {}

                    override fun onError(e: Exception?) {
                        Log.d("BOOYA", "Error downloading ${item.title} url: [${item.media?.m}] cause: ${e.toString()}")
                    }
                })

            binding.titleText.text = item.title

            binding.searchResultItemContainer.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutSearchResultItemBinding.inflate(LayoutInflater.from(parent.context)), onItemClicked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
