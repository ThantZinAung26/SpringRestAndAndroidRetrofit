package thant.soft.thantsinaung.model

import javax.persistence.*

@Entity
data class Product(
        var name: String,
        var description: String,
        var fileName: String,
        @Lob
        var file: ByteArray,
        var available: Boolean) {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0L

        /*var fileName: String = "test"

        @Lob
        var file: ByteArray = "Photo".toByteArray()*/

        constructor() : this("", "", "", "test".toByteArray(charset = Charsets.UTF_8),false)

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Product

                if (name != other.name) return false
                if (description != other.description) return false
                if (fileName != other.fileName) return false
                if (!file.contentEquals(other.file)) return false
                if (available != other.available) return false
                if (id != other.id) return false

                return true
        }

        override fun hashCode(): Int {
                var result = name.hashCode()
                result = 31 * result + description.hashCode()
                result = 31 * result + fileName.hashCode()
                result = 31 * result + file.contentHashCode()
                result = 31 * result + available.hashCode()
                result = 31 * result + id.hashCode()
                return result
        }

}
