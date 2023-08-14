package com.hrblizz.fileapi.library.log

import com.hrblizz.fileapi.data.entities.Log
import com.hrblizz.fileapi.data.repository.LogRepository
import com.hrblizz.fileapi.services.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Logger {

    @Autowired
    private lateinit var logRepository: LogRepository

    fun info(logItem: LogItem) {
        write("info", logItem)
    }

    fun warning(logItem: LogItem) {
        write("warning", logItem)
    }

    fun error(logItem: LogItem) {
        write("error", logItem)
    }

    fun crit(logItem: LogItem) {
        write("crit", logItem)
        val log = Log(null, "crit", logItem)
        this.logRepository.save(log)
    }

    private fun write(logLevel: String, logItem: LogItem) {
        println("$logLevel: $logItem")
    }
}
