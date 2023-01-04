package shop.mtcoding.bank.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionEnum;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

public class DummyObject {
    protected User newUser(String username, String fullname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        User user = User.builder()
                .username(username)
                .password(encPassword)
                .email(username + "@nate.com")
                .fullname(fullname)
                .role(username.equals("admin") ? UserEnum.ADMIN : UserEnum.CUSTOMER)
                .build();
        return user;
    }

    protected User newMockUser(Long id, String username, String fullname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        User user = User.builder()
                .id(id)
                .username(username)
                .password(encPassword)
                .email(username + "@nate.com")
                .fullname(fullname)
                .role(username.equals("admin") ? UserEnum.ADMIN : UserEnum.CUSTOMER)
                .build();
        return user;
    }

    protected Account newAccount(Long number, User user) {
        Account account = Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .user(user)
                .build();
        return account;
    }

    protected Account newMockAccount(Long id, Long number, Long balance, User user) {
        Account account = Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return account;
    }

    protected Transaction newMockDepositTransaction(Long id, Account account) {
        Transaction transaction = Transaction.builder()
                .id(id)
                .depositAccount(account)
                .withdrawAccount(null)
                .depositAccountBalance(account.getBalance())
                .withdrawAccountBalance(null)
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .reciver(account.getNumber() + "")
                .tel("010-2222-7777")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return transaction;
    }

    protected Transaction newMockWithdrawTransaction(Long id, Account account) {
        Transaction transaction = Transaction.builder()
                .id(id)
                .depositAccount(null)
                .withdrawAccount(account)
                .depositAccountBalance(null)
                .withdrawAccountBalance(account.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.WITHDRAW)
                .sender(account.getNumber() + "")
                .reciver("ATM")
                .tel("010-2222-7777")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return transaction;
    }
}
