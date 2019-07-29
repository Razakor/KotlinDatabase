package com.vntu.directories

import com.vntu.add.*
import com.vntu.edit.*
import com.vntu.main.*
import com.vntu.main.DataBaseConnection.connection
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.IOException
import java.sql.*
import java.util.*
import java.io.FileInputStream
import java.sql.SQLException
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class StudentStateController {

    lateinit var resultTable: TableView<ObservableList<String>>
    lateinit var editInstButton: Button
    lateinit var addInstButton: Button
    lateinit var editGroupButton: Button
    lateinit var addGroupButton: Button
    lateinit var addStudButton: Button
    lateinit var editStudButton: Button
    lateinit var deleteStudButton: Button
    lateinit var excelExportButton: Button
    lateinit var importExcel: Button
    lateinit var instituteComboBox: ComboBox<String>
    lateinit var groupComboBox: ComboBox<String>
    lateinit var courseComboBox: ComboBox<String>
    private val CHOOSE_ALL = "Всі"
    private var courseValue = CHOOSE_ALL
    private var instituteValue = CHOOSE_ALL
    private var groupValue = CHOOSE_ALL
    private var localCourseValue = "IS NOT NULL"
    private var localInstituteValue = "IS NOT NULL"
    private var localGroupValue = "IS NOT NULL"
    private var selectedRow: String? = null
    private var filePaths = ""
    private val stage: Stage? = null

    @Throws(SQLException::class)
    fun initialize() {
        setCourseComboBox()
        setInstituteComboBox()
        setGroupComboBox()
        initialResult()
        resultTable.selectionModel.selectedItemProperty().addListener { _, _, newSelection ->
            if (newSelection != null) {
                selectedRow = resultTable.selectionModel.selectedItem.toString()
                selectedRow = Parser.processBrackets(selectedRow!!)
            }
        }
    }

    @Throws(SQLException::class)
    private fun initialResult() {
        val query =
            "SELECT student.id, institute.short_name, `group`.name, student.surname, student.name, speciality.name, student.id_course, student.RAZR, student.addition, study_form.name\n" +
                    "FROM student\n" +
                    "INNER JOIN institute\n" +
                    "ON student.id_institute = institute.id\n" +
                    "INNER JOIN `group`\n" +
                    "ON student.id_group = `group`.id\n" +
                    "INNER JOIN speciality\n" +
                    "ON student.id_speciality = speciality.id\n" +
                    "INNER JOIN study_form\n" +
                    "ON student.id_study_form = study_form.id\n"
        resultTable = QueryResult.getTableResult(resultTable, query)
        courseComboBox.selectionModel.selectFirst()
        instituteComboBox.selectionModel.selectFirst()
        groupComboBox.selectionModel.selectFirst()
    }

    @Throws(SQLException::class)
    private fun setCourseComboBox() {
        val query = "SELECT value FROM course"
        courseComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(courseComboBox)
    }

    @Throws(SQLException::class)
    fun setInstituteComboBox() {
        val query = "SELECT short_name FROM institute"
        instituteComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(instituteComboBox)
    }

    @Throws(SQLException::class)
    fun setGroupComboBox() {
        localInstituteValue =
            if (instituteValue == CHOOSE_ALL) "IS NOT NULL" else "= '$instituteValue'"
        val query = "SELECT `group`.name FROM `group`\n" +
                "INNER JOIN institute ON `group`.id_institute = institute.id\n" +
                "WHERE institute.short_name " + localInstituteValue
        groupComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(groupComboBox)
    }

    @Throws(SQLException::class)
    fun setCourse() {
        if (courseComboBox.value == null) {
            courseComboBox.value = CHOOSE_ALL
        }
        courseValue = Parser.processQuote(courseComboBox.value)
        setInstituteComboBox()
    }

    @Throws(SQLException::class)
    fun setInstitute() {
        if (instituteComboBox.value == null) {
            instituteComboBox.value = CHOOSE_ALL
        }
        instituteValue = instituteComboBox.value
        instituteValue = Parser.processQuote(instituteValue)
        setGroupComboBox()
    }

    @Throws(SQLException::class)
    fun setGroup() {
        if (groupComboBox.value == null) {
            groupComboBox.value = CHOOSE_ALL
        }
        groupValue = Parser.processQuote(groupComboBox.value)
        localCourseValue = if (courseValue == CHOOSE_ALL) "IS NOT NULL" else "= '$courseValue'"
        localGroupValue = if (groupValue == CHOOSE_ALL) "IS NOT NULL" else "= '$groupValue'"
        localInstituteValue = if (instituteValue == CHOOSE_ALL) "IS NOT NULL" else "= '$instituteValue'"
        val query =
            "SELECT student.id, institute.short_name, `group`.name, student.surname, student.name, speciality.name, student.id_course, student.RAZR, student.addition, study_form.name\n" +
                    "FROM student\n" +
                    "INNER JOIN institute ON student.id_institute = institute.id\n" +
                    "INNER JOIN `group` ON student.id_group = `group`.id\n" +
                    "INNER JOIN speciality ON student.id_speciality = speciality.id\n" +
                    "INNER JOIN study_form ON student.id_study_form = study_form.id\n" +
                    "WHERE institute.short_name " + localInstituteValue + " AND  `group`.name " + localGroupValue + " AND student.id_course " + localCourseValue
        resultTable = QueryResult.getTableResult(resultTable, query)
    }

    @Throws(IOException::class)
    fun editInst() {
        if (instituteComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування факультету"
            alert.headerText = "Оберіть факультет перед редагуванням"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_inst.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Редагування факультету"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditInstController>()
        controller.initData(this)
        stage.show()
    }

    @Throws(IOException::class)
    fun addInst() {
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_inst.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додавання факультетів"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddInstController>()
        controller.initData(this)
        stage.show()
    }

    @Throws(IOException::class)
    fun editGroup() {
        if (groupComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування групи"
            alert.headerText = "Оберіть групу перед редагуванням"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_group.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Редагування групи"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditGroupController>()
        controller.initData(this, groupComboBox.value)
        stage.show()
    }

    @Throws(IOException::class)
    fun addGroup() {
        if (courseComboBox.value == CHOOSE_ALL || instituteComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка додавання групи"
            alert.headerText = "Перевірте поля перед додаванням групи"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_group.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додавання груп"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddGroupController>()
        controller.initData(this, instituteComboBox.value, courseComboBox.value)
        stage.show()
    }

    @Throws(IOException::class, SQLException::class)
    fun editStud() {
        if (selectedRow == null) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування студента"
            alert.headerText = "Оберіть студента для редагування"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_stud_info.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Редагування студента"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditStudInfoController>()
        controller.initData(this, selectedRow!!)
        stage.show()
    }

    @Throws(IOException::class, SQLException::class)
    fun addStud() {
        if (courseComboBox.value == CHOOSE_ALL || instituteComboBox.value == CHOOSE_ALL || groupComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка додавання студента"
            alert.headerText = "Перевірте поля перед додаванням студента"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_stud_info.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додавання інформації про студентів"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddStudInfoController>()
        controller.initData(this, courseComboBox.value, instituteComboBox.value, groupComboBox.value)
        stage.show()
    }

    @Throws(SQLException::class)
    fun deleteInstitute() {
        if (instituteComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення факультету"
            alert.headerText = "Оберіть факультет для видалення"
            alert.showAndWait()
            return
        }
        val name = instituteComboBox.value
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення факультету"
        alert.headerText = "Видалити \"$name\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query = "DELETE FROM institute WHERE institute.short_name = '$name'"
            QueryResult.updateDataBase(query)
            setInstituteComboBox()
        }
    }

    @Throws(SQLException::class)
    fun deleteGroup() {
        if (groupComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення групи"
            alert.headerText = "Оберіть групу для видалення"
            alert.showAndWait()
            return
        }
        val name = groupComboBox.value
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення групи"
        alert.headerText = "Видалити \"$name\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query = "DELETE FROM `group` WHERE `group`.name = '$name'"
            QueryResult.updateDataBase(query)
            setGroupComboBox()
        }
    }

    @Throws(SQLException::class)
    fun deleteStudent() {
        if (selectedRow == null) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення студента"
            alert.headerText = "Оберіть студента для видалення"
            alert.showAndWait()
            return
        }
        val studentInfo = listOf(*selectedRow!!.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення студента"
        alert.headerText = "Видалити \"${studentInfo[3]} ${studentInfo[4]}\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query = "DELETE FROM student WHERE student.id = '" + studentInfo[0] + "'"
            QueryResult.updateDataBase(query)
            setGroup()
        }
    }

    @Throws(IOException::class, SQLException::class)
    fun excelExport() {
        val query =
            "SELECT student.id, institute.short_name AS institute_short_name, `group`.name AS group_name, student.surname AS student_surname, student.name AS student_name, speciality.name AS speciality_name, student.id_course, student.RAZR, study_form.name AS study_form_name, student.addition\n" +
                    "FROM student\n" +
                    "INNER JOIN institute\n" +
                    "ON student.id_institute = institute.id\n" +
                    "INNER JOIN `group`\n" +
                    "ON student.id_group = `group`.id\n" +
                    "INNER JOIN speciality\n" +
                    "ON student.id_speciality = speciality.id\n" +
                    "INNER JOIN study_form\n" +
                    "ON student.id_study_form = study_form.id\n" +
                    "WHERE institute.short_name " + localInstituteValue + " AND  `group`.name " + localGroupValue + " AND student.id_course " + localCourseValue
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)

        val headerList = ArrayList<String>()
        headerList.add("Код")
        headerList.add("Інститут")
        headerList.add("Група")
        headerList.add("Прізвище")
        headerList.add("Ім'я та побатькові")
        headerList.add("Спеціальність")
        headerList.add("Курс")
        headerList.add("Розряд")
        headerList.add("Форма навчання")
        headerList.add("Додатково")

        val rowList = ArrayList<String>()
        rowList.add("id")
        rowList.add("institute_short_name")
        rowList.add("group_name")
        rowList.add("student_surname")
        rowList.add("student_name")
        rowList.add("speciality_name")
        rowList.add("id_course")
        rowList.add("RAZR")
        rowList.add("study_form_name")
        rowList.add("addition")

        ExcelExport.export(resultSet, headerList, rowList, "Students")
        statement.close()
        resultSet.close()
    }

    private fun openFile() {
        val fileChooser = FileChooser()
        val file = fileChooser.showOpenDialog(stage)
        if (file != null) {
            filePaths = file.absolutePath
        }
    }

    @Throws(SQLException::class, IOException::class, ClassNotFoundException::class)
    fun importExcel() {
        var surname: String
        var name: String
        var addition: String
        var course: Double
        var institute: String
        var group: String
        var studyForm: String
        var speciality: String
        var razr: Double
        var idInstitute: String
        var idGroup: String
        var idSpeciality: String
        var idStudyForm: String
        var sql: String

        openFile()

        Class.forName("com.mysql.cj.jdbc.Driver")
        connection.autoCommit = false
        var pstm: PreparedStatement? = null

        try {
            val input: FileInputStream
            if (filePaths != "") {
                input = FileInputStream(filePaths)
                filePaths = ""
            } else
                return
            val wb = XSSFWorkbook(input)
            val sheet = wb.getSheetAt(0)
            var row: Row

            for (i in 1..sheet.lastRowNum) {
                row = sheet.getRow(i)

                institute = row.getCell(0).stringCellValue
                group = row.getCell(1).stringCellValue
                surname = row.getCell(2).stringCellValue
                name = row.getCell(3).stringCellValue
                speciality = row.getCell(4).stringCellValue
                course = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).numericCellValue
                razr = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).numericCellValue
                studyForm = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).stringCellValue
                addition = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).stringCellValue

                surname = Parser.processQuote(surname)
                name = Parser.processQuote(name)
                speciality = Parser.processQuote(speciality)

                idInstitute = QueryResult.getListResult("SELECT id FROM institute WHERE short_name = '$institute'", false)[0].toString()
                idGroup = QueryResult.getListResult("SELECT id FROM `group` WHERE name = '$group'", false)[0].toString()
                idSpeciality = QueryResult.getListResult("SELECT id FROM speciality WHERE name = '$speciality'", false)[0].toString()
                idSpeciality = Parser.processQuote(idSpeciality)

                idStudyForm = QueryResult.getListResult(
                    "SELECT study_form.id FROM study_form INNER JOIN student ON study_form.id = student.id_study_form WHERE study_form.name = '$studyForm'",
                    false
                )[0].toString()

                var stringRazr = razr.toInt().toString()
                val stringCourse = course.toInt().toString()

                stringRazr = if (stringRazr === "") "NULL" else "'$stringRazr'"
                val localRazr = if (stringRazr === "NULL") "IS NULL" else "= $stringRazr"

                addition = if (addition === "") "NULL" else "'$addition'"
                val localAddition = if (addition === "NULL") "IS NULL" else "= $addition"

                sql =
                    ("INSERT INTO student (id_institute, id_group, surname, name, id_speciality, id_study_form, addition, id_course, RAZR) " +
                            "SELECT * FROM (SELECT '"
                            + idInstitute + "' AS d1,'"
                            + idGroup + "' AS d2,'"
                            + surname + "' AS d3,'"
                            + name + "' AS d4,'"
                            + idSpeciality + "' AS d5,'"
                            + idStudyForm + "' AS d6, "
                            + addition + " AS d7,'"
                            + stringCourse + "' AS d8,"
                            + stringRazr + " AS d9) AS tmp\n" +
                            "WHERE NOT EXISTS(SELECT * FROM student WHERE id_institute = '" + idInstitute + "' AND id_group = '" + idGroup + "' AND " +
                            " surname = '" + surname + "' AND name = '" + name + "' AND id_speciality = '" + idSpeciality + "' AND id_study_form = '" + idStudyForm + "' AND addition " + localAddition + " \n" +
                            " AND id_course = '" + stringCourse + "' AND RAZR " + localRazr + " )")

                pstm = connection.prepareStatement(sql)
                pstm!!.execute()
            }
            connection.commit()
            pstm!!.close()
            input.close()
            val query =
                "SELECT student.id, institute.short_name, `group`.name, student.surname, student.name, speciality.name, student.id_course, student.RAZR, student.addition, study_form.name\n" +
                        "FROM student\n" +
                        "INNER JOIN institute\n" +
                        "ON student.id_institute = institute.id\n" +
                        "INNER JOIN `group`\n" +
                        "ON student.id_group = `group`.id\n" +
                        "INNER JOIN speciality\n" +
                        "ON student.id_speciality = speciality.id\n" +
                        "INNER JOIN study_form\n" +
                        "ON student.id_study_form = study_form.id\n" +
                        "WHERE institute.short_name " + localInstituteValue + " AND  `group`.name " + localGroupValue + " AND student.id_course " + localCourseValue
            resultTable = QueryResult.getTableResult(resultTable, query)
        } catch (e: IllegalStateException) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "ПОМИЛКА"
            alert.headerText = "Неправильний формат вхідних даних таблиці"
            alert.showAndWait()
        } catch (e: NotOfficeXmlFileException) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "ПОМИЛКА"
            alert.headerText = "Неправильний формат файлу"
            alert.showAndWait()
        }

    }
}
