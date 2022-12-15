package shop.mtcoding.bank.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.bank.handler.ex.CustomApiException;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;
    @Column(nullable = false, length = 60) // 패스워드 인코딩하면 길어짐
    private String password;
    @Column(nullable = false, length = 20)
    private String email;
    @Column(nullable = false, length = 20)
    private String fullname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public User(Long id, String username, String password, String email, String fullname, UserEnum role,
            LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.role = role;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public void checkCurrentPasswordIsNotSame(String currentPassword, BCryptPasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(currentPassword, this.password)) {
            throw new CustomApiException("패스워드 인증에 실패하였습니다");
        }
    }

    public void checkNewPasswordIsSame(String newEncPassword, BCryptPasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(newEncPassword, this.password)) {
            throw new CustomApiException("새로운 패스워드가 현재 패스워드와 동일합니다");
        }
    }

    public void updatePassword(String currentPassword, String newPassword,
            BCryptPasswordEncoder passwordEncoder) {
        checkCurrentPasswordIsNotSame(currentPassword, passwordEncoder);
        checkNewPasswordIsSame(newPassword, passwordEncoder);
        this.password = passwordEncoder.encode(newPassword);
    }

}
