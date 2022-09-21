package com.woynex.parasayar.feature_settings.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithSubCategories(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val subCategoryList: List<SubCategory>
)
