package com.jinsite.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 인증시 받을 이메일과 페스워드 요청
 */
@Getter
@Setter
@ToString
public class Login {

    @NotBlank(message ="이메일을 입력해주세요.")
    private String email;

    @NotBlank(message ="비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
