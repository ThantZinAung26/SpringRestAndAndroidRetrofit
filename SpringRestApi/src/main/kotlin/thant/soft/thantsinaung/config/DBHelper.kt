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
                Product("Iphone", "New model.", false),
                Product("Samsung A50","Amoled display", false),
                Product("Redmi K20","popup back camera", false),
                Product("Mi CC 9 Pro", "mid range phone", false),
                Product("Macbook Pro 13\"", "new model 2018 late.", true)
        )
        productRepo.saveAll(items)
    }
}
