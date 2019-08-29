package com.vntu.database.entities

import java.sql.Date

data class Timings (val id: Int,
                    var start: Date,
                    var end: Date)