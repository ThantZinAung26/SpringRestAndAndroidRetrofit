package thant.soft.thantsinaung.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import thant.soft.thantsinaung.model.Product

@Repository
interface ProductRepo : CrudRepository<Product, Long>
