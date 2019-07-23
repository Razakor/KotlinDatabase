package com.vntu.main

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DataBaseConnection {
    lateinit var connection: Connection
    private const val URL = "jdbc:sqlite:db_sw.db"
    private const val USER = ""
    private const val PASSWORD = ""

    @Throws(SQLException::class)
    fun connectToDB() {
        connection = DriverManager.getConnection(URL, USER, PASSWORD)
    }

}