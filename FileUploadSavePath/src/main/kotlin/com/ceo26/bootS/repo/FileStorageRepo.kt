package com.ceo26.bootS.repo

import com.ceo26.bootS.model.FileProperties
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface FileStorageRepo : JpaRepository<FileProperties, Long> {
}
