package com.vntu.database

import com.vntu.database.entities.*
import java.sql.Connection
import java.sql.Statement

lateinit var connection: Connection
lateinit var statement: Statement

var countries = mutableListOf<Country>()