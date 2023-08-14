package com.hrblizz.fileapi.data.entities

import com.hrblizz.fileapi.library.log.LogItem
import org.springframework.data.annotation.Id

data class Log(
    @Id var id: String?,
    var logLevel: String,
    var logItem: LogItem,
)
