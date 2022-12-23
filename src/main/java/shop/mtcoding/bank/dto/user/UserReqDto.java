package shop.mtcoding.bank.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

public class UserReqDto {
    @Getter
    @Setter
    public static class LoginReqDto {
        private String username;
        private String password;
    }

    @Setter
    @Getter
    public static class JoinReqDto {

        // 영문,숫자만 되고, 길이는 최소2~20이다.
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        private String username;

        @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
        @NotEmpty(message = "password는 필수입니다")
        private String password;

        @Size(min = 9, max = 20)
        @Pattern(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 적어주세요")
        private String email;

        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요.")
        private String fullname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UserPasswordUpdateReqDto {
        @Size(min = 4, max = 20)
        @NotEmpty(message = "password는 필수입니다")
        private String currentPassword;
        @Size(min = 4, max = 20)
        @NotEmpty(message = "password는 필수입니다")
        private String newPassword;
    }
}
