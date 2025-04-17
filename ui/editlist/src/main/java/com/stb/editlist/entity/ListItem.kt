package com.stb.editlist.entity

import java.util.Random

data class ListItem (
    val id: Int = Random().nextInt(),
    val title: String = "",
    val quantityOrWeight: Double = 1.0,
    val pricePerUnit: Double = 0.0,
)
