package com.vntu.distribution

import com.vntu.main.ComboBoxAutoComplete
import com.vntu.main.Docx4j
import com.vntu.main.QueryResult
import com.vntu.main.WordMerge
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.apache.commons.io.FileUtils
import java.awt.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.sql.SQLException
import java.util.*

class ReferralController {

    lateinit var instituteComboBox: ComboBox<String>
    lateinit var groupComboBox: ComboBox<String>
    private lateinit var stage: Stage
    private val CHOOSE_ALL = "Всі"
    private val filePaths = ArrayList<String>()
    private val directoryPath = ""


    @Throws(SQLException::class)
    fun initialize() {
        setInstituteComboBox()
        setGroupComboBox()
        instituteComboBox.selectionModel.selectFirst()
        groupComboBox.selectionModel.selectFirst()
    }

    fun initData(stage: Stage) {
        this.stage = stage
    }

    @Throws(SQLException::class)
    fun setInstituteComboBox() {
        if (instituteComboBox.value == null) {
            instituteComboBox.value = CHOOSE_ALL
        }

        val query = "SELECT DISTINCT institute.short_name FROM student\n" +
                "INNER JOIN institute\n" +
                "ON student.id_institute = institute.id"
        instituteComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(instituteComboBox)
    }

    @Throws(SQLException::class)
    fun setGroupComboBox() {
        if (groupComboBox.value == null) {
            groupComboBox.value = CHOOSE_ALL
        }
        val localInstituteValue =
            if (instituteComboBox.value.toString() == CHOOSE_ALL) "IS NOT NULL" else "= '" + instituteComboBox.value.toString() + "'"
        val query = "SELECT DISTINCT `group`.name FROM student\n" +
                "INNER JOIN institute\n" +
                "ON student.id_institute = institute.id\n" +
                "INNER JOIN `group`\n" +
                "ON student.id_group = `group`.id\n" +
                "WHERE institute.short_name " + localInstituteValue
        groupComboBox.items = QueryResult.getListResult(query, true)
        groupComboBox.value = CHOOSE_ALL
        ComboBoxAutoComplete(groupComboBox)
    }


    @Throws(Exception::class)
    fun exportDoc() {

        val localInstituteValue =
            if (instituteComboBox.value.toString() == CHOOSE_ALL) "IS NOT NULL" else "= '" + instituteComboBox.value.toString() + "'"
        val localGroupValue =
            if (groupComboBox.value.toString() == CHOOSE_ALL) "IS NOT NULL" else "= '" + groupComboBox.value.toString() + "'"

        val idQuery = "SELECT distribution.id_student FROM distribution\n" +
                "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = 0"

        val surnameQuery = "SELECT student.surname FROM distribution\n" +
                "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                "INNER JOIN student ON distribution.id_student = student.id\n" +
                "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = 0"

        val nameQuery = "SELECT student.name FROM distribution\n" +
                "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                "INNER JOIN student ON distribution.id_student = student.id\n" +
                "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = 0"

        val instituteQuery = "SELECT institute.short_name FROM distribution\n" +
                "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = 0"

        val groupQuery = "SELECT `group`.name FROM distribution\n" +
                "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = 0"

        val enterpriseQuery = "SELECT COALESCE (enterprise.name, vntu.name) FROM distribution\n" +
                "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                "LEFT JOIN enterprise ON distribution.id_enterprise = enterprise.id\n" +
                "LEFT JOIN vntu ON distribution.id_vntu = vntu.id\n" +
                "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = 0"

        val courseQuery = "SELECT student.id_course FROM distribution\n" +
                "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                "INNER JOIN student ON distribution.id_student = student.id\n" +
                "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = 0"

        val specialityQuery = "SELECT speciality.name FROM distribution\n" +
                "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                "INNER JOIN speciality ON distribution.id_speciality = speciality.id\n" +
                "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = 0"

        val nameList = QueryResult.getListResult(surnameQuery, false)
        val temp = QueryResult.getListResult(nameQuery, false)

        for (i in nameList.indices) {
            nameList[i] = nameList[i] + " " + temp[i]
        }

        val instituteList = QueryResult.getListResult(instituteQuery, false)
        val groupList = QueryResult.getListResult(groupQuery, false)
        val enterpriseList = QueryResult.getListResult(enterpriseQuery, false)
        val courseList = QueryResult.getListResult(courseQuery, false)
        val specialityList = QueryResult.getListResult(specialityQuery, false)
        val idList = QueryResult.getListResult(idQuery, false)

        var index = 1

        if (nameList.size == 0)
            return

        Docx4j.initializePaths(filePaths, directoryPath)

        run {
            var i = 0
            while (i < nameList.size) {
                Docx4j.start(
                    nameList.subList(i, if (i + 3 >= nameList.size) nameList.size else i + 3),
                    instituteList.subList(i, if (i + 3 >= instituteList.size) instituteList.size else i + 3),
                    groupList.subList(i, if (i + 3 >= groupList.size) groupList.size else i + 3),
                    enterpriseList.subList(i, if (i + 3 >= enterpriseList.size) enterpriseList.size else i + 3),
                    courseList.subList(i, if (i + 3 >= courseList.size) courseList.size else i + 3),
                    specialityList.subList(i, if (i + 3 >= specialityList.size) specialityList.size else i + 3),
                    idList.subList(i, if (i + 3 >= idList.size) idList.size else i + 3), index
                )
                i += 3
                index++
            }
        }
        val faos = FileOutputStream(directoryPath + "Результат.docx")
        val wordMerge = WordMerge(faos)
        for (i in 1 until index) {
            wordMerge.add(FileInputStream(directoryPath + "output\\" + i + ".docx"))
        }
        wordMerge.doMerge()
        wordMerge.close()
        FileUtils.deleteDirectory(File(directoryPath + "output"))

        Desktop.getDesktop().open(File(directoryPath + "Результат.docx"))

        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Успішність результату"
        alert.headerText = "Чи були обрані студенти роздруковані?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            QueryResult.updateDataBase(
                "UPDATE distribution SET printed = 1\n" +
                        "WHERE id_student IN\n" +
                        "(SELECT id_student FROM distribution\n" +
                        "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                        "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                        "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + " AND printed = '0')"
            )
        }
    }

    fun openFile() {
        val fileChooser = FileChooser()
        val files = fileChooser.showOpenMultipleDialog(stage)
        if (files != null) {
            for (i in files.indices) {
                filePaths.add(files[i].absolutePath)
            }
            filePaths.sort()
        }
    }

    @Throws(SQLException::class)
    fun openDirectory() {

        val localInstituteValue =
            if (instituteComboBox.value.toString() == CHOOSE_ALL) "IS NOT NULL" else "= '" + instituteComboBox.value.toString() + "'"
        val localGroupValue =
            if (groupComboBox.value.toString() == CHOOSE_ALL) "IS NOT NULL" else "= '" + groupComboBox.value.toString() + "'"

        QueryResult.updateDataBase(
            "UPDATE distribution SET printed = 0\n" +
                    "WHERE id_student IN\n" +
                    "(SELECT id_student FROM distribution\n" +
                    "INNER JOIN `group` ON distribution.id_group = `group`.id\n" +
                    "INNER JOIN institute ON distribution.id_institute = institute.id\n" +
                    "WHERE institute.short_name " + localInstituteValue + " AND `group`.name " + localGroupValue + ")"
        )
    }
}
