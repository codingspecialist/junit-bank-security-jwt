package shop.mtcoding.bank.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.user.UserRepository;

@Configuration
public class DummyDevInit {

    @Bean
    CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        return (args) -> {
            // 추가하기
        };
    }
}
