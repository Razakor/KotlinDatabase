package com.vntu.database.entities

data class Country (val id: Int, val states: MutableList<State>, var name: String)