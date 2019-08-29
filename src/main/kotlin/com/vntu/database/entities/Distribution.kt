package com.vntu.database.entities

import java.sql.Date

data class Distribution (var idContract: Int,
                         var city: City,
                         var enterprise: Enterprise,
                         var institute: Institute,
                         var group: Group,
                         var student: Student,
                         var speciality: Speciality,
                         var vntu: VNTU,
                         var contractType: String,
                         var date: Date,
                         var printed: Boolean)