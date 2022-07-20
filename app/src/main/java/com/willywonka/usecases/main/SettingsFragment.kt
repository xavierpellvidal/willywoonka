package com.willywonka.usecases.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.willywonka.databinding.FragmentSettingsBinding
import com.willywonka.WillyWonkaApp

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Disable filter menu item
        (activity as MainActivity).enableFilterMenuItem(false)

        fillUI()
        setListeners()
    }

    private fun fillUI() {
        //If we have filters applied show it to UI
        if(WillyWonkaApp.mPrefs.getFilteredData()){
            if(WillyWonkaApp.mPrefs.getGenderFilter()!! == "M")
                binding.radioM.isChecked = true
            else
                binding.radioF.isChecked = true

            binding.editProf.setText(WillyWonkaApp.mPrefs.getProfessionFilter()!!)
        }
    }

    private fun setListeners() {
        binding.buttonApply.setOnClickListener {
            //Settings selected
            if(binding.radioF.isChecked || binding.radioM.isChecked || !binding.editProf.getText().isNullOrEmpty()){
                WillyWonkaApp.mPrefs.setFilteredData(true)
            }

            //Save gender filter
            if(binding.radioF.isChecked) WillyWonkaApp.mPrefs.setGenderFilter("F")
            else WillyWonkaApp.mPrefs.setGenderFilter("M")

            //Save profession filter
            if(binding.editProf.text != null)
                WillyWonkaApp.mPrefs.setProfessionFilter(binding.editProf.getText().toString())

            //Navigate up
            findNavController().navigateUp()
        }

        binding.buttonReset.setOnClickListener {
            //Reset prefs
            WillyWonkaApp.mPrefs.clear()

            //Navigate up
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}