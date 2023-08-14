package com.hrblizz.fileapi.data.entities

import org.springframework.data.annotation.Id
import java.util.*

data class File(
    @Id val token: String = UUID.randomUUID().toString(),
    val name: String,
    val contentType: String,
    val meta: Map<Any, Any>,
    val source: Map<Any, Any>,
    val expireTime: Date?,
    val createTime: Date = Date(),
    val content: String
) {

}
