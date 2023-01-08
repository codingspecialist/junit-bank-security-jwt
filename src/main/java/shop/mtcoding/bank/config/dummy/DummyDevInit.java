package shop.mtcoding.bank.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject {

    @Bean
    CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        return (args) -> {
            User ssar = userRepository.save(newUser("ssar", "쌀"));
            User cos = userRepository.save(newUser("cos", "코스,"));
            User love = userRepository.save(newUser("love", "러브"));
            User admin = userRepository.save(newUser("admin", "관리자"));
            Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
            Account cosAccount = accountRepository.save(newAccount(2222L, cos));
            Account loveAccount = accountRepository.save(newAccount(3333L, love));
            Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));
            Transaction withdrawTransaction1 = transactionRepository
                    .save(newWithdrawTransaction(ssarAccount1, accountRepository));
            Transaction depositTransaction1 = transactionRepository
                    .save(newDepositTransaction(ssarAccount1, accountRepository));
            Transaction transferTransaction1 = transactionRepository
                    .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
            Transaction transferTransaction2 = transactionRepository
                    .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
            Transaction transferTransaction3 = transactionRepository
                    .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
        };
    }
}
