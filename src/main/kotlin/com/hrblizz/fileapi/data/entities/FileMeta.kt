package com.hrblizz.fileapi.data.entities

import java.util.*

class FileMeta(
    var token: String,
    var filename: String,
    var size: Int,
    var contentType: String,
    var createTime: Date,
    var meta: Map<Any, Any>
)
