package com.vntu.directories

import com.vntu.add.AddVntuDepController
import com.vntu.edit.EditVntuDepController
import com.vntu.main.OpenNewWindow
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TableView
import javafx.scene.image.Image
import javafx.stage.Stage
import java.io.IOException
import java.sql.SQLException
import java.util.Objects

class WorkVntuController {

    lateinit var resultTable: TableView<ObservableList<String>>
    private val window = OpenNewWindow()
    private var name: String? = null


    @Throws(SQLException::class)
    fun initialize() {
        val query = "SELECT vntu.name FROM vntu"
        resultTable = QueryResult.getTableResult(resultTable, query)
        resultTable.selectionModel.selectedItemProperty().addListener { _, _, newSelection ->
            if (newSelection != null) {
                name = resultTable.selectionModel.selectedItem.toString()
            }
        }
    }

    @Throws(IOException::class)
    fun editVntuDep() {
        if (name == null) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка редагування"
            alert.headerText = "Оберіть підрозділ"
            alert.showAndWait()
            return
        }

        name = Parser.processBrackets(name!!)
        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_vntu_dep.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Редагування підрозділів ВНТУ"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<EditVntuDepController>()
        controller.initData(resultTable, name!!)
        stage.show()
    }

    @Throws(IOException::class)
    fun addVntuDep() {
        window.openWindow("fxml/add_vntu_dep.fxml", "Додання підрозділів ВНТУ")
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_vntu_dep.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додання підрозділів ВНТУ"
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        val controller = fxmlLoader.getController<AddVntuDepController>()
        controller.initData(resultTable)
        stage.show()
    }

    @Throws(SQLException::class)
    fun deleteDep() {
        if (name == null) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Помилка видалення"
            alert.headerText = "Оберіть підрозділ"
            alert.showAndWait()
            return
        }

        name = name!!.substring(1, name!!.length - 1)
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення запису"
        alert.headerText = "Видалити \"$name\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            var query = "DELETE FROM vntu WHERE vntu.name = '$name'"
            QueryResult.updateDataBase(query)
            query = "SELECT vntu.name FROM vntu"
            resultTable = QueryResult.getTableResult(resultTable, query)
        }
    }
}
