package com.vntu.edit

import com.vntu.directories.StudentStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class EditStudInfoController {

    lateinit var closeButton: Button
    lateinit var surnameTextField: TextField
    lateinit var nameTextField: TextField
    lateinit var additionTextField: TextField
    lateinit var razrTextField: TextField
    lateinit var specialityComboBox: ComboBox<String>
    lateinit var studyFormComboBox: ComboBox<String>
    private lateinit var studentStateController: StudentStateController
    private lateinit var studentInfo: List<String>

    @Throws(SQLException::class)
    fun initData(studentStateController: StudentStateController, selectedRow: String) {
        this.studentStateController = studentStateController
        studentInfo = listOf(*selectedRow.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        surnameTextField.text = studentInfo[3]
        nameTextField.text = studentInfo[4]
        additionTextField.text = studentInfo[8]
        razrTextField.text = studentInfo[7]
        specialityComboBox.items = QueryResult.getListResult("SELECT name FROM speciality", false)
        studyFormComboBox.items = QueryResult.getListResult("SELECT name FROM study_form", false)
        specialityComboBox.value = studentInfo[5]
        studyFormComboBox.value = studentInfo[9]
    }

    @Throws(SQLException::class)
    fun editStudInfo() {
        val surname = Parser.processQuote(surnameTextField.text)
        val name = Parser.processQuote(nameTextField.text)
        val addition = Parser.processQuote(additionTextField.text)
        val razr = Parser.processQuote(razrTextField.text)
        val speciality = Parser.processQuote(specialityComboBox.value.toString())
        val studyForm = Parser.processQuote(studyFormComboBox.value.toString())

        val idSpeciality = QueryResult.getListResult("SELECT id FROM speciality WHERE name = '$speciality'", false)[0].toString()
        val idStudyForm = QueryResult.getListResult("SELECT id FROM study_form WHERE name = '$studyForm'", false)[0].toString()

        val query = "UPDATE student SET surname = '" + surname + "', name = '" + name + "', razr = '" + razr + "', addition = '" + addition + "', id_speciality = '" + idSpeciality + "', id_study_form = '" + idStudyForm + "' WHERE id = '" + studentInfo!![0] + "'"
        QueryResult.updateDataBase(query)
        studentStateController.setGroup()
        val stage = closeButton.scene.window as Stage
        stage.close()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }


}
