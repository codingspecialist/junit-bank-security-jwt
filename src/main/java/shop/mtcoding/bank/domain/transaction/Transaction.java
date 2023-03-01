package shop.mtcoding.bank.domain.transaction;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import shop.mtcoding.bank.domain.account.Account;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction_tb")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account withdrawAccount; // 출금 계좌

    @ManyToOne(fetch = FetchType.LAZY)
    private Account depositAccount; // 입금 계좌

    @Column(nullable = false)
    private Long amount; // 금액

    private Long withdrawAccountBalance; // 거래후 잔액
    private Long depositAccountBalance; // 거래후 잔액

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionEnum gubun; // 입금(ATM으로 부터), 출금(ATM으로), 이체(다른계좌로)

    private String sender; // 계좌가 사라져도 로그는 남아야 한다.
    private String receiver; // 계좌가 사라져도 로그는 남아야 한다.
    private String tel; // 무통장 입금시 전화번호 필요함 (입금이 잘못될 경우 연락처가 필요하다)

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount,
            Long withdrawAccountBalance, Long depositAccountBalance, TransactionEnum gubun, String sender,
            String receiver, String tel, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.gubun = gubun;
        this.sender = sender;
        this.receiver = receiver;
        this.tel = tel;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

}
