package com.lovebugs.auth.domain.entity;

import com.lovebugs.auth.domain.enums.Gender;
import com.lovebugs.auth.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(indexes = @Index(name = "idx_member_email", columnList = "email"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = true, updatable = true, insertable = true)
    private String profileImage;

    @Column(nullable = false, updatable = true, insertable = true)
    private String password;

    @Column(nullable = true, updatable = true, insertable = true)
    private String refreshToken;

    /* 회원가입만 한 경우를 고려하여 nullable */
    @Column(nullable = true, updatable = true, insertable = true)
    private LocalDateTime lastLoginDate;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Builder
    public Member(String name, String email, String profileImage, String password, String refreshToken, Gender gender, RoleType roleType) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.password = password;
        this.refreshToken = refreshToken;
        this.gender = gender;
        this.roleType = roleType;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now();
    }

    public void updateProfileImage(String src) {
        this.profileImage = src;
    }
}
