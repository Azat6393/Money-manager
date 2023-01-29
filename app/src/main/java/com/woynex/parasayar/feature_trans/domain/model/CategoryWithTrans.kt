package com.woynex.parasayar.feature_trans.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.woynex.parasayar.feature_settings.domain.model.Category

data class CategoryWithTrans(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val transList: List<Trans>
)