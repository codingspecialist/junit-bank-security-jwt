package shop.mtcoding.bank.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// http code = 200(get, delete, put), 201(post)
@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {
    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data;
}
