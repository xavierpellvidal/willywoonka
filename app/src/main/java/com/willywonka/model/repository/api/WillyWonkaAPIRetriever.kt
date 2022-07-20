package com.willywonka.model.repository.api

import com.willywonka.model.data.OompaListResponse
import com.willywonka.model.data.OompaProfile
import com.willywonka.WillyWonkaApp

object WillyWonkaAPIRetriever {

    // Api functions

    /**
     * GET OOMPA LOOMPA BY ID
     */
    suspend fun getOompaWorker(id: Int) : OompaProfile {
        return WillyWonkaApp.apiService.getApi().getOompaWorker(id)
    }

    /**
     * GET OOMPA LOOMPAS LIST BY PAGE
     */
    suspend fun getOompaWorkersList(page: Int) : OompaListResponse {
        return WillyWonkaApp.apiService.getApi().getOompaWorkersList(page)
    }
}