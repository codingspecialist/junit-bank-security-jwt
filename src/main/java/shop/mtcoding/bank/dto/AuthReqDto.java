package shop.mtcoding.bank.dto;

import lombok.Getter;
import lombok.Setter;

public class AuthReqDto {
    @Getter
    @Setter
    public static class LoginReqDto {
        private String username;
        private String password;
    }
}
