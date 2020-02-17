package thant.soft.thantsinaung.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Product(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0L,
        var name: String = "",
        var available: Boolean = false) {

        constructor(name: String, available: Boolean) : this()

}
