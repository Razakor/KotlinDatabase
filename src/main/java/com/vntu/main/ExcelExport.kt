package com.vntu.main

import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.awt.*
import java.io.*
import java.sql.ResultSet
import java.sql.SQLException
import java.util.ArrayList

object ExcelExport {

    @Throws(IOException::class, SQLException::class)
    fun export(resultSet: ResultSet, headerList: ArrayList<String>, rowList: ArrayList<String>, name: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet(name)
        val rowHead = sheet.createRow(0)

        for (j in headerList.indices) {
            rowHead.createCell(j).setCellValue(headerList[j])
        }

        var i = 1
        while (resultSet.next()) {
            val row = sheet.createRow(i)
            for (j in rowList.indices) {
                row.createCell(j).setCellValue(resultSet.getString(rowList[j]))
            }
            i++
        }

        for (j in rowList.indices) {
            sheet.autoSizeColumn(j)
        }

        val fileOut = FileOutputStream("$name.xlsx")
        workbook.write(fileOut)
        Desktop.getDesktop().open(File("$name.xlsx"))
        fileOut.close()
    }
}
