package org.example.types.entitiy

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "account")
data class Account(  // ← NoArg 플러그인 없으면 data class 피하는 게 낫습니다.
    @Id
    @Column(name = "ulid", length = 26, nullable = false)
    val ulid: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ulid", nullable = false)
    val user: User,

    @Column(name="balance", nullable = false, precision = 15, scale = 2)
    var balance: BigDecimal = BigDecimal.ZERO,

    @Column(name="account_number", nullable = false, length = 100, unique = true)
    var account_number: String,  // ← 오타 수정

    @Column(name="is_deleted", nullable = false)
    val isDeleted: Boolean = false,

    @Column(name="created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name="updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
