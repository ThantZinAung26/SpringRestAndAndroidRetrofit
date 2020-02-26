package com.ceo26.sfu.service

import com.ceo26.sfu.exception.FileNotFoundException
import com.ceo26.sfu.exception.FileStorageException
import com.ceo26.sfu.model.FileModel
import com.ceo26.sfu.repository.FileModelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Service
class FileModelService{

    @Autowired
    lateinit var repository: FileModelRepository

    fun storeFile(file: MultipartFile): FileModel {
        val fileName = StringUtils.cleanPath(file.originalFilename.toString())

        try {
            if (fileName.contains("..")) {
                throw FileStorageException("Sorry! Filename contains invalid path sequence $fileName")
            }

            val fileModel = FileModel(fileName, file.bytes)
            return repository.save(fileModel)
        } catch (ex: IOException) {
            throw FileStorageException("Could not store file $fileName. !Please try again.", ex)
        }
    }

    fun getFile(fileId: Long): FileModel {
        return repository.findById(fileId)
                .orElseThrow { FileNotFoundException("File not found with id $fileId") }
    }

}