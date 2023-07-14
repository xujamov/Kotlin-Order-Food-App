package com.xujamov.orderfood.data.models

data class OrderStatus(
    var bill_id: Int,
    var user_id: Int,
    var bill_phone: String?,
    var bill_address: String?,
    var bill_when: String,
    var bill_method: String,
    var bill_discount: Int,
    var bill_delivery: Int,
    var bill_total: Int,
    var bill_paid: Boolean,
    var bill_status: Int
)