package com.willywonka.usecases.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.willywonka.R
import com.willywonka.databinding.ItemOompaLoadingBinding
import com.willywonka.databinding.ItemOompaProfileBinding
import com.willywonka.model.data.OompaProfile

class OompaListAdapter (val oompas: MutableList<OompaProfile>, val onOompaClick: (OompaProfile) -> Unit, var context : Context?) : RecyclerView.Adapter<OompaListAdapter.BaseViewHolder<OompaProfile>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<OompaProfile> {
        return when (viewType) {
            OOMPA_ITEM -> OompaProfileViewHolderStyle(ItemOompaProfileBinding.inflate(LayoutInflater.from(context), parent, false))
            LOADING_ITEM -> LoadingViewHolderStyle(ItemOompaLoadingBinding.inflate(LayoutInflater.from(context), parent, false))
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<OompaProfile>, position: Int) {
        when (holder) {
            is OompaProfileViewHolderStyle -> holder.bindStyle(oompas[position])
            is LoadingViewHolderStyle -> holder.bindStyle(oompas[position])
            else -> throw IllegalArgumentException()
        }
    }

    abstract class BaseViewHolder<OompaProfile>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindStyle(oompa: OompaProfile)
    }

    inner class OompaProfileViewHolderStyle(private val binding: ItemOompaProfileBinding) : BaseViewHolder<OompaProfile>(binding.root) {
        override fun bindStyle(oompa: OompaProfile): Unit = with(binding) {

            binding.txtName.text = oompa.first_name + " " + oompa.last_name
            Glide.with(context!!).load(oompa.image).into(binding.imgProfile)
            binding.txtEmail.text = oompa.email

            if(oompa.gender.equals('M')){
                binding.txtGender.text = "Male "
                binding.imgGender.setImageResource(R.drawable.ic_male_black)
            } else {
                binding.txtGender.text = "Female "
                binding.imgGender.setImageResource(R.drawable.ic_female_black)
            }

            binding.txtWork.text = oompa.profession + " "
            binding.txtLocation.text = oompa.country + " "

            itemView.setOnClickListener {
                onOompaClick(oompa)
            }
        }
    }

    inner class LoadingViewHolderStyle(binding: ItemOompaLoadingBinding) : BaseViewHolder<OompaProfile>(binding.root) {
        override fun bindStyle(oompa: OompaProfile) {}
    }

    override fun getItemCount(): Int = oompas.size

    override fun getItemViewType(position: Int): Int {
        return if(oompas[position].id == -1) LOADING_ITEM else OOMPA_ITEM
    }

    companion object {
        private const val OOMPA_ITEM = 1
        private const val LOADING_ITEM = 2
    }
}
