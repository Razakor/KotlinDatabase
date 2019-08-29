package com.vntu.database.entities

data class Enterprise (val id: Int,
                       var city: City,
                       var name: String,
                       var index: String,
                       var adress: String,
                       var telephone: String,
                       var fax: String,
                       var email: String,
                       var website: String)