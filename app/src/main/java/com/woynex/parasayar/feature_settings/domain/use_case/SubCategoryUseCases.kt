package com.woynex.parasayar.feature_settings.domain.use_case

data class SubCategoryUseCases(
    val insertSubCategory: InsertSubCategory,
    val updateSubCategory: UpdateSubCategory,
    val deleteSubCategory: DeleteSubCategory,
    val getSubCategories: GetSubCategories
)
