package com.gk.cvscodechallenge.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.gk.cvscodechallenge.R
import com.gk.cvscodechallenge.api.dto.Item
import com.gk.cvscodechallenge.databinding.FragmentDetailsBinding
import com.gk.cvscodechallenge.databinding.LayoutDetailItemDetailRowBinding
import com.gk.cvscodechallenge.databinding.LayoutHeaderItemDetailRowBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding
    private val viewModel by activityViewModels<MainActivityViewModel>()
    private lateinit var adapter : DetailsFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        viewModel.selectedItem?.let {
            adapter = DetailsFragmentAdapter(it)
            binding.detailsList.adapter = adapter
        }

        return binding.root
    }
}

class DetailsFragmentAdapter(private val item: Item) : RecyclerView.Adapter<DetailsFragmentAdapter.ViewHolder>() {
    override fun getItemCount() = DetailsEnum.values().size
    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == DetailsEnum.IMAGE.ordinal) {
            HeaderItemViewHolder(LayoutHeaderItemDetailRowBinding.inflate(LayoutInflater.from(parent.context)))
        } else {
            DetailsItemViewHolder(LayoutDetailItemDetailRowBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item, DetailsEnum.values()[position])
    }

    enum class DetailsEnum {
        IMAGE, TITLE, DESCRIPTION, IMAGE_WIDTH, IMAGE_HEIGHT, AUTHOR
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Various View Holders
    ////////////////////////////////////////////////////////////////////////////////////////////////
    abstract class ViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: Item, detailEnum: DetailsEnum)
    }

    class HeaderItemViewHolder(private val binding: LayoutHeaderItemDetailRowBinding) : ViewHolder(binding) {
        override fun bind(item: Item, detailEnum: DetailsEnum) {
            Picasso
                .get()
                .load(item.link)
                .placeholder(R.drawable.hourglass)
                .error(R.drawable.icons8_broken_heart_96)
                .into(binding.detailImage, object: Callback {
                    override fun onSuccess() {}

                    override fun onError(e: Exception?) {
                        Log.d("BOOYA", "Detail Image Load Error ${item.link}, error: $e")
                    }
                })
        }
    }

    class DetailsItemViewHolder(private val binding: LayoutDetailItemDetailRowBinding) : ViewHolder(binding) {
        override fun bind(item: Item, detailEnum: DetailsEnum) {
            when(detailEnum) {
                DetailsEnum.IMAGE -> {
                    binding.detailLine.text = "Image: ${item.link}"
                }
                DetailsEnum.TITLE -> {
                    binding.detailLine.text = "Title: ${item.title}"
                }
                DetailsEnum.DESCRIPTION -> {
                    binding.detailLine.text = "Description: ${item.description}"
                }
                DetailsEnum.IMAGE_WIDTH -> {
                    binding.detailLine.text = "Image Width: TBD"
                }
                DetailsEnum.IMAGE_HEIGHT -> {
                    binding.detailLine.text = "Image Height: TBD"
                }
                DetailsEnum.AUTHOR -> {
                    binding.detailLine.text = "Author: ${item.author}"
                }
            }
        }
    }
}