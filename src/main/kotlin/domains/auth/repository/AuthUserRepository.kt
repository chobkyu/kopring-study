package org.example.domains.auth.repository

import okhttp3.internal.platform.Platform
import org.example.types.entitiy.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AuthUserRepository: JpaRepository<User, String> {
    fun existsByUsernameAndPlatform(username:String): Boolean

    @Modifying
    @Query("UPDATE User SET token = :accessToken WHERE username = :username")
    fun updateAccessTokenByUsername(
        @Param("username") username: String,
        @Param("accessToken") token: String
    )
}