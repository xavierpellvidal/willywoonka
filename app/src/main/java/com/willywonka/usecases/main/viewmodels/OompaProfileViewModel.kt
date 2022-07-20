package com.willywonka.usecases.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willywonka.model.data.OompaProfile
import com.willywonka.model.providers.OompaProvider
import kotlinx.coroutines.launch

class OompaProfileViewModel : ViewModel() {
    //Provider
    private var oompaProvider: OompaProvider = OompaProvider()

    // Public constants
    val oompa = MutableLiveData<OompaProfile>()
    val loader: MutableLiveData<Boolean> = MutableLiveData()

    // View Model
    fun loadView(id: Int) {
        //Load data coroutine
        viewModelScope.launch {
            loadData(id)
        }
    }

    private suspend fun loadData(id :Int){
        // Start data loading
        loader.postValue(true)

        // Load Oompa loompas
        val oompaReceived: OompaProfile = oompaProvider.getOompaByID(id)
        oompa.postValue(oompaReceived)

        // Hide loader
        loader.postValue(false)
    }
}