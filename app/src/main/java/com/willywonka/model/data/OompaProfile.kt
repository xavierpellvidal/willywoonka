package com.willywonka.model.data

import java.io.Serializable

class OompaProfile (

    var id: Int = -1,
    var height: Int = 0,
    var country: String = "",
    var age: Int = 0,
    var email: String = "",
    var profession: String = "",
    var image: String = "",
    var gender: Char = ' ',
    var favorite: Favorite?,
    var first_name: String = "",
    var last_name: String = ""

) : Serializable {

    constructor(id: Int) : this(id, 0, "", 0, "", "", "", ' ', null)

}

