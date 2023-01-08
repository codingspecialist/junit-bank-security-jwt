package shop.mtcoding.bank.dto.account;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.user.User;

public class AccountReqDto {
    @Setter
    @Getter
    public static class AccountSaveReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4, message = "숫자 4자리로 작성해주세요")
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4, message = "숫자 4자리로 작성해주세요")
        private Long password;

        public Account toEntity(User user) {
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class AccountDepositReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4, message = "숫자 4자리로 작성해주세요")
        private Long number;
        @NotNull
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "^(DEPOSIT)$", message = "구분값을 정확히 입력해주세요")
        private String gubun;
        @NotEmpty
        @Pattern(regexp = "^[0-9]{3}[0-9]{4}[0-9]{4}$", message = "전화번호 양식에 맞게 입력해주세요")
        private String tel;
    }

    @Getter
    @Setter
    public static class AccountWithdrawReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4, message = "숫자 4자리로 작성해주세요")
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4, message = "숫자 4자리로 작성해주세요")
        private Long password;
        @NotNull
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "^(WITHDRAW)$", message = "구분값을 정확히 입력해주세요")
        private String gubun;
    }

    @Getter
    @Setter
    public static class AccountTransferReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4, message = "숫자 4자리로 작성해주세요")
        private Long withdrawNumber;
        @NotNull
        @Digits(integer = 4, fraction = 4, message = "숫자 4자리로 작성해주세요")
        private Long depositNumber;
        @NotNull
        @Digits(integer = 4, fraction = 4, message = "숫자 4자리로 작성해주세요")
        private Long withdrawPassword;
        @NotNull
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "^(TRANSFER)$", message = "구분값을 정확히 입력해주세요")
        private String gubun;
    }
}
