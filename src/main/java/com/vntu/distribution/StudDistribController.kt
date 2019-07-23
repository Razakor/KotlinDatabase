package com.vntu.distribution

import com.vntu.main.ComboBoxAutoComplete
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.collections.ObservableList
import javafx.scene.control.*

import java.sql.Date
import java.sql.SQLException

class StudDistribController {

    lateinit var courseComboBox: ComboBox<String>
    lateinit var instituteComboBox: ComboBox<String>
    lateinit var groupComboBox: ComboBox<String>
    lateinit var studentsComboBox: ComboBox<String>
    lateinit var countryComboBox: ComboBox<String>
    lateinit var stateComboBox: ComboBox<String>
    lateinit var cityComboBox: ComboBox<String>
    lateinit var regionComboBox: ComboBox<String>
    lateinit var enterpriseComboBox: ComboBox<String>
    lateinit var specialityComboBox: ComboBox<String>
    lateinit var radioGroup: ToggleGroup
    lateinit var enterpriseVntuLabel: Label
    lateinit var changeButton: Button
    lateinit var contractNumberTextField: TextField
    lateinit var datePicker: DatePicker

    private lateinit var VNTUList: ObservableList<String>
    private val CHOOSE_ALL = "Всі"
    private var courseValue = CHOOSE_ALL
    private var instituteValue = CHOOSE_ALL
    private var groupValue = CHOOSE_ALL
    private var studentsValue = CHOOSE_ALL
    private var countryValue = CHOOSE_ALL
    private var stateValue = CHOOSE_ALL
    private var regionValue = CHOOSE_ALL
    private var cityValue = CHOOSE_ALL
    private var localCourseValue = "IS NOT NULL"
    private var localInstituteValue = "IS NOT NULL"
    private var localGroupValue = "IS NOT NULL"
    private var localStudentsSurnameValue = "IS NOT NULL"
    private var localStudentsNameValue = "IS NOT NULL"
    private var localCountryValue = "IS NOT NULL"
    private var localStateValue = "IS NOT NULL"
    private var localRegionValue = "IS NOT NULL"
    private var localCityValue = "IS NOT NULL"
    private var isVNTU = false

    @Throws(SQLException::class)
    fun initialize() {
        setCourseComboBox()
        setInstituteComboBox()
        setGroupComboBox()
        setStudentsComboBox()
        setCountryComboBox()
        setStateComboBox()
        setRegionComboBox()
        setCityComboBox()
        setEnterpriseComboBox()
        setSpecialityComboBox()
        setVNTUComboBox()
        courseComboBox.selectionModel.selectFirst()
        instituteComboBox.selectionModel.selectFirst()
        groupComboBox.selectionModel.selectFirst()
        studentsComboBox.selectionModel.selectFirst()
        countryComboBox.selectionModel.selectFirst()
        stateComboBox.selectionModel.selectFirst()
        regionComboBox.selectionModel.selectFirst()
        cityComboBox.selectionModel.selectFirst()
        enterpriseComboBox.selectionModel.selectFirst()
        specialityComboBox.selectionModel.selectFirst()
    }

    @Throws(SQLException::class)
    private fun setCourseComboBox() {
        val query = "SELECT DISTINCT id_course FROM student GROUP BY id_course"
        courseComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(courseComboBox)
    }

    @Throws(SQLException::class)
    private fun setInstituteComboBox() {
        localCourseValue = if (courseValue == CHOOSE_ALL) "IS NOT NULL" else "= '$courseValue'"
        val query = "SELECT DISTINCT institute.short_name FROM student\n" +
                "INNER JOIN institute\n" +
                "ON student.id_institute = institute.id\n" +
                "WHERE student.id_course " + localCourseValue
        instituteComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(instituteComboBox)
    }

    @Throws(SQLException::class)
    private fun setGroupComboBox() {
        localCourseValue = if (courseValue == CHOOSE_ALL) "IS NOT NULL" else "= '$courseValue'"
        localInstituteValue = if (instituteValue == CHOOSE_ALL) "IS NOT NULL" else "= '$instituteValue'"
        val query = "SELECT DISTINCT `group`.name FROM student\n" +
                "INNER JOIN institute\n" +
                "ON student.id_institute = institute.id\n" +
                "INNER JOIN `group`\n" +
                "ON student.id_group = `group`.id\n" +
                "WHERE student.id_course " + localCourseValue + " AND institute.short_name " + localInstituteValue
        groupComboBox.setItems(QueryResult.getListResult(query, true))
        ComboBoxAutoComplete(groupComboBox)
    }

    @Throws(SQLException::class)
    private fun setStudentsComboBox() {
        localCourseValue = if (courseValue == CHOOSE_ALL) "IS NOT NULL" else "= '$courseValue'"
        localInstituteValue = if (instituteValue == CHOOSE_ALL) "IS NOT NULL" else "= '$instituteValue'"
        localGroupValue = if (groupValue == CHOOSE_ALL) "IS NOT NULL" else "= '$groupValue'"
        localStudentsSurnameValue =
            if (studentsValue == CHOOSE_ALL) "IS NOT NULL" else "= '" + studentsValue.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        localStudentsNameValue =
            if (studentsValue == CHOOSE_ALL) "IS NOT NULL" else "= '" + studentsValue.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

        val query =
            "SELECT DISTINCT (student.surname || ' ' || student.name) AS student_fullname  FROM student\n" + // TODO: Test if this works, was rewritten with || instead of CONCAT

                    "INNER JOIN institute\n" +
                    "ON student.id_institute = institute.id\n" +
                    "INNER JOIN `group`\n" +
                    "ON student.id_group = `group`.id\n" +
                    "WHERE student.id_course " + localCourseValue +
                    " AND institute.short_name " + localInstituteValue +
                    " AND `group`.name " + localGroupValue +
                    " AND student.surname " + localStudentsSurnameValue +
                    " AND student.name " + localStudentsNameValue

        studentsComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(studentsComboBox)
    }

    @Throws(SQLException::class)
    private fun setCountryComboBox() {
        val query = "SELECT name FROM country"
        countryComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(countryComboBox)
    }

    @Throws(SQLException::class)
    private fun setStateComboBox() {
        localCountryValue = if (countryValue == CHOOSE_ALL) "IS NOT NULL" else "= '$countryValue'"
        val query = "SELECT DISTINCT state.name\n" +
                "FROM country\n" +
                "INNER JOIN state\n" +
                "ON state.id_country = country.id\n" +
                "WHERE country.name " + localCountryValue
        stateComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(stateComboBox)
    }

    @Throws(SQLException::class)
    private fun setRegionComboBox() {
        localCountryValue = if (countryValue == CHOOSE_ALL) "IS NOT NULL" else "= '$countryValue'"
        localStateValue = if (stateValue == CHOOSE_ALL) "IS NOT NULL" else "= '$stateValue'"
        val query = "SELECT DISTINCT region.name\n" +
                "FROM enterprise\n" +
                "INNER JOIN city ON enterprise.id_city = city.id\n" +
                "INNER JOIN region ON city.id_region = region.id\n" +
                "INNER JOIN state ON region.id_state = state.id\n" +
                "INNER JOIN country ON state.id_country = country.id\n" +
                "WHERE country.name " + localCountryValue + " AND state.name " + localStateValue
        regionComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(regionComboBox)
    }

    @Throws(SQLException::class)
    private fun setCityComboBox() {
        localCountryValue = if (countryValue == CHOOSE_ALL) "IS NOT NULL" else "= '$countryValue'"
        localStateValue = if (stateValue == CHOOSE_ALL) "IS NOT NULL" else "= '$stateValue'"
        localRegionValue = if (regionValue == CHOOSE_ALL) "IS NOT NULL" else "= '$regionValue'"
        val query = "SELECT DISTINCT city.name\n" +
                "FROM enterprise\n" +
                "INNER JOIN city ON enterprise.id_city = city.id\n" +
                "INNER JOIN region ON city.id_region = region.id\n" +
                "INNER JOIN state ON region.id_state = state.id\n" +
                "INNER JOIN country ON state.id_country = country.id\n" +
                "WHERE country.name " + localCountryValue + " AND state.name " + localStateValue + " AND region.name " + localRegionValue
        cityComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(cityComboBox)
    }

    @Throws(SQLException::class)
    private fun setEnterpriseComboBox() {
        localCountryValue = if (countryValue == CHOOSE_ALL) "IS NOT NULL" else "= '$countryValue'"
        localStateValue = if (stateValue == CHOOSE_ALL) "IS NOT NULL" else "= '$stateValue'"
        localRegionValue = if (regionValue == CHOOSE_ALL) "IS NOT NULL" else "= '$regionValue'"
        localCityValue = if (cityValue == CHOOSE_ALL) "IS NOT NULL" else "= '$cityValue'"
        val query = "SELECT DISTINCT enterprise.name\n" +
                "FROM enterprise\n" +
                "INNER JOIN city ON enterprise.id_city = city.id\n" +
                "INNER JOIN region ON city.id_region = region.id\n" +
                "INNER JOIN state ON region.id_state = state.id\n" +
                "INNER JOIN country ON state.id_country = country.id\n" +
                "WHERE country.name " + localCountryValue + " AND state.name " + localStateValue + " AND region.name " + localRegionValue + " AND city.name " + localCityValue
        enterpriseComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(enterpriseComboBox)
    }

    @Throws(SQLException::class)
    private fun setSpecialityComboBox() {
        val query = "SELECT speciality.name FROM speciality"
        specialityComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(specialityComboBox)
    }

    @Throws(SQLException::class)
    private fun setVNTUComboBox() {
        val query = "SELECT vntu.name FROM vntu"
        VNTUList = QueryResult.getListResult(query, true)
    }

    @Throws(SQLException::class)
    fun setCourse() {
        if (courseComboBox.value == null) {
            courseComboBox.value = CHOOSE_ALL
        }
        courseValue = courseComboBox.value
        courseValue = Parser.processQuote(courseValue)
        instituteValue = CHOOSE_ALL
        instituteComboBox.value = CHOOSE_ALL
        setInstituteComboBox()
    }

    @Throws(SQLException::class)
    fun setInstitute() {
        if (instituteComboBox.value == null) {
            instituteComboBox.value = CHOOSE_ALL
        }
        instituteValue = instituteComboBox.value
        instituteValue = Parser.processQuote(instituteValue)
        groupValue = CHOOSE_ALL
        groupComboBox.value = CHOOSE_ALL
        setGroupComboBox()
    }

    @Throws(SQLException::class)
    fun setGroup() {
        if (groupComboBox.value == null) {
            groupComboBox.value = CHOOSE_ALL
        }
        groupValue = groupComboBox.value
        groupValue = Parser.processQuote(groupValue)
        studentsValue = CHOOSE_ALL
        studentsComboBox.value = CHOOSE_ALL
        setStudentsComboBox()
    }

    @Throws(SQLException::class)
    fun setStudents() {
        if (studentsComboBox.value == null) {
            studentsComboBox.value = CHOOSE_ALL
        }
        studentsValue = Parser.processQuote(studentsComboBox.value)
        if (studentsValue != CHOOSE_ALL) {
            val studentName = studentsComboBox.value.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val surname = Parser.processQuote(studentName[0])
            val name = Parser.processQuote(studentName[1] + " " + studentName[2])

            localStudentsSurnameValue = if (studentsValue == CHOOSE_ALL) "IS NOT NULL" else "= '$surname'"
            localStudentsNameValue = if (studentsValue == CHOOSE_ALL) "IS NOT NULL" else "= '$name'"

            val query = "SELECT speciality.name FROM speciality\n" +
                    "INNER JOIN student ON student.id_speciality = speciality.id\n" +
                    "WHERE student.surname " + localStudentsSurnameValue + " AND student.name " + localStudentsNameValue
            specialityComboBox.value = QueryResult.getListResult(query, false).get(0).toString()
        }
    }

    @Throws(SQLException::class)
    fun setCountry() {
        if (countryComboBox.value == null) {
            countryComboBox.value = CHOOSE_ALL
        }
        countryValue = countryComboBox.value
        countryValue = Parser.processQuote(countryValue)
        setStateComboBox()
        stateValue = CHOOSE_ALL
        stateComboBox.value = CHOOSE_ALL
    }

    @Throws(SQLException::class)
    fun setState() {
        if (stateComboBox.value == null) {
            stateComboBox.value = CHOOSE_ALL
        }
        stateValue = stateComboBox.value
        stateValue = Parser.processQuote(stateValue)
        setRegionComboBox()
        regionValue = CHOOSE_ALL
        regionComboBox.value = CHOOSE_ALL
    }

    @Throws(SQLException::class)
    fun setRegion() {
        if (regionComboBox.value == null) {
            regionComboBox.value = CHOOSE_ALL
        }
        regionValue = regionComboBox.value
        regionValue = Parser.processQuote(regionValue)
        setCityComboBox()
        cityValue = CHOOSE_ALL
        cityComboBox.value = CHOOSE_ALL
    }

    @Throws(SQLException::class)
    fun setCity() {
        if (cityComboBox.value == null) {
            cityComboBox.value = CHOOSE_ALL
        }
        cityValue = cityComboBox.value
        cityValue = Parser.processQuote(cityValue)
        setEnterpriseComboBox()
        enterpriseComboBox.value = CHOOSE_ALL
    }

    fun setEnterprise() {
        if (enterpriseComboBox.value == null) {
            enterpriseComboBox.value = CHOOSE_ALL
        }
    }

    fun setSpeciality() {
        if (specialityComboBox.value == null) {
            specialityComboBox.value = CHOOSE_ALL
        }
    }

    @Throws(SQLException::class)
    fun change() {
        if (!isVNTU) {
            enterpriseVntuLabel.text = "Підрозділ ВНТУ"
            enterpriseComboBox.items = VNTUList
            ComboBoxAutoComplete(enterpriseComboBox)
            enterpriseComboBox.selectionModel.selectFirst()
            changeButton.text = "Підприємства"
            isVNTU = true
        } else {
            enterpriseVntuLabel.text = "Підприємство"
            setEnterpriseComboBox()
            enterpriseComboBox.selectionModel.selectFirst()
            changeButton.text = "ВНТУ"
            isVNTU = false
        }
    }

    @Throws(SQLException::class)
    fun distribute() {
        val query: String
        val idContract: String
        val idCity: String
        var idEnterprise: String? = null
        val idInstitute: String
        val idGroup: String
        val idStudent: String
        val idSpeciality: String
        var idVNTU: String? = null
        val documentType: String
        val date: Date

        if (cityComboBox.value != CHOOSE_ALL && instituteComboBox.value != CHOOSE_ALL && groupComboBox.value != CHOOSE_ALL &&
            specialityComboBox.value != CHOOSE_ALL && enterpriseComboBox.value != CHOOSE_ALL &&
            studentsComboBox.value != CHOOSE_ALL && contractNumberTextField.text != "" && datePicker.value != null
        ) {

            idCity = QueryResult.getListResult(
                "SELECT id FROM city WHERE name = '" + Parser.processQuote(cityComboBox.value) + "'",
                false
            )[0].toString()
            idInstitute = QueryResult.getListResult(
                "SELECT id FROM institute WHERE short_name = '" + instituteComboBox.value + "'",
                false
            )[0].toString()
            idGroup =
                QueryResult.getListResult("SELECT id FROM `group` WHERE name = '" + groupComboBox.value + "'", false)[0].toString()
            idSpeciality = QueryResult.getListResult(
                "SELECT id FROM speciality WHERE name = '" + Parser.processQuote(specialityComboBox.value) + "'",
                false
            )[0].toString()

            if (!isVNTU) {
                idEnterprise = QueryResult.getListResult(
                    "SELECT id FROM enterprise WHERE name = '" + Parser.processQuote(enterpriseComboBox.value) + "'",
                    false
                )[0].toString()
            } else {
                idVNTU = QueryResult.getListResult(
                    "SELECT id FROM vntu WHERE name = '" + Parser.processQuote(enterpriseComboBox.value) + "'", false
                )[0].toString()
            }

            val studentName =
                studentsComboBox.value.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val surname = Parser.processQuote(studentName[0])
            val name = Parser.processQuote(studentName[1] + " " + studentName[2])
            idStudent =
                QueryResult.getListResult("SELECT id FROM student WHERE surname = '$surname' AND name = '$name'", false)[0].toString()

            val radioButton = radioGroup.selectedToggle as RadioButton
            documentType = radioButton.text

            idContract = contractNumberTextField.text

            val localDate = datePicker.value
            date = Date.valueOf(localDate)

            query = if (!isVNTU) {
                "INSERT INTO distribution (id_contract, id_city, id_enterprise, id_institute, id_group, id_student, id_speciality, contract_type, date) \n" +
                        "VALUES ('$idContract', '$idCity', '$idEnterprise', '$idInstitute', '$idGroup', '$idStudent', '$idSpeciality', '$documentType', '$date')"
            } else {
                "INSERT INTO distribution (id_contract, id_institute, id_group, id_student, id_speciality, id_vntu, contract_type, date) \n" +
                        "VALUES ('$idContract', '$idInstitute', '$idGroup', '$idStudent', '$idSpeciality', '$idVNTU', '$documentType', '$date')"
            }
            QueryResult.updateDataBase(query)
        } else {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка розподілу"
            alert.headerText = "Перевірте поля перед розподілом"
            alert.showAndWait()
        }
    }
}
