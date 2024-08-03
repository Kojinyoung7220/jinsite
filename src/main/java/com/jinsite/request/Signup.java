package com.jinsite.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Signup {

    private String name;
    private String email;
    private String password;
}
