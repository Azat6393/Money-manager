package com.woynex.parasayar.feature_settings.domain.use_case

data class CategoryUseCases(
    val insertCategory: InsertCategory,
    val updateCategory: UpdateCategory,
    val deleteCategory: DeleteCategory,
    val getCategories: GetCategories
)