package com.vntu.main

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.io.IOException
import java.util.Objects

class OpenNewWindow {

    @Throws(IOException::class)
    fun openWindow(fxmlName: String, windowName: String) {
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource(fxmlName)))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = windowName
        stage.scene = Scene(root)
        stage.icons.add(Image("pictures/ico.jpg"))
        stage.show()
    }

}
