package com.willywonka.usecases.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.willywonka.R
import com.willywonka.databinding.FragmentOompaListBinding
import com.willywonka.model.data.OompaProfile
import com.willywonka.WillyWonkaApp
import com.willywonka.usecases.main.adapters.OompaListAdapter
import com.willywonka.usecases.main.viewmodels.OompaListViewModel
import java.util.ArrayList

class OompasListFragment : Fragment(), (OompaProfile) -> Unit, SwipeRefreshLayout.OnRefreshListener {

    // Properties
    private var oompasAdapter: OompaListAdapter? = null
    private val oompasList = ArrayList<OompaProfile>()

    // View model
    private val viewModel: OompaListViewModel by viewModels()

    // Life cycle
    private var _binding: FragmentOompaListBinding? = null
    private val binding get() = _binding!!

    //Variables
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0
    private var loading = false
    private val visibleThreshold = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOompaListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Enable filter menu item
        (activity as MainActivity).enableFilterMenuItem(true)

        //Set toolbar title
        (activity as MainActivity).changeToolbarTitle(getString(R.string.oompa_list_label))

        setAdapter()
        setObservers()
        setListeners()
    }

    private fun setAdapter() {
        //Create adapter
        oompasAdapter = OompaListAdapter(oompasList, this, context)
        oompasAdapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Load info
        binding.listOompas.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        binding.listOompas.addItemDecoration(DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL))
        binding.listOompas.adapter = oompasAdapter
    }

    private fun setListeners() {
        binding.swipeRefresh.setOnRefreshListener(this)
        addScrollListener()
    }

    private fun addScrollListener(){
        binding.listOompas.addOnScrollListener( object : RecyclerView.OnScrollListener()
        {
            val linearLayoutManager = binding.listOompas.layoutManager as LinearLayoutManager
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)

                //If scrolled y axis manually
                if(dy > 0) {
                    totalItemCount = linearLayoutManager.itemCount
                    linearLayoutManager.findLastVisibleItemPosition().also { lastVisibleItem = it }
                    //Last item of list
                    if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                        //Load more elements
                        viewModel.getOompasList(false)
                        loading = true
                    }
                }
            }
        })
    }

    private fun setObservers() {
        //Remove if any observer attached
        viewModel.oopmas.removeObservers(viewLifecycleOwner)
        viewModel.loader.removeObservers(viewLifecycleOwner)

        viewModel.oopmas.observe(viewLifecycleOwner) {
            loading = false
            showOompas(it)
        }

        viewModel.loader.observe(viewLifecycleOwner) { loader ->
            binding.swipeRefresh.isRefreshing = loader
        }
    }

    private fun showOompas(oompas: MutableList<OompaProfile>){
        //Clear list
        oompasList.clear()

        //If settings saved -> filter data
        if(WillyWonkaApp.mPrefs.getFilteredData()){
            //Filter oompas
            val oompasfiltered = filterOompas(oompas)

            //If we have results -> fill list
            if(oompasfiltered.isNotEmpty())
                oompasList.addAll(oompasfiltered)
            else
                Toast.makeText(context, getString(R.string.no_oompas_filtered), Toast.LENGTH_LONG).show()
        } else {
            //If we have results -> fill list
            if(oompas.size > 0)
                oompasList.addAll(oompas)
            else
                Toast.makeText(context, getString(R.string.no_oompas_filtered), Toast.LENGTH_LONG).show()
        }

        //Notify items changed
        binding.listOompas.adapter!!.notifyDataSetChanged()
    }

    private fun filterOompas(oompas: MutableList<OompaProfile>): List<OompaProfile> {
        var oompasfiltered: List<OompaProfile> = mutableListOf()

        //Filter by gender
        if(WillyWonkaApp.mPrefs.getGenderFilter()!! == "M" || WillyWonkaApp.mPrefs.getGenderFilter()!! == "F") {
            //Plus filter by profession if not null or empty
            if(!WillyWonkaApp.mPrefs.getProfessionFilter().isNullOrEmpty()){
                oompasfiltered = oompas.filter {
                    (it.profession.contains(WillyWonkaApp.mPrefs.getProfessionFilter()!!, ignoreCase = true)
                            && it.gender.toString() ==  WillyWonkaApp.mPrefs.getGenderFilter())
                            || it.id == -1
                }
            } else {
                oompasfiltered = oompas.filter {
                    it.gender.toString() ==  WillyWonkaApp.mPrefs.getGenderFilter()
                            ||  it.id == -1
                }
            }
        }
        else {
            //Only filter by profession if not null or empty
            if(!WillyWonkaApp.mPrefs.getProfessionFilter().isNullOrEmpty()){
                oompasfiltered = oompas.filter {
                    it.profession.contains(WillyWonkaApp.mPrefs.getProfessionFilter()!!, ignoreCase = true)
                            || it.id == -1
                }
            }
        }

        //We only have loader element -> no results
        if(oompasfiltered.size == 1 && oompasfiltered.get(0).id == -1) return mutableListOf()
        else return oompasfiltered
    }

    //Oompa prodile from list clicked
    override fun invoke(oompaClick: OompaProfile) {
        val bundle = bundleOf("oompaID" to oompaClick.id)
        findNavController(binding.root).navigate(R.id.action_OomplaLoompaFragment_to_OompaProfileFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRefresh() {
        viewModel.getOompasList(true)
    }
}