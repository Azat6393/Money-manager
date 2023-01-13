package com.woynex.parasayar.feature_trans.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woynex.parasayar.core.utils.RoomConstants
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = RoomConstants.TRANS_TABLE_NAME)
data class  Trans(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val trans_id: String,
    val amount: Double,
    val currency: String,
    val type: String,
    val account_name: String,
    val account_id: Int,
    val category: String? = null,
    val category_id: Int? = null,
    val subcategory: String? = null,
    val subcategory_id: Int? = null,
    val to_account_name: String? = null,
    val to_account_id: Int? = null,
    val fee_amount: Double? = null,
    val fee_trans_id: String? = null,
    val note: String,
    val photo: Bitmap? = null,
    val description: String,
    val date_in_millis: Long,
    val day: Int,
    val month: Int,
    val year: Int,
    val time: String
): Parcelable
