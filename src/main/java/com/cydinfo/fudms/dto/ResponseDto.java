package com.cydinfo.fudms.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Http status와 메시지 객체를 반환하는 dto
 * @param <T>
 */
@Data
@NoArgsConstructor
@Builder
public class ResponseDto<T> {

    int status;
    Object msg;
    T data;

    public ResponseDto(int status, Object obj, T data) {
        super();
        this.status = status;
        this.msg = obj;
        this.data = data;
    }

    public ResponseDto(int status, T data) {
        super();
        this.status = status;
        this.data = data;
    }
}
