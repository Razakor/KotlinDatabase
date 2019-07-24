package com.vntu.add

import com.vntu.directories.StudentStateController
import com.vntu.main.ComboBoxAutoComplete
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class AddStudInfoController {

    lateinit var closeButton: Button
    lateinit var nameTextField: TextField
    lateinit var surnameTextField: TextField
    lateinit var specialityComboBox: ComboBox<String>
    lateinit var studyFormComboBox: ComboBox<String>
    lateinit var additionTextField: TextField
    lateinit var razrTextField: TextField
    private lateinit var idCourse: String
    private lateinit var idInstitute: String
    private lateinit var idGroup: String
    private lateinit var studentStateController: StudentStateController

    @Throws(SQLException::class)
    fun initData(studentStateController: StudentStateController, course: String, instituteName: String, groupName: String) {
        this.studentStateController = studentStateController
        this.idCourse = course
        this.idInstitute = QueryResult.getListResult("SELECT id FROM institute WHERE short_name = '$instituteName'", false)[0].toString()
        this.idGroup = QueryResult.getListResult("SELECT id FROM `group` WHERE name = '$groupName'", false)[0].toString()
        setSpecialityComboBox()
        setStudyFormComboBox()
    }

    @Throws(SQLException::class)
    fun setSpecialityComboBox() {
        val query = "SELECT name FROM speciality"
        specialityComboBox.items = QueryResult.getListResult(query, false)
        ComboBoxAutoComplete(specialityComboBox)
    }

    @Throws(SQLException::class)
    fun setStudyFormComboBox() {
        val query = "SELECT name FROM study_form"
        studyFormComboBox.items = QueryResult.getListResult(query, false)
        ComboBoxAutoComplete(studyFormComboBox)
    }

    @Throws(SQLException::class)
    fun addStudentQuery() {
        val surname = Parser.processQuote(surnameTextField.text)
        val name = Parser.processQuote(nameTextField.text)
        val razr = Parser.processQuote(razrTextField.text)
        val addition = Parser.processQuote(additionTextField.text)
        val idSpeciality = QueryResult.getListResult(
            "SELECT id FROM speciality WHERE name = '${Parser.processQuote(specialityComboBox.value.toString())}'",
            false
        )[0].toString()
        val idStudyForm = QueryResult.getListResult(
            "SELECT id FROM study_form WHERE name = '${Parser.processQuote(studyFormComboBox.value.toString())}'",
            false
        )[0].toString()
        val query = "INSERT INTO student (surname, name, id_institute, id_group, id_speciality, id_study_form, addition, id_course, razr)" +
                    "VALUES ('$surname', '$name', '$idInstitute', '$idGroup', '$idSpeciality', '$idStudyForm', '$addition', '$idCourse', '$razr')"
        QueryResult.updateDataBase(query)
        clear()
        studentStateController.setGroup()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }

    private fun clear() {
        surnameTextField.clear()
        nameTextField.clear()
        razrTextField.clear()
        additionTextField.clear()
        specialityComboBox.value = null
        studyFormComboBox.value = null
    }
}