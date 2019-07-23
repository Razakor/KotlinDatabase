package com.vntu.report

import com.vntu.main.ComboBoxAutoComplete
import com.vntu.main.DataBaseConnection.connection
import com.vntu.main.ExcelExport
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ToggleGroup
import java.io.IOException
import java.sql.SQLException
import java.util.ArrayList

class StateOfAffairsController {
    lateinit var instituteComboBox: ComboBox<String>
    lateinit var groupComboBox: ComboBox<String>
    lateinit var vntuComboBox: ComboBox<String>
    lateinit var countryComboBox: ComboBox<String>
    lateinit var stateComboBox: ComboBox<String>
    lateinit var regionComboBox: ComboBox<String>
    lateinit var cityComboBox: ComboBox<String>
    lateinit var enterpriseComboBox: ComboBox<String>
    lateinit var radioGroup: ToggleGroup
    lateinit var instituteLabel: Label
    lateinit var groupLabel: Label
    lateinit var vntuLabel: Label
    lateinit var countryLabel: Label
    lateinit var stateLabel: Label
    lateinit var regionLabel: Label
    lateinit var cityLabel: Label
    lateinit var enterpriseLabel: Label

    private val CHOOSE_ALL = "Всі"
    private var instituteValue = CHOOSE_ALL
    private var groupValue = CHOOSE_ALL
    private var countryValue = CHOOSE_ALL
    private var stateValue = CHOOSE_ALL
    private var regionValue = CHOOSE_ALL
    private var cityValue = CHOOSE_ALL
    private var enterpriseValue = CHOOSE_ALL
    private var vntuValue = CHOOSE_ALL
    private var localInstituteValue = "IS NOT NULL"
    private var localGroupValue = "IS NOT NULL"
    private var localCountryValue = "IS NOT NULL"
    private var localStateValue = "IS NOT NULL"
    private var localRegionValue = "IS NOT NULL"
    private var localCityValue = "IS NOT NULL"
    private var localEnterpriseValue = "IS NOT NULL"
    private var localVNTUValue = "IS NOT NULL"

    @Throws(SQLException::class)
    fun initialize() {
        setInstituteComboBox()
        setGroupComboBox()
        setVNTUComboBox()
        setCountryComboBox()
        setStateComboBox()
        setRegionComboBox()
        setCityComboBox()
        setEnterpriseComboBox()

        instituteComboBox.selectionModel.selectFirst()
        groupComboBox.selectionModel.selectFirst()
        vntuComboBox.selectionModel.selectFirst()
        countryComboBox.selectionModel.selectFirst()
        stateComboBox.selectionModel.selectFirst()
        regionComboBox.selectionModel.selectFirst()
        cityComboBox.selectionModel.selectFirst()
        enterpriseComboBox.selectionModel.selectFirst()

        countryLabel.isVisible = false
        stateLabel.isVisible = false
        regionLabel.isVisible = false
        cityLabel.isVisible = false
        enterpriseLabel.isVisible = false
        vntuLabel.isVisible = false

        countryComboBox.isVisible = false
        stateComboBox.isVisible = false
        regionComboBox.isVisible = false
        cityComboBox.isVisible = false
        enterpriseComboBox.isVisible = false
        vntuComboBox.isVisible = false

        radioGroup.toggles[0].selectedProperty().addListener { _, _, isNowSelected ->
            if (isNowSelected!!) {
                showAll()
                countryLabel.isVisible = false
                stateLabel.isVisible = false
                regionLabel.isVisible = false
                cityLabel.isVisible = false
                enterpriseLabel.isVisible = false
                vntuLabel.isVisible = false

                countryComboBox.isVisible = false
                stateComboBox.isVisible = false
                regionComboBox.isVisible = false
                cityComboBox.isVisible = false
                enterpriseComboBox.isVisible = false
                vntuComboBox.isVisible = false
            }
        }

        radioGroup.toggles[1].selectedProperty().addListener { _, _, isNowSelected ->
            if (isNowSelected!!) {
                showAll()
                instituteLabel.isVisible = false
                groupLabel.isVisible = false

                instituteComboBox.isVisible = false
                groupComboBox.isVisible = false
            }
        }

        radioGroup.toggles[2].selectedProperty().addListener { _, _, isNowSelected ->
            if (isNowSelected!!) {
                showAll()
                countryLabel.isVisible = false
                stateLabel.isVisible = false
                regionLabel.isVisible = false
                cityLabel.isVisible = false
                enterpriseLabel.isVisible = false
                vntuLabel.isVisible = false

                countryComboBox.isVisible = false
                stateComboBox.isVisible = false
                regionComboBox.isVisible = false
                cityComboBox.isVisible = false
                enterpriseComboBox.isVisible = false
                vntuComboBox.isVisible = false
            }
        }
    }

    private fun showAll() {
        instituteComboBox.selectionModel.selectFirst()
        groupComboBox.selectionModel.selectFirst()
        vntuComboBox.selectionModel.selectFirst()
        countryComboBox.selectionModel.selectFirst()
        stateComboBox.selectionModel.selectFirst()
        regionComboBox.selectionModel.selectFirst()
        cityComboBox.selectionModel.selectFirst()
        enterpriseComboBox.selectionModel.selectFirst()

        instituteLabel.isVisible = true
        groupLabel.isVisible = true
        countryLabel.isVisible = true
        stateLabel.isVisible = true
        regionLabel.isVisible = true
        cityLabel.isVisible = true
        enterpriseLabel.isVisible = true
        vntuLabel.isVisible = true

        instituteComboBox.isVisible = true
        groupComboBox.isVisible = true
        countryComboBox.isVisible = true
        stateComboBox.isVisible = true
        regionComboBox.isVisible = true
        cityComboBox.isVisible = true
        enterpriseComboBox.isVisible = true
        vntuComboBox.isVisible = true
    }


    @Throws(SQLException::class)
    private fun setInstituteComboBox() {
        val query = "SELECT institute.short_name FROM institute"
        instituteComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(instituteComboBox)
    }

    @Throws(SQLException::class)
    private fun setGroupComboBox() {
        localInstituteValue = if (instituteValue == CHOOSE_ALL) "IS NOT NULL" else "= '$instituteValue'"
        val query = "SELECT DISTINCT `group`.name FROM student\n" +
                "INNER JOIN institute\n" +
                "ON student.id_institute = institute.id\n" +
                "INNER JOIN `group`\n" +
                "ON student.id_group = `group`.id\n" +
                "WHERE institute.short_name " + localInstituteValue
        groupComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(groupComboBox)
    }

    @Throws(SQLException::class)
    private fun setVNTUComboBox() {
        val query = "SELECT vntu.name FROM vntu"
        vntuComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(vntuComboBox)
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
                "INNER JOIN city ON enterprise.id_city =  city.id\n" +
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
                "INNER JOIN city ON enterprise.id_city =  city.id\n" +
                "INNER JOIN region ON city.id_region = region.id\n" +
                "INNER JOIN state ON region.id_state = state.id\n" +
                "INNER JOIN country ON state.id_country = country.id\n" +
                "WHERE country.name " + localCountryValue + " AND state.name " + localStateValue + " AND region.name " + localRegionValue + " AND city.name " + localCityValue
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
    fun setInstitute() {
        if (instituteComboBox.value == null) {
            instituteComboBox.value = CHOOSE_ALL
        }
        instituteValue = instituteComboBox.value
        instituteValue = Parser.processQuote(instituteValue)
        setGroupComboBox()
        groupValue = CHOOSE_ALL
        groupComboBox.value = CHOOSE_ALL
    }

    fun setGroup() {
        if (groupComboBox.value == null) {
            groupComboBox.value = CHOOSE_ALL
        }
        groupValue = groupComboBox.value
    }

    fun setVNTU() {
        if (vntuComboBox.value == null) {
            vntuComboBox.value = CHOOSE_ALL
        }
        vntuValue = Parser.processQuote(vntuComboBox.value)
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
        enterpriseValue = Parser.processQuote(enterpriseComboBox.value)
    }

    @Throws(SQLException::class, IOException::class)
    fun excelExport() {
        when {
            radioGroup.toggles[0].isSelected -> {
                val headerList = ArrayList<String>()
                headerList.add("Прізвище")
                headerList.add("Ім'я")
                headerList.add("Факультет")
                headerList.add("Група")

                val rowList = ArrayList<String>()
                rowList.add("surname")
                rowList.add("student_name")
                rowList.add("institute_name")
                rowList.add("group_name")

                localInstituteValue = if (instituteValue == CHOOSE_ALL) "IS NOT NULL" else "= '$instituteValue'"
                localGroupValue = if (groupValue == CHOOSE_ALL) "IS NOT NULL" else "= '$groupValue'"
                val query =
                    "SELECT student.surname, student.name AS student_name, institute.short_name AS institute_name, `group`.name AS group_name FROM student\n" +
                            "LEFT JOIN distribution ON distribution.id_student = student.id\n" +
                            "INNER JOIN `group` ON student.id_group = `group`.id\n" +
                            "INNER JOIN institute ON student.id_institute = institute.id\n" +
                            "WHERE distribution.id_student IS NULL AND `group`.name " + localGroupValue + " AND institute.short_name " + localInstituteValue
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                ExcelExport.export(resultSet, headerList, rowList, "Not Distributed Students")
                statement.close()
                resultSet.close()
            }
            radioGroup.toggles[1].isSelected -> {
                val headerList = ArrayList<String>()
                headerList.add("Прізвище")
                headerList.add("Ім'я")
                headerList.add("Група")
                headerList.add("Спеціальність")
                headerList.add("Підприємство")

                val rowList = ArrayList<String>()
                rowList.add("surname")
                rowList.add("student_name")
                rowList.add("group_name")
                rowList.add("speciality_name")
                rowList.add("COALESCE (enterprise.name, vntu.name)")

                val query: String
                localGroupValue = if (groupValue == CHOOSE_ALL) "IS NOT NULL" else "= '$groupValue'"
                localCityValue = if (cityValue == CHOOSE_ALL) "IS NOT NULL" else "= '$cityValue'"
                localEnterpriseValue = if (enterpriseValue == CHOOSE_ALL) "IS NOT NULL" else "= '$enterpriseValue'"
                localVNTUValue = if (vntuValue == CHOOSE_ALL) "IS NOT NULL" else "= '$vntuValue'"

                if (vntuValue != CHOOSE_ALL) {
                    query =
                        "SELECT student.surname, student.name AS student_name, `group`.name AS group_name, speciality.name AS speciality_name, COALESCE (enterprise.name, vntu.name) FROM distribution\n" +
                                "INNER JOIN student ON distribution.id_student = student.id\n" +
                                "INNER JOIN speciality ON distribution.id_speciality = speciality.id\n" +
                                "INNER JOIN `group` ON student.id_group = `group`.id\n" +
                                "LEFT JOIN enterprise ON distribution.id_enterprise = enterprise.id\n" +
                                "LEFT JOIN vntu ON distribution.id_vntu = vntu.id\n" +
                                "WHERE COALESCE (enterprise.name, vntu.name) " + localVNTUValue + " AND `group`.name " + localGroupValue
                } else if (enterpriseValue != CHOOSE_ALL && cityValue != CHOOSE_ALL || enterpriseValue == CHOOSE_ALL && cityValue != CHOOSE_ALL || enterpriseValue != CHOOSE_ALL && cityValue == CHOOSE_ALL) {
                    query =
                        "SELECT student.surname, student.name AS student_name, `group`.name AS group_name, speciality.name AS speciality_name, COALESCE (enterprise.name, vntu.name) FROM distribution\n" +
                                "INNER JOIN student ON distribution.id_student = student.id\n" +
                                "INNER JOIN speciality ON distribution.id_speciality = speciality.id\n" +
                                "LEFT JOIN enterprise ON distribution.id_enterprise = enterprise.id\n" +
                                "INNER JOIN `group` ON student.id_group = `group`.id\n" +
                                "LEFT JOIN vntu ON distribution.id_vntu = vntu.id\n" +
                                "LEFT JOIN city ON distribution.id_city = city.id\n" +
                                "WHERE COALESCE (enterprise.name, vntu.name) " + localEnterpriseValue + " AND city.name " + localCityValue + " AND `group`.name " + localGroupValue
                } else {
                    query =
                        "SELECT student.surname, student.name AS student_name, `group`.name AS group_name, speciality.name AS speciality_name, COALESCE (enterprise.name, vntu.name) FROM distribution\n" +
                                "INNER JOIN student ON distribution.id_student = student.id\n" +
                                "INNER JOIN speciality ON distribution.id_speciality = speciality.id\n" +
                                "INNER JOIN `group` ON student.id_group = `group`.id\n" +
                                "LEFT JOIN enterprise ON distribution.id_enterprise = enterprise.id\n" +
                                "LEFT JOIN vntu ON distribution.id_vntu = vntu.id\n" +
                                "WHERE `group`.name " + localGroupValue
                }

                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                ExcelExport.export(resultSet, headerList, rowList, "Filtered Distributed Students")
                statement.close()
                resultSet.close()
            }
            else -> {
                val headerList = ArrayList<String>()
                headerList.add("Прізвище")
                headerList.add("Ім'я")
                headerList.add("Факультет")
                headerList.add("Група")
                headerList.add("Спеціальність")
                headerList.add("Підприємство")

                val rowList = ArrayList<String>()
                rowList.add("surname")
                rowList.add("student_name")
                rowList.add("institute_name")
                rowList.add("group_name")
                rowList.add("speciality_name")
                rowList.add("COALESCE (enterprise.name, vntu.name)")

                localInstituteValue = if (instituteValue == CHOOSE_ALL) "IS NOT NULL" else "= '$instituteValue'"
                localGroupValue = if (groupValue == CHOOSE_ALL) "IS NOT NULL" else "= '$groupValue'"
                val query =
                    "SELECT student.surname, student.name AS student_name, institute.short_name AS institute_name, `group`.name AS group_name, speciality.name AS speciality_name, COALESCE (enterprise.name, vntu.name) FROM distribution\n" +
                            "INNER JOIN student ON distribution.id_student = student.id\n" +
                            "INNER JOIN speciality ON distribution.id_speciality = speciality.id\n" +
                            "INNER JOIN `group` ON student.id_group = `group`.id\n" +
                            "INNER JOIN institute ON student.id_institute = institute.id\n" +
                            "LEFT JOIN enterprise ON distribution.id_enterprise = enterprise.id\n" +
                            "LEFT JOIN vntu ON distribution.id_vntu = vntu.id\n" +
                            "WHERE `group`.name " + localGroupValue + " AND institute.short_name " + localInstituteValue

                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                ExcelExport.export(resultSet, headerList, rowList, "Distributed Students")
                statement.close()
                resultSet.close()
            }
        }
    }
}
