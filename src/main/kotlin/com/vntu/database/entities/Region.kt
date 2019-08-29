package com.vntu.database.entities

data class Region (val id: Int, val cities: MutableList<City>, var name: String)