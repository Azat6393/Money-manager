package com.woynex.parasayar.core.utils

import android.annotation.SuppressLint
import java.util.*

object RoomConstants {
    const val TRANS_TABLE_NAME = "trans"
    const val CATEGORY_TABLE_NAME = "category"
    const val ACCOUNT_TABLE_NAME = "account"
    const val ACCOUNT_GROUP_TABLE_NAME = "account_group"
    const val SUB_CATEGORY_TABLE_NAME = "subcategory"


    @SuppressLint("ConstantLocale")
    val PARA_SAYAR_DEFAULT_DB =
        when (Locale.getDefault().language) {
            "en" -> "database/accout_group.db"
            else -> "database/accout_group.db"
        }
}