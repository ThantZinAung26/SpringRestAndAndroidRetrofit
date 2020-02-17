package thant.soft.thantsinaung.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import thant.soft.thantsinaung.model.Product
import thant.soft.thantsinaung.model.ResponseEntityStatus
import thant.soft.thantsinaung.repository.ProductRepo

@RestController
@RequestMapping("/api/product")
class ProductController(private var productRepo: ProductRepo) {

    @GetMapping("/products")
    fun getAllProduct(): MutableIterable<Product> {
        return productRepo.findAll()
    }

    @PutMapping("/add")
    fun addProduct(@RequestBody product: Product): ResponseEntity<ResponseEntityStatus<Product>> {
        val item = productRepo.save(product)
        return ResponseEntity(ResponseEntityStatus(
                true,
                "Add Product item to your list",
                item
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
    fun toggleProductAvailable(@RequestBody availability: Boolean,@RequestParam id: Long):
    ResponseEntity<ResponseEntityStatus<Boolean>>{

        val product = productRepo.findById(id)
        return when {
            product.isPresent -> {
                product.get().available = availability
                productRepo.save(product.get())
                ResponseEntity(ResponseEntityStatus(true, "product ${product.get().name} is now " +
                        "${if(availability) "available" else "finished"} ", true), HttpStatus.OK)
            }
            else -> {
                ResponseEntity(ResponseEntityStatus(false, "could not find product",false), HttpStatus.NOT_FOUND)
            }
        }

    }

}
