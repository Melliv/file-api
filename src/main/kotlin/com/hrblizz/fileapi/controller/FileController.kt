package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.data.entities.FileMetasReq
import com.hrblizz.fileapi.rest.ErrorMessage
import com.hrblizz.fileapi.rest.ResponseEntity
import com.hrblizz.fileapi.services.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse


@RestController
class FileController() {

    @Autowired
    private lateinit var fileService: FileService

    @RequestMapping("/files/metas", method = [RequestMethod.POST])
    fun getFileMetas(@RequestBody requestData: FileMetasReq): ResponseEntity<Map<String, Any>> {
        val tokenMetas = fileService.getFileMetas(requestData)

        return ResponseEntity(
            mapOf(
                "files" to tokenMetas
            ),
            HttpStatus.OK.value()
        )
    }

    @RequestMapping("/files", method = [RequestMethod.POST])
    @ResponseStatus(HttpStatus.CREATED)
    fun createFile(@RequestBody file: File): ResponseEntity<Map<String, Any>> {
        fileService.createFile(file)

        return ResponseEntity(
            mapOf(
                "token" to file.token
            ),
            HttpStatus.CREATED.value()
        )
    }

    @RequestMapping("/file/{token}", method = [RequestMethod.GET])
    @ResponseBody
    fun getFile(@PathVariable("token") token: String, response: HttpServletResponse): Any {
        val fileOpt = fileService.getFile(token)

        if (fileOpt === Optional.empty<File>()) {
            response.status = HttpStatus.BAD_REQUEST.value()
            val errorMessage = ErrorMessage("No file with that token!")
            return ResponseEntity(null, listOf(errorMessage), HttpStatus.BAD_REQUEST.value())
        }

        val file = fileOpt.get()
        response.setHeader("X-Filename", "${file.name}.${file.contentType}")
        response.setHeader("X-Filesize", file.content.length.toString())
        response.setHeader("X-CreateTime", file.createTime.toString())
        response.setHeader("Content-Type", "application/${file.contentType}")
        return file.content
    }

    @RequestMapping("/file/{token}", method = [RequestMethod.DELETE])
    @ResponseBody
    fun deleteFile(@PathVariable("token") token: String): ResponseEntity<Any> {
        val deleted = fileService.deleteFile(token)

        if (!deleted) {
            val errorMessage = ErrorMessage("No file with that token to delete!")
            return ResponseEntity(null, listOf(errorMessage), HttpStatus.BAD_REQUEST.value())
        }
        return ResponseEntity(HttpStatus.OK.value())
    }
}
