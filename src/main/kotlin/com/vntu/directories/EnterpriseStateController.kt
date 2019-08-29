package com.vntu.directories

import com.vntu.add.*
import com.vntu.edit.*
import com.vntu.main.*
import com.vntu.database.connection
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.collections.transformation.SortedList
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import java.io.IOException
import java.sql.SQLException
import java.util.*
import javafx.stage.Stage

class EnterpriseStateController {

    lateinit var resultTable: TableView<ObservableList<String>>
    lateinit var setCountryComboBox: ComboBox<String>
    lateinit var setStateComboBox: ComboBox<String>
    lateinit var setRegionComboBox: ComboBox<String>
    lateinit var setCityComboBox: ComboBox<String>
    lateinit var searchTextField: TextField

    private val CHOOSE_ALL = "Всі"
    private var countryValue = CHOOSE_ALL
    private var stateValue = CHOOSE_ALL
    private var regionValue = CHOOSE_ALL
    private var cityValue = CHOOSE_ALL
    private var localCountryValue = "IS NOT NULL"
    private var localStateValue = "IS NOT NULL"
    private var localRegionValue = "IS NOT NULL"
    private var localCityValue = "IS NOT NULL"
    private var selectedRow: String? = null

    @Throws(SQLException::class)
    fun initialize() {
        setCountryComboBox()
        setStateComboBox()
        setRegionComboBox()
        setCityComboBox()
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
            "SELECT enterprise.`index`, enterprise.adress, enterprise.name, enterprise.telephone, enterprise.fax, enterprise.email, enterprise.website, city.name, region.name, state.name, country.name\n" +
                    "FROM enterprise\n" +
                    "INNER JOIN `city`\n" +
                    "ON enterprise.id_city = `city`.id\n" +
                    "INNER JOIN region\n" +
                    "ON `city`.id_region = region.id\n" +
                    "INNER JOIN state\n" +
                    "ON region.id_state = state.id\n" +
                    "INNER JOIN country\n" +
                    "ON state.id_country = country.id"

        setTable(query, resultTable, searchTextField)

        setCountryComboBox.selectionModel.selectFirst()
        setStateComboBox.selectionModel.selectFirst()
        setRegionComboBox.selectionModel.selectFirst()
        setCityComboBox.selectionModel.selectFirst()
    }

    @Throws(SQLException::class)
    private fun setTable(query: String, table: TableView<ObservableList<String>>, searchTextField: TextField) {
        var resultTable = table
        resultTable = QueryResult.getTable(resultTable, query)
        val observableList = QueryResult.getTableAsList(query)
        val filteredList = FilteredList(observableList) { true }

        resultTable.items = filteredList
        searchTextField.textProperty().addListener { _, _, newValue ->
            filteredList.setPredicate { value ->
                val lowerCaseFilter = newValue.toLowerCase()
                when {
                    newValue == null || newValue.isEmpty() -> true
                    value[2].toString().toLowerCase().contains(lowerCaseFilter) -> true
                    else -> value[2].toString().toLowerCase().contains(lowerCaseFilter)
                }
            }

            val sortedList = SortedList<ObservableList<String>>(filteredList)
            sortedList.comparatorProperty().bind(resultTable.comparatorProperty())
            resultTable.setItems(sortedList)
        }
    }

    @Throws(SQLException::class)
    fun setCountryComboBox() {
        val query = "SELECT DISTINCT name FROM country GROUP BY country.id"
        setCountryComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(setCountryComboBox)
    }

    @Throws(SQLException::class)
    fun setStateComboBox() {
        localCountryValue = if (countryValue == CHOOSE_ALL) "IS NOT NULL" else "= '$countryValue'"
        val query = "SELECT DISTINCT state.name FROM state\n" +
                "INNER JOIN country ON state.id_country = country.id\n" +
                "WHERE country.name " + localCountryValue
        setStateComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(setStateComboBox)
    }

    @Throws(SQLException::class)
    fun setRegionComboBox() {
        localStateValue = if (stateValue == CHOOSE_ALL) "IS NOT NULL" else "= '$stateValue'"
        localCountryValue = if (countryValue == CHOOSE_ALL) "IS NOT NULL" else "= '$countryValue'"
        val query = "SELECT region.name FROM region\n" +
                "INNER JOIN state ON region.id_state = state.id\n" +
                "INNER JOIN country ON state.id_country = country.id\n" +
                "WHERE state.name " + localStateValue + " AND country.name " + localCountryValue
        setRegionComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(setRegionComboBox)
    }

    @Throws(SQLException::class)
    fun setCityComboBox() {
        localRegionValue = if (regionValue == CHOOSE_ALL) "IS NOT NULL" else "= '$regionValue'"
        localCountryValue = if (countryValue == CHOOSE_ALL) "IS NOT NULL" else "= '$countryValue'"
        localStateValue = if (stateValue == CHOOSE_ALL) "IS NOT NULL" else "= '$stateValue'"
        val query = "SELECT city.name FROM city\n" +
                "INNER JOIN region ON city.id_region = region.id\n" +
                "INNER JOIN state ON region.id_state = state.id\n" +
                "INNER JOIN country ON state.id_country = country.id\n" +
                "WHERE region.name " + localRegionValue + " AND state.name " + localStateValue + " AND country.name " + localCountryValue
        setCityComboBox.items = QueryResult.getListResult(query, true)
        ComboBoxAutoComplete(setCityComboBox)
    }

    @Throws(SQLException::class)
    fun setCountry() {
        if (setCountryComboBox.value == null) {
            setCountryComboBox.value = CHOOSE_ALL
        }
        countryValue = Parser.processQuote(setCountryComboBox.value)
        setStateComboBox()
    }

    @Throws(SQLException::class)
    fun setState() {
        if (setStateComboBox.value == null) {
            setStateComboBox.value = CHOOSE_ALL
        }
        stateValue = Parser.processQuote(setStateComboBox.value)
        setRegionComboBox()
    }

    @Throws(SQLException::class)
    fun setRegion() {
        if (setRegionComboBox.value == null) {
            setRegionComboBox.value = CHOOSE_ALL
        }
        regionValue = Parser.processQuote(setRegionComboBox.value)
        setCityComboBox()
    }

    @Throws(SQLException::class)
    fun setCity() {
        if (setCityComboBox.value == null) {
            setCityComboBox.value = CHOOSE_ALL
        }
        cityValue = Parser.processQuote(setCityComboBox.value)
        localCountryValue = if (countryValue == CHOOSE_ALL) "IS NOT NULL" else "= '$countryValue'"
        localStateValue = if (stateValue == CHOOSE_ALL) "IS NOT NULL" else "= '$stateValue'"
        localRegionValue = if (regionValue == CHOOSE_ALL) "IS NOT NULL" else "= '$regionValue'"
        localCityValue = if (cityValue == CHOOSE_ALL) "IS NOT NULL" else "= '$cityValue'"
        val query =
            "SELECT enterprise.`index`, enterprise.adress, enterprise.name, enterprise.telephone, enterprise.fax, enterprise.email, enterprise.website, city.name, region.name, state.name, country.name\n" +
                    "FROM enterprise\n" +
                    "INNER JOIN `city` ON enterprise.id_city = `city`.id\n" +
                    "INNER JOIN region ON `city`.id_region = region.id\n" +
                    "INNER JOIN state ON region.id_state = state.id\n" +
                    "INNER JOIN country ON state.id_country = country.id\n" +
                    "WHERE country.name " + localCountryValue + " AND state.name " + localStateValue + " AND region.name " + localRegionValue + " AND city.name " + localCityValue
        setTable(query, resultTable, searchTextField)
    }

    @Throws(IOException::class)
    fun addCountry() {
        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_country.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додання нової країни"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddCountryController>()
        controller.initData(this)
        stage.show()
    }

    @Throws(IOException::class)
    fun editCountry() {
        if (setCountryComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування країни"
            alert.headerText = "Оберіть країну для редагування"
            alert.showAndWait()
            return
        }
        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_country.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Зміна даних про країну"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditCountryController>()
        controller.initData(this, setCountryComboBox.value)
        stage.show()
    }

    @Throws(SQLException::class)
    fun deleteCountry() {
        if (setCountryComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення країни"
            alert.headerText = "Оберіть країну для видалення"
            alert.showAndWait()
            return
        }
        val name = Parser.processQuote(setCountryComboBox.value)
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення країни"
        alert.headerText = "Видалити \"$name\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query = "DELETE FROM country WHERE country.name = '$name'"
            QueryResult.updateDataBase(query)
            setCountryComboBox()
        }
    }

    @Throws(IOException::class, SQLException::class)
    fun addState() {
        if (setCountryComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка додавання області"
            alert.headerText = "Перевірте поля перед додаванням області"
            alert.showAndWait()
            return
        }

        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_state.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додання нової області"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddStateController>()
        controller.initData(this, setCountryComboBox.value)
        stage.show()
    }

    @Throws(IOException::class)
    fun editState() {
        if (setStateComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування області"
            alert.headerText = "Оберіть область для редагування"
            alert.showAndWait()
            return
        }
        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_state.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Зміна даних про область"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditStateController>()
        controller.initData(this, setStateComboBox.value)
        stage.show()
    }

    @Throws(SQLException::class)
    fun deleteState() {
        if (setStateComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення області"
            alert.headerText = "Оберіть область для видалення"
            alert.showAndWait()
            return
        }
        val name = Parser.processQuote(setStateComboBox.value)
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення області"
        alert.headerText = "Видалити \"$name\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query = "DELETE FROM state WHERE state.name = '$name'"
            QueryResult.updateDataBase(query)
            setStateComboBox()
        }
    }

    @Throws(IOException::class, SQLException::class)
    fun addRegion() {
        if (setCountryComboBox.value == CHOOSE_ALL || setStateComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка додавання району"
            alert.headerText = "Перевірте поля перед додаванням району"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_region.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додання нового району"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddRegionController>()
        controller.initData(this, setStateComboBox.value)
        stage.show()
    }

    @Throws(IOException::class)
    fun editRegion() {
        if (setRegionComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування району"
            alert.headerText = "Оберіть район для редагування"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_region.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Зміна даних про район"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditRegionController>()
        controller.initData(this, setRegionComboBox.value)
        stage.show()
    }

    @Throws(SQLException::class)
    fun deleteRegion() {
        if (setRegionComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення району"
            alert.headerText = "Оберіть район для видалення"
            alert.showAndWait()
            return
        }
        val name = Parser.processQuote(setRegionComboBox.value)
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення району"
        alert.headerText = "Видалити \"$name\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query = "DELETE FROM region WHERE region.name = '$name'"
            QueryResult.updateDataBase(query)
            setRegionComboBox()
        }
    }

    @Throws(IOException::class, SQLException::class)
    fun addCity() {
        if (setCountryComboBox.value == CHOOSE_ALL || setStateComboBox.value == CHOOSE_ALL || setRegionComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка додавання району"
            alert.headerText = "Перевірте поля перед додаванням району"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_city.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додання нового міста"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddCityController>()
        controller.initData(this, setRegionComboBox.value)
        stage.show()
    }

    @Throws(IOException::class, SQLException::class)
    fun editCity() {
        if (setCityComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування міста"
            alert.headerText = "Оберіть місто для редагування"
            alert.showAndWait()
            return
        }
        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_city.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Зміна даних про місто"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditEnterpriseController>()
        controller.initData(this, setCityComboBox.value)
        stage.show()
    }

    @Throws(SQLException::class)
    fun deleteCity() {
        if (setCityComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення міста"
            alert.headerText = "Оберіть місто для видалення"
            alert.showAndWait()
            return
        }
        val name = Parser.processQuote(setCityComboBox.value)
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення міста"
        alert.headerText = "Видалити \"$name\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query = "DELETE FROM city WHERE city.name = '$name'"
            QueryResult.updateDataBase(query)
            setCityComboBox()
        }
    }

    @Throws(IOException::class, SQLException::class)
    fun addEnterprise() {
        if (setCountryComboBox.value == CHOOSE_ALL || setStateComboBox.value == CHOOSE_ALL || setRegionComboBox.value == CHOOSE_ALL || setCityComboBox.value == CHOOSE_ALL) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка додавання підприємства"
            alert.headerText = "Перевірте поля перед додаванням підприємства"
            alert.showAndWait()
            return
        }
        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_enterprise.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додавання інформації про підприємства"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddEnterpriseController>()
        controller.initData(this, setCityComboBox.value)

        stage.show()
    }

    @Throws(IOException::class, SQLException::class)
    fun editEnterprise() {
        if (selectedRow == null) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування підприємства"
            alert.headerText = "Оберіть підприємство для редагування"
            alert.showAndWait()
            return
        }
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_enterprise.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Редагування підприємства"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditEnterpriseController>()
        controller.initData(this, selectedRow!!)
        stage.show()
    }

    @Throws(SQLException::class)
    fun deleteEnterprise() {
        if (selectedRow == null) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення підприємства"
            alert.headerText = "Оберіть підприємство для видалення"
            alert.showAndWait()
            return
        }

        val enterpriseInfo = ArrayList(arrayListOf(*selectedRow!!.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
        while (enterpriseInfo.size > 11) {
            enterpriseInfo.add(1, enterpriseInfo[1] + ", " + enterpriseInfo[2])
            enterpriseInfo.removeAt(2)
            enterpriseInfo.removeAt(2)
        }

        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення підприємства"
        alert.headerText = "Видалити \"${enterpriseInfo[2]}\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query =
                "DELETE FROM enterprise WHERE name = '" + enterpriseInfo[2] + "' AND adress = '" + enterpriseInfo[1] + "'"
            QueryResult.updateDataBase(query)
            setCity()
        }

    }

    @Throws(SQLException::class, IOException::class)
    fun excelExport() {
        val query =
            "SELECT enterprise.`index`, enterprise.adress, enterprise.name AS enterprise_name, enterprise.telephone, enterprise.fax, enterprise.email, city.name AS city_name, region.name AS region_name, state.name AS state_name, country.name AS country_name\n" +
                    "FROM enterprise\n" +
                    "INNER JOIN `city`\n" +
                    "ON enterprise.id_city = `city`.id\n" +
                    "INNER JOIN region\n" +
                    "ON `city`.id_region = region.id\n" +
                    "INNER JOIN state\n" +
                    "ON region.id_state = state.id\n" +
                    "INNER JOIN country\n" +
                    "ON state.id_country = country.id\n" +
                    "WHERE country.name " + localCountryValue + " AND state.name " + localStateValue + " AND region.name " + localRegionValue + " AND city.name " + localCityValue
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)

        val headerList = ArrayList<String>()
        headerList.add("Індекс")
        headerList.add("Адреса")
        headerList.add("Назва")
        headerList.add("Телефон")
        headerList.add("Факс")
        headerList.add("Email")
        headerList.add("Місто")
        headerList.add("Район")
        headerList.add("Область")
        headerList.add("Країна")

        val rowList = ArrayList<String>()
        rowList.add("index")
        rowList.add("adress")
        rowList.add("enterprise_name")
        rowList.add("telephone")
        rowList.add("fax")
        rowList.add("email")
        rowList.add("city_name")
        rowList.add("region_name")
        rowList.add("state_name")
        rowList.add("country_name")

        ExcelExport.export(resultSet, headerList, rowList, "Enterprise")
        statement.close()
        resultSet.close()
    }
}
