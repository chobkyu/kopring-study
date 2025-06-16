package org.example.types.entitiy
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user")
data class User(  // ← NoArg 플러그인 없으면 data class 피하는 게 낫습니다.
    @Id
    @Column(name = "ulid", length = 26, nullable = false)
    val ulid: String,

    @Column(name = "username", nullable = false, unique = true, length = 50)
    val username: String,

    @Column(name="access_token", length = 255)
    val accessToken: String? = null,

    @Column(name="created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "user")
    val accounts: List<Account> = mutableListOf(),
)