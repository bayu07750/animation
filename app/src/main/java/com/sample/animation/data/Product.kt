package com.sample.animation.data

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDate

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val specs: ImmutableList<String>,
    val color: Color,
    val stock: Int = 10,
    val estimatedDelivery: LocalDate = LocalDate.now().plusDays(3)
)
