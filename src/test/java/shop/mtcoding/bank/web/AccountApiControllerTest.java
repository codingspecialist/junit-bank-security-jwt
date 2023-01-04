package shop.mtcoding.bank.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountDepositReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountTransferReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountWithdrawReqDto;

@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql") // teardown
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class AccountApiControllerTest extends DummyObject {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        User cos = userRepository.save(newUser("cos", "코스,"));
        User love = userRepository.save(newUser("love", "러브"));
        User admin = userRepository.save(newUser("admin", "관리자"));
        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, love));
    }

    // 계좌 비밀번호 BCrypt 인코딩 (숙제)
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void saveAccount_test() throws Exception {
        // given
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(9999L);
        accountSaveReqDto.setPassword(1234L);

        String requestBody = om.writeValueAsString(accountSaveReqDto);
        log.debug("디버그 : " + requestBody);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/s/account").content(requestBody).contentType(APPLICATION_JSON_UTF8));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("디버그 : " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());

    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findUserAccount_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/api/s/account/login-user"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("디버그 : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void deleteAccount_test() throws Exception {
        // given
        Long accountNumber = 1111L;

        // when
        ResultActions resultActions = mvc
                .perform(delete("/api/s/account/" + accountNumber));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("디버그 : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());

    }

    @Test
    public void depositAccount_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("010-2222-6666");

        String requestBody = om.writeValueAsString(accountDepositReqDto);
        log.debug("디버그 : " + requestBody);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/account/deposit").content(requestBody).contentType(APPLICATION_JSON_UTF8));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("디버그 : " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void withdrawAccount_test() throws Exception {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setGubun("WITHDRAW");

        String requestBody = om.writeValueAsString(accountWithdrawReqDto);
        log.debug("디버그 : " + requestBody);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/s/account/withdraw").content(requestBody).contentType(APPLICATION_JSON_UTF8));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("디버그 : " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void transferAccount_test() throws Exception {
        // given
        AccountTransferReqDto transferReqDto = new AccountTransferReqDto();
        transferReqDto.setWithdrawNumber(1111L);
        transferReqDto.setDepositNumber(2222L);
        transferReqDto.setAmount(100L);
        transferReqDto.setWithdrawPassword(1234L);
        transferReqDto.setGubun("TRANSFER");

        String requestBody = om.writeValueAsString(transferReqDto);
        log.debug("디버그 : " + requestBody);

        // when
        ResultActions resultActions = mvc
                .perform(post("/api/s/account/transfer").content(requestBody).contentType(APPLICATION_JSON_UTF8));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("디버그 : " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

}
