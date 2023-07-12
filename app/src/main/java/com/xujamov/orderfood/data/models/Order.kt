package com.xujamov.orderfood.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Order(
    @SerializedName("id") @Expose var id: UUID?,
    @SerializedName("portions") @Expose var portion: String?
):Serializable