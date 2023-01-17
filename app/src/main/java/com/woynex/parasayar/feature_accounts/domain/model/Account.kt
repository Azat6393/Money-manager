package com.woynex.parasayar.feature_accounts.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(
    val id: Int? = null,
    val name: String,
    val deposit: Double,
    val withdrawal: Double,
    val group_name: String,
    val group_id: Int
): Parcelable