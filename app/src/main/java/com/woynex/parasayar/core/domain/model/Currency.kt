package com.woynex.parasayar.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val cc: String,
    val name: String,
    val symbol: String
)