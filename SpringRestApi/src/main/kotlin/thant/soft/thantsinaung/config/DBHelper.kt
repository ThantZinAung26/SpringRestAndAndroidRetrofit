package thant.soft.thantsinaung.config

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import thant.soft.thantsinaung.model.Product
import thant.soft.thantsinaung.repository.ProductRepo

@Component
class DBHelper(private var productRepo: ProductRepo) : CommandLineRunner {

    @Throws(Exception::class)
    override fun run(vararg args: String?) {
        productRepo.deleteAll()
        val items = listOf(
                Product("Iphone", true),
                Product("Samsung A50", true),
                Product("Redmi K20", true),
                Product("Mi CC 9 Pro", true),
                Product("Macbook Pro 13\"", true)
        )
        productRepo.saveAll(items)
    }
}
