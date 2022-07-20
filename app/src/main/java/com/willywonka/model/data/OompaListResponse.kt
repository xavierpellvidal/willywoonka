package com.willywonka.model.data

import java.io.Serializable

class OompaListResponse (

    var current: Int = 0,
    var total: Int = 0,
    var results: MutableList<OompaProfile>

) : Serializable

