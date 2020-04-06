package thant.soft.thantsinaung.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import thant.soft.thantsinaung.model.Product
import thant.soft.thantsinaung.model.ResponseEntityStatus
import thant.soft.thantsinaung.repository.ProductRepo
import thant.soft.thantsinaung.service.FileService

@RestController
@RequestMapping("/api")
class ProductController(private var productRepo: ProductRepo) {

    @Autowired
    lateinit var  fileService: FileService

    @GetMapping("/products")
    fun getAllProduct(): MutableIterable<Product> {
        return productRepo.findAll()
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    fun downloadFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val resource = fileService.loadAsResource(filename)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.filename + "\"")
                .body(resource)
    }

    @PostMapping("/upload")
    @ResponseBody
    fun uploadFile(@RequestParam("file") file: MultipartFile, @RequestParam id: Long):
            ResponseEntity<ResponseEntityStatus<Product>> {
        val item = productRepo.findById(id)
        return when {
            item.isPresent -> {
                val name = fileService.storeFile(file)
                val uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/download/")
                        .path(name)
                        .toUriString()
                item.get().photoFilePath = uri
                productRepo.save(item.get())
                return ResponseEntity(ResponseEntityStatus(
                        true,
                        "Add Product item to your list",
                        item.get()
                ), HttpStatus.CREATED)
            }
            else -> {
                ResponseEntity(ResponseEntityStatus(false, "could not find product", item.get()), HttpStatus.NOT_FOUND)
            }
        }


    }

    @PutMapping("/add")
    @ResponseBody
    fun addProduct(@RequestBody product: Product): ResponseEntity<ResponseEntityStatus<Product>> {
        val item = productRepo.save(product)
        return ResponseEntity(ResponseEntityStatus(
                true,
                "Add Product item to your list",
                product
        ), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<ResponseEntityStatus<Boolean>> {
        val product = productRepo.findById(id)
        return when {
            product.isPresent -> {
                ResponseEntity(ResponseEntityStatus(
                        true,
                        "Delete product ${product.get().name} from list", true
                ), HttpStatus.OK)
            }
            else -> {
                ResponseEntity(ResponseEntityStatus(
                        false,
                        "Sorry! could not find product to delete", false
                ), HttpStatus.NOT_FOUND)
            }
        }
    }

    @PostMapping("/available")
    fun toggleProductAvailable(@RequestBody availability: Boolean, @RequestParam id: Long):
            ResponseEntity<ResponseEntityStatus<Boolean>> {

        val product = productRepo.findById(id)
        return when {
            product.isPresent -> {
                product.get().available = availability
                productRepo.save(product.get())
                ResponseEntity(ResponseEntityStatus(true, "product ${product.get().name} is now " +
                        "${if (availability) "In Stock" else "Sold Out"} ", true), HttpStatus.OK)
            }
            else -> {
                ResponseEntity(ResponseEntityStatus(false, "could not find product", false), HttpStatus.NOT_FOUND)
            }
        }
    }

}
