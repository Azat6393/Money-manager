package com.woynex.parasayar.feature_trans.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithSubcategoryBudget(
    @Embedded val categoryBudget: CategoryBudget,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val subcategoryBudgetList: List<SubcategoryBudget>
)
