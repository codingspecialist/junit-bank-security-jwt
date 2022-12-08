package shop.mtcoding.bank.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.user.User;

public class UserRespDto {
    @Setter
    @Getter
    public static class UserJoinRespDto {
        private Long id;
        private String username;
        private String fullname;

        public UserJoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
        }
    }
}
