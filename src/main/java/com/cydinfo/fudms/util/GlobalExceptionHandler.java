package com.cydinfo.fudms.util;

import com.cydinfo.fudms.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


/**
 * GlobalExceptionHandler 는 예외가 발생했을때 실행되는 클래스입니다.
 */
@ControllerAdvice // 어느파일에서나 Exception이 발생해도 이 함수를 타게하기 위한 어노테이션
@RestController
public class GlobalExceptionHandler {

    /**
     * http status와 에러 메시지를 반환해주는 메소드
     * @param e
     * @return ResponseDto<String>
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseDto<String> handleArgumentException(Exception e) {
        System.out.println("GlobalExceptionHandler 호출");

        System.out.println(e.getMessage());

        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());

    }
}
