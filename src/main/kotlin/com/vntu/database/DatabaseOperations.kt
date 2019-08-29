package com.vntu.database

import com.vntu.database.entities.*

fun initAllData() {
    initCountries()
    initStates()
    initRegions()
    initCities()
}

fun initCountries() {
    val sql = "SELECT * FROM country"
    val rs = statement.executeQuery(sql)

    while(rs.next()) {
        val country = Country(rs.getInt("id"),
            mutableListOf(),
            rs.getString("name"))
        countries.add(country)
    }
}

fun initStates() {
    val sql = "SELECT * FROM state"
    val rs = statement.executeQuery(sql)

    while(rs.next()) {
        val state = State(
            rs.getInt("id"),
            mutableListOf(),
            rs.getString("name")
        )
        countries.filter { it.id == rs.getInt("id_country") }[0].states.add(state)
    }
}

fun initRegions() {
    val sql = "SELECT * FROM region"
    val rs = statement.executeQuery(sql)
    var stateList: List<State>

    while (rs.next()) {
        val region = Region(
            rs.getInt("id"),
            mutableListOf(),
            rs.getString("name")
        )
        countries.forEach {
            stateList = it.states.filter { state ->
                state.id == rs.getInt("id_state")
            }
            if (stateList.isNotEmpty()) {
                stateList[0].regions.add(region)
            }
        }
    }
}

fun initCities() {
    val sql = "SELECT * FROM city"
    val rs = statement.executeQuery(sql)
    var regionList: List<Region>

    while (rs.next()) {
        val city = City(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("index")
        )
        countries.forEach {
            it.states.forEach { state ->
                regionList = state.regions.filter { region ->
                    region.id == rs.getInt("id_region")
                }
                if(regionList.isNotEmpty()) {
                    regionList[0].cities.add(city)
                }
            }
        }
    }
}