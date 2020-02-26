package com.ceo26.sfu.repository

import com.ceo26.sfu.model.FileModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FileModelRepository : JpaRepository<FileModel, Long>