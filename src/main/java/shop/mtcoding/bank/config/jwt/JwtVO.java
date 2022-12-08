package shop.mtcoding.bank.config.jwt;

public interface JwtVO {
    public static final String SECRET = "메타코딩";
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일 (1/1000초)
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
}
