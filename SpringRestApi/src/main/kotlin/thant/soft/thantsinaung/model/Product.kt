package thant.soft.thantsinaung.model

import javax.persistence.*

@Entity
data class Product(
        var name: String,
        var description: String,
        var photoFilePath: String,
        var available: Boolean) {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0L

        /*var fileName: String = "test"

        @Lob
        var file: ByteArray = "Photo".toByteArray()*/

        constructor() : this("", "", "", false)

}
