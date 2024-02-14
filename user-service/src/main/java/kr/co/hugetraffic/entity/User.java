package kr.co.hugetraffic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseDateTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(name = "user_encrypted_password")
    private String password;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_profile_img")
    private String profileImg;

    @Column(name = "user_greetings")
    private String greetings;

    @Column(name = "user_role")
    private String role;

    @Column(name = "user_disabled")
    private boolean disabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(role));
        return auth;
    }

    @Override
    public String getUsername() {
        return email;
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

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
