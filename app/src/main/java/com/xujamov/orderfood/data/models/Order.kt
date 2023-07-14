package com.xujamov.orderfood.data.models

import java.util.UUID

data class Order(
    var bill_id: UUID?,
    var food_id: UUID?,
    var item_qty: Int = 1
)