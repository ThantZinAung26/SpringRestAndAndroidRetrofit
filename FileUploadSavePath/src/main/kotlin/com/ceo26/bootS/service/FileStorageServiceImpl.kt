package com.ceo26.bootS.service

import com.ceo26.bootS.exception.FileNotFoundException
import com.ceo26.bootS.exception.StorageException
import com.ceo26.bootS.model.FileProperties
import com.ceo26.bootS.repo.FileStorageRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream
import javax.annotation.PostConstruct

@Service
class FileStorageServiceImpl @Autowired constructor(fileProperties: FileProperties) : FilePropertiesService {

    @Autowired
    lateinit var repo: FileStorageRepo

    private var rootLocation: Path = Paths.get(fileProperties.location)

    @PostConstruct
    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (ex: IOException) {
            throw StorageException("Could not initialize storage location", ex)
        }
    }

    override fun storeFile(file: MultipartFile): String {
        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        try {
            if (file.isEmpty) throw StorageException("Failed to store empty file $filename")
            if (filename.contains("..")) throw StorageException("Cannot store file with relative path outside current directory $filename")

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING)
            }

            val fileStoragePath = FileProperties(load(filename).toUri().toString())
            repo.save(fileStoragePath)
            print(UrlResource(load(filename).toUri()))

        } catch (e: IOException) {
            throw StorageException("")
        }
        return filename
    }

    override fun loadAll(): Stream<Path> {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter { path -> path != this.rootLocation }
                    .map(this.rootLocation::relativize)
        } catch (e: IOException) {
            throw StorageException("Failed to read file ", e)
        }
    }

    override fun load(filename: String): Path {
        println(this.rootLocation.resolve(filename))
        return this.rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource {
        try {
            val file = load(filename)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                return resource
            } else {
                throw FileNotFoundException("Could not read file $filename")
            }
        } catch (e: MalformedURLException) {
            throw FileNotFoundException("Could not read file $filename", e)
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }
}