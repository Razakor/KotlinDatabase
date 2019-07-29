package com.vntu.main

import org.docx4j.openpackaging.exceptions.Docx4JException
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.ContentAccessor
import org.docx4j.wml.Text

import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException
import java.io.*
import java.util.ArrayList
import java.util.HashMap

class Docx4j {

    @Throws(Docx4JException::class, FileNotFoundException::class)
    private fun getTemplate(name: String): WordprocessingMLPackage {
        return WordprocessingMLPackage
            .load(FileInputStream(File(name)))
    }

    @Throws(IOException::class, Docx4JException::class)
    private fun writeDocxToStream(
        template: WordprocessingMLPackage,
        target: String
    ) {

        File(outputFilePath!!).mkdirs()

        val f = File(target)

        template.save(f)
    }

    companion object {

        private var filePaths: List<String>? = null
        private var outputFilePath: String? = null

        fun initializePaths(input: List<String>, output: String) {

            filePaths = input
            outputFilePath = output + "output\\" // TODO: Delete output if it exists

        }

        @Throws(Docx4JException::class, IOException::class, JAXBException::class)
        fun start(
            nameList: List<String>, instituteList: List<String>, groupList: List<String>,
            enterpriseList: List<String>, courseList: List<String>, specialityList: List<String>,
            idList: List<String>, index: Int
        ) {
            // TODO: Add arguments size comparison to be sure
            val docx4j = Docx4j()


            val template = docx4j.getTemplate(filePaths!![nameList.size - 1])


            val texts = getAllElementFromObject(
                template.getMainDocumentPart(), Text::class.java
            )
            searchAndReplace(texts, object : HashMap<String, String>() {
                init {


                    for (i in nameList.indices) {

                        this["\${name$i}"] = nameList[i]
                        this["\${inst$i}"] = instituteList[i]
                        this["\${group$i}"] = groupList[i]
                        this["\${enterprise$i}"] = enterpriseList[i]
                        this["\${course$i}"] = courseList[i]
                        this["\${speciality$i}"] = specialityList[i]
                        this["\${id$i}"] = idList[i]

                    }


                }

            })

            docx4j.writeDocxToStream(template, "$outputFilePath$index.docx")
        }

        private fun searchAndReplace(texts: List<Any>, values: Map<String, String>) {

            // -- scan all expressions
            // Will later contain all the expressions used though not used at the moment
            val els = ArrayList<String>()

            var sb = StringBuilder()
            val PASS = 0
            val PREPARE = 1
            val READ = 2
            var mode = PASS

            // to nullify
            val toNullify = ArrayList<IntArray>()
            var currentNullifyProps: IntArray? = IntArray(4)

            // Do scan of els and immediately insert value
            for (i in texts.indices) {
                val text = texts[i]
                val textElement = text as Text
                var newVal = ""
                val v = textElement.getValue()
                var textSofar = StringBuilder()
                var extra = 0
                val vchars = v.toCharArray()
                for (col in vchars.indices) {
                    val c = vchars[col]
                    textSofar.append(c)
                    when (c) {
                        '$' -> {
                            mode = PREPARE
                            sb.append(c)
                        }
                        '{' -> {
                            if (mode == PREPARE) {
                                sb.append(c)
                                mode = READ
                                currentNullifyProps?.set(0, i)
                                currentNullifyProps?.set(1, col + extra - 1)
                            } else {
                                if (mode == READ) {
                                    // consecutive opening curl found. just read it
                                    // but supposedly throw error
                                    sb = StringBuilder()
                                    mode = PASS
                                }
                            }
                        }
                        '}' -> {
                            run {
                                if (mode == READ) {
                                    mode = PASS
                                    sb.append(c)
                                    els.add(sb.toString())
                                    newVal += textSofar.toString() + if (null == values[sb.toString()]) sb.toString() else values[sb.toString()]
                                    textSofar = StringBuilder()
                                    currentNullifyProps?.set(2, i)
                                    currentNullifyProps?.set(3, col + extra)
                                    toNullify.add(currentNullifyProps!!)
                                    currentNullifyProps = IntArray(4)
                                    extra += sb.toString().length
                                    sb = StringBuilder()
                                } else if (mode == PREPARE) {
                                    mode = PASS
                                    sb = StringBuilder()
                                }
                            }

                        }
                        else -> {
                            if (mode == READ)
                                sb.append(c)
                            else if (mode == PREPARE) {
                                mode = PASS
                                sb = StringBuilder()
                            }
                        }
                    }
                }
                newVal += textSofar.toString()
                textElement.value = newVal
            }

            // remove original expressions
            if (toNullify.size > 0)
                for (i in texts.indices) {
                    if (toNullify.size == 0) break
                    currentNullifyProps = toNullify[0]
                    val text = texts[i]
                    val textElement = text as Text
                    val v = textElement.value
                    val nvalSB = StringBuilder()
                    val textChars = v.toCharArray()
                    for (j in textChars.indices) {
                        val c = textChars[j]
                        if (null == currentNullifyProps) {
                            nvalSB.append(c)
                            continue
                        }
                        // I know 100000 is too much!!! And so what???
                        val floor = currentNullifyProps!![0] * 100000 + currentNullifyProps!![1]
                        val ceil = currentNullifyProps!![2] * 100000 + currentNullifyProps!![3]
                        val head = i * 100000 + j
                        if (head !in floor..ceil) {
                            nvalSB.append(c)
                        }

                        if (j > currentNullifyProps!![3] && i >= currentNullifyProps!![2]) {
                            toNullify.removeAt(0)
                            if (toNullify.size == 0) {
                                currentNullifyProps = null
                                continue
                            }
                            currentNullifyProps = toNullify[0]
                        }
                    }
                    textElement.value = nvalSB.toString()
                }
        }

        private fun getAllElementFromObject(obj: Any, toSearch: Class<*>): List<Any> {
            var obj = obj
            val result = ArrayList<Any>()
            if (obj is JAXBElement<*>)
                obj = obj.value

            if (obj.javaClass == toSearch)
                result.add(obj)
            else if (obj is ContentAccessor) {
                val children = (obj).content
                for (child in children) {
                    result.addAll(getAllElementFromObject(child, toSearch))
                }

            }
            return result
        }
    }
}

