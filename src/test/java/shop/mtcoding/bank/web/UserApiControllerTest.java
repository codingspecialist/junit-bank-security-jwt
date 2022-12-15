package shop.mtcoding.bank.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.user.UserReqDto.UserJoinReqDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserPasswordUpdateReqDto;

@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql") // teardown
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserApiControllerTest extends DummyObject {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        User cos = userRepository.save(newUser("cos", "코스"));
        User admin = userRepository.save(newUser("admin", "관리자"));
    }

    @Test
    public void join_success_test() throws Exception {
        // given
        UserJoinReqDto userJoinReqDto = new UserJoinReqDto();
        userJoinReqDto.setUsername("love");
        userJoinReqDto.setPassword("1234");
        userJoinReqDto.setEmail("love@nate.com");
        userJoinReqDto.setFullname("러브");

        String requestBody = om.writeValueAsString(userJoinReqDto);
        log.debug("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mvc
                .perform(post("/join").content(requestBody).contentType(APPLICATION_JSON_UTF8));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : " + responseBody);
        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void join_fail_test() throws Exception {
        // given
        UserJoinReqDto userJoinReqDto = new UserJoinReqDto();
        userJoinReqDto.setUsername("ssar");
        userJoinReqDto.setPassword("1234");
        userJoinReqDto.setEmail("ssar@nate.com");
        userJoinReqDto.setFullname("쌀");

        String requestBody = om.writeValueAsString(userJoinReqDto);
        log.debug("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mvc
                .perform(post("/join").content(requestBody).contentType(APPLICATION_JSON_UTF8));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : " + responseBody);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    // ssar, cos, admin 테스트 해보기
    // 비번 변경해서 테스트 해보기
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void updatePassword_test() throws Exception {
        // given
        Long userId = 1L;
        UserPasswordUpdateReqDto userPasswordUpdateReqDto = new UserPasswordUpdateReqDto();
        userPasswordUpdateReqDto.setCurrentPassword("1234");
        userPasswordUpdateReqDto.setNewPassword("5678");

        String requestBody = om.writeValueAsString(userPasswordUpdateReqDto);
        log.debug("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mvc
                .perform(put("/user/" + userId + "/password").content(requestBody).contentType(APPLICATION_JSON_UTF8));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }
}
