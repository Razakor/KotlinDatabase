package com.vntu.database.entities

data class Student (val id: Int,
                    var institute: Institute,
                    var group: Group,
                    var surname: String,
                    var name: String,
                    var speciality: Speciality,
                    var studyForm: StudyForm,
                    var addition: String,
                    var course: Course,
                    var razr: String)