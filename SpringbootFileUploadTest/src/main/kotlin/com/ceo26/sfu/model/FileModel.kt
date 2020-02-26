package com.ceo26.sfu.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
data class FileModel(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0L,
        var fileName: String = "",
        var fileData: ByteArray = "Test".toByteArray()
) {

    constructor(fileName: String, fileData: ByteArray) : this() {
        this.fileName = fileName
        this.fileData = fileData
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileModel

        if (id != other.id) return false
        if (fileName != other.fileName) return false
        if (!fileData.contentEquals(other.fileData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + fileData.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "FileModel(id=$id, fileName='$fileName', fileData=${fileData.contentToString()})"
    }
}