package shop.mtcoding.bank.dto.account;

import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.user.User;

public class AccountReqDto {
    @Setter
    @Getter
    public static class AccountSaveReqDto {
        @Pattern(regexp = "^[0-9]{4,4}$", message = "숫자 4자리로 작성해주세요.")
        private Long number;
        @Pattern(regexp = "^[0-9]{4,4}$", message = "숫자 4자리로 작성해주세요.")
        private String password;

        public Account toEntity(User user) {
            return Account.builder()
                    .number(number.longValue())
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();
        }
    }
}
