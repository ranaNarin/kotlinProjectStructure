package com.alpha.alphamoney.models

import com.alpha.alphamoney.models.User

data class UserResponse(
    val token: String,
    val user: User
)