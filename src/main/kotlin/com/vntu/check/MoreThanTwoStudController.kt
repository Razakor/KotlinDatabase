package com.vntu.check

import com.vntu.main.ComboBoxAutoComplete
import com.vntu.database.connection
import com.vntu.main.ExcelExport
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField

import java.io.IOException
import java.sql.SQLException
import java.util.ArrayList

class MoreThanTwoStudController {
    lateinit var countryComboBox: ComboBox<String>
    lateinit var stateComboBox: ComboBox<String>
    lateinit var regionComboBox: ComboBox<String>
    lateinit var cityComboBox: ComboBox<String>
    lateinit var textField: TextField

    private val CHOOSE_ALL = "Всі"
    private var countryValue = CHOOSE_ALL
    private var stateValue = CHOOSE_ALL
    private var regionValue = CHOOSE_ALL
    private var cityValue = CHOOSE_ALL
    private var localCountryValue = "IS NOT NULL"
    private var localStateValue = "IS NOT NULL"
    private var localRegionValue = "IS NOT NULL"
    private var localCityValue = "IS NOT NULL"

    @Throws(SQLException::class)
    fun initialize() {
        setCountryComboBox()
        setStateComboBox()
        setRegionComboBox()
        setCityComboBox()
        countryComboBox.selectionModel.selectFirst()
        stateComboBox.selectionModel.selectFirst()
        regionComboBox.selectionModel.selectFirst()
        cityComboBox.selectionModel.selectFirst()
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
                "WHERE country.name $localCountryValue"
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

    fun setCity() {
        if (cityComboBox.value == null) {
            cityComboBox.value = CHOOSE_ALL
        }
        cityValue = Parser.processQuote(cityComboBox.value)
    }


    @Throws(SQLException::class, IOException::class)
    fun form() {

        localCityValue = if (cityValue == CHOOSE_ALL) "IS NOT NULL" else "= '$cityValue'"

        val query =
            "SELECT enterprise.name, enterprise.`index`, enterprise.adress, enterprise.telephone, enterprise.fax, enterprise.email, enterprise.website, COUNT(distribution.id_enterprise) FROM distribution\n" +
                    "INNER JOIN enterprise ON distribution.id_enterprise = enterprise.id\n" +
                    "INNER JOIN city ON distribution.id_city = city.id\n" +
                    "GROUP BY distribution.id_enterprise\n" +
                    "HAVING COUNT(distribution.id_enterprise) >= " + textField.text + " AND city.name " + localCityValue
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)

        val headerList = ArrayList<String>()
        headerList.add("Назва підприємства")
        headerList.add("Індекс")
        headerList.add("Адреса")
        headerList.add("Телефон")
        headerList.add("Факс")
        headerList.add("Email")
        headerList.add("Вебсайт")
        headerList.add("Кількість студентів")

        val rowList = ArrayList<String>()
        rowList.add("name")
        rowList.add("index")
        rowList.add("adress")
        rowList.add("telephone")
        rowList.add("fax")
        rowList.add("email")
        rowList.add("website")
        rowList.add("COUNT(distribution.id_enterprise)")

        ExcelExport.export(resultSet, headerList, rowList, "Enterprise")
        statement.close()
        resultSet.close()
    }
}