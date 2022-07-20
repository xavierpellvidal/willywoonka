package com.willywonka.usecases.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willywonka.model.data.OompaProfile
import com.willywonka.model.providers.OompaProvider
import kotlinx.coroutines.launch

class OompaListViewModel : ViewModel() {
    //Provider
    private var oompaProvider: OompaProvider = OompaProvider()

    // Public vars
    var oopmas = MutableLiveData<MutableList<OompaProfile>>()
    val loader: MutableLiveData<Boolean> = MutableLiveData()

    //Private vars
    private var oompaList : MutableList<OompaProfile> = mutableListOf()
    private var currentPage: Int = 1

    init {
        //Load data coroutine
        viewModelScope.launch {
            loadData(currentPage)
        }
    }

    fun getOompasList(reset: Boolean){
        //Load data coroutine
        viewModelScope.launch {
            if(reset) currentPage = 1
            else currentPage++
            loadData(currentPage)
        }
    }

    suspend fun loadData(page: Int){
        if(currentPage == 1){
            // Start data loading
            loader.postValue(true)

            //Delete old oompas
            oompaList.clear()
        }

        // Load Oompas loompas
        val oompasReceived: MutableList<OompaProfile> = oompaProvider.getOompasByPage(page)

        //Add loader item at the end of the list
        oompasReceived.add(OompaProfile(-1))

        // Delete loader item to add new oompas
        if (currentPage > 1)
            oompaList.removeAt(oompaList.size -1)

        //Add oompas
        oompaList.addAll(oompasReceived)

        //Post value to view
        oopmas.postValue(oompaList)

        if(currentPage == 1) {
            // Hide loader
            loader.postValue(false)
        }
    }
}