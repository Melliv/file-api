package com.hrblizz.fileapi.services;

import com.hrblizz.fileapi.data.entities.File;
import com.hrblizz.fileapi.data.entities.FileMeta;
import com.hrblizz.fileapi.data.entities.FileMetasReq
import com.hrblizz.fileapi.data.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import kotlin.collections.HashMap


@Service
class FileService {

    @Autowired
    private lateinit var fileRepository: FileRepository;

    fun createFile(file: File) {
        this.fileRepository.save(file);
    }

    fun getFile(token: String): Optional<File> {
        return this.fileRepository.findById(token);
    }

    fun deleteFile(token: String): Boolean {
        val exist = this.fileRepository.existsById(token);
        if (!exist) return false
        this.fileRepository.deleteById(token);
        return true
    }

    fun getFileMetas(requestData: FileMetasReq): Map<String, FileMeta> {
        val files = this.fileRepository.findAllById(requestData.tokens)
        val tokenMetas = HashMap<String, FileMeta>()

        val mutableIterator = files.iterator()
        while (mutableIterator.hasNext()) {
            val file = mutableIterator.next();
            val fileMeta = FileMeta(
                file.token,
                file.name,
                file.content.length,
                file.contentType,
                file.createTime,
                file.meta
            );
            tokenMetas[fileMeta.token] = fileMeta;
            mutableIterator.remove()
        }
        return tokenMetas;
    }


}
