package com.lovebugs.auth.domain.entity;

import com.lovebugs.auth.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(indexes = @Index(name = "idx_member_email", columnList = "email"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity implements UserDetails {
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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Builder
    public Member(String name, String email, String profileImage, String password, String refreshToken, Gender gender, List<String> roles) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.password = password;
        this.refreshToken = refreshToken;
        this.gender = gender;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now();
    }
}
