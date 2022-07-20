package com.willywonka.usecases.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.willywonka.R
import com.willywonka.databinding.FragmentOompaProfileBinding
import com.willywonka.model.data.OompaProfile
import com.willywonka.usecases.main.viewmodels.OompaProfileViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class OompaProfileFragment : Fragment() {
    // View model
    private val viewModel: OompaProfileViewModel by viewModels()

    // Life cycle
    private var _binding: FragmentOompaProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOompaProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val oompaID = arguments?.getInt("oompaID")
        viewModel.loadView(oompaID!!)

        //Disable filter menu item
        (activity as MainActivity).enableFilterMenuItem(false)

        //Clear toolbar title
        (activity as MainActivity).changeToolbarTitle("")

        data()
    }

    private fun data() {
        viewModel.oompa.observe(viewLifecycleOwner) {
            showOompaProfile(it)
        }

        viewModel.loader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                // Show loader
                binding.linearLoading.visibility = View.VISIBLE
                binding.linearProfile.visibility = View.GONE
            } else {
                // Hide loader
                binding.linearLoading.visibility = View.GONE
                binding.linearProfile.visibility = View.VISIBLE
            }
        }
    }

    private fun showOompaProfile(oompa: OompaProfile){
        //Set toolbar title
        (activity as MainActivity).changeToolbarTitle(oompa.first_name + " " + oompa.last_name)

        //Bind info
        binding.txtName.text = oompa.first_name + " " + oompa.last_name
        Glide.with(this).load(oompa.image).into(binding.imgProfile)
        binding.txtEmail.text = oompa.email

        if(oompa.gender == 'M'){
            binding.txtGender.text = "Male "
            binding.imgGender.setImageResource(R.drawable.ic_male_black)
        } else {
            binding.txtGender.text = "Female "
            binding.imgGender.setImageResource(R.drawable.ic_female_black)
        }

        binding.txtWork.text = oompa.profession + " "
        binding.txtLocation.text = oompa.country + " "
        binding.txtAge.text = oompa.age.toString() + " years old "

        binding.txtHeight.text = oompa.height.toString() + " cm "
        binding.txtFavTitle.text = oompa.first_name + "'s favourites..."
        binding.txtColor.text = oompa.favorite!!.color.replaceFirstChar { it.uppercase() } + " "
        binding.txtFood.text = oompa.favorite!!.food.replaceFirstChar { it.uppercase() } + " "
        binding.txtSong.text = oompa.favorite!!.song + " "
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}