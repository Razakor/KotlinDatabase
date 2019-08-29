package com.vntu.database

import java.sql.DriverManager
import java.sql.SQLException

object DataBaseConnection {
    private const val url = "jdbc:sqlite:db_sw.db"
    @Throws(SQLException::class)
    fun connectToDB() {
        connection = DriverManager.getConnection(url)
        statement = connection.createStatement()
    }

}