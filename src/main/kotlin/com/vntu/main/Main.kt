package com.vntu.main

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.util.Objects

class Main : Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        DataBaseConnection.connectToDB()
        val root = FXMLLoader.load<Parent>(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/main_form.fxml")))
        primaryStage.title = "СУБД \"Виробнича Практика\""
        primaryStage.scene = Scene(root, primaryStage.width, primaryStage.height)
        primaryStage.icons.add(Image("pictures/ico.jpg"))
        primaryStage.show()
    }
}
