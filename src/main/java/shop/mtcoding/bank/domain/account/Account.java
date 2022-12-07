package shop.mtcoding.bank.domain.account;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.bank.domain.user.User;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account")
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long number; // 계좌번호

    @Column(nullable = false)
    private String password; // 계좌비밀번호

    @Column(nullable = false)
    private Long balance; // 잔액 (디폴트 값 1000원)

    @Column(nullable = false)
    private Boolean isActive; // 계좌 활성화 여부

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Account(Long id, Long number, String password, Long balance, Boolean isActive, User user,
            LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.isActive = isActive;
        this.user = user;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
