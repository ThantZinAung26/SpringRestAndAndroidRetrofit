package thant.soft.thantsinaung.service

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface FileService {
    fun storeFile(file: MultipartFile): String
    fun loadAll(): Stream<Path>
    fun load(fileName: String): Path
    fun loadAsResource(fileName: String): Resource
}