package thant.soft.thantsinaung.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Product(

        var name: String,
        var available: Boolean) {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0L

        constructor() : this("", false)

}
