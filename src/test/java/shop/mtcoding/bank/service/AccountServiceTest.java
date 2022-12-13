package shop.mtcoding.bank.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.account.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

}
