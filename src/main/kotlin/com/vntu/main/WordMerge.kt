package com.vntu.main

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.InputStream
import java.io.OutputStream
import java.util.ArrayList

class WordMerge(private val result: OutputStream) {
    private val inputs: MutableList<InputStream>
    private var first: XWPFDocument? = null

    init {
        inputs = ArrayList()
    }

    @Throws(Exception::class)
    fun add(stream: InputStream) {
        inputs.add(stream)
        val srcPackage = OPCPackage.open(stream)
        val src1Document = XWPFDocument(srcPackage)
        if (inputs.size == 1) {
            first = src1Document
        } else {
            val srcBody = src1Document.document.body
            first!!.document.addNewBody().set(srcBody)
        }
    }

    @Throws(Exception::class)
    fun doMerge() {
        first!!.write(result)
    }

    @Throws(Exception::class)
    fun close() {
        result.flush()
        result.close()
        for (input in inputs) {
            input.close()
        }
    }
}
