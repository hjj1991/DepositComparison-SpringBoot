package com.myhome.domain.user;


import com.myhome.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "tbl_user")
@Entity
public class UserEntity extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(length = 200, nullable = true)
    private String email;

    @Column(length = 300, nullable = true)
    private String userPw;

    @Column(nullable = true)
    private String picture;

    @Column(nullable = true)
    private String provider;

    @Column
    private LocalDateTime loginDateTime = LocalDateTime.now();

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name="userRole")
//    @Column(name = "role_name", nullable = false)
//    private List<String> userRoles;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'GUEST'")
    private Role role;

    @Builder
    public UserEntity(String userId, String name, String email, String picture, String userPw, Role role, String provider, LocalDateTime loginDateTime) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.userPw = userPw;
        this.role = role;
        this.provider = provider;
        this.loginDateTime = loginDateTime;
    }

    public UserEntity update(String name, String email, String picture, String userPw, Role role, LocalDateTime loginDateTime) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.userPw = userPw;
        this.role = role;
        this.loginDateTime = loginDateTime;

        return this;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.role.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(this.role.getKey()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userPw;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override   //계정이 만료가 안되었는지
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override   //계정이 잠겨있는지
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override   //계정 패스워드가 만료되었는지
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override   //계정이 사용가능한지
    public boolean isEnabled() {
        return true;
    }
}
