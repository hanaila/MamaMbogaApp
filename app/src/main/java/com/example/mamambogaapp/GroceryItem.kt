package com.example.mamambogaapp

data class GroceryItem(
    val id: Int,
    val name: String,
    val price: Double,
    val unit: String,
    val emoji: String,
    var isSelected: Boolean = false,
    var quantity: Int = 1
)
