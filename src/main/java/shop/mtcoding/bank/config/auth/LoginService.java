package shop.mtcoding.bank.config.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@Service
public class LoginService implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("디버그 : loadUserByUsername 실행됨");
        User userPS = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new InternalAuthenticationServiceException("인증 실패"));
        // request - 세션생성 - 세션사용 - response - 세션삭제
        return new LoginUser(userPS);
    }
    // if (userOP.isPresent()) {
    // return new LoginUser(userOP.get());
    // } else {
    // return null;
    // }
}
