package com.muratozturk.orderfood.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Categories(
    @SerializedName("id") @Expose var id: UUID,
    @SerializedName("name") @Expose var name: String,
    @SerializedName("imagePath") @Expose var imagePath: String
):Serializable