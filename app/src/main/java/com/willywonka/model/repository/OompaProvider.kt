package com.willywonka.model.repository

import com.willywonka.model.data.OompaProfile
import com.willywonka.model.repository.api.WillyWonkaAPIRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OompaProvider {
    suspend fun getOompasByPage(page: Int): MutableList<OompaProfile> {
        return withContext(Dispatchers.IO) {
            // Try to get oompas list by page
            val oompas: MutableList<OompaProfile> = WillyWonkaAPIRetriever.getOompaWorkersList(page).results
            oompas
        }
    }

    suspend fun getOompaByID(id: Int): OompaProfile {
        return withContext(Dispatchers.IO) {
            // Try to get oompas list by page
            val oompa: OompaProfile = WillyWonkaAPIRetriever.getOompaWorker(id)
            oompa
        }
    }
}