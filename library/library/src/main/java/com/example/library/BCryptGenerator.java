package com.example.library;

import org.springframework.security
        .crypto.bcrypt.BCryptPasswordEncoder;

// хешує паролі для внесення в бд вже зареєстрованих акаунтів заздалегідь, в самому застосунку не потрібно

public class BCryptGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        System.out.println(
                encoder.encode("admin1")
        );

        System.out.println(
                encoder.encode("client2")
        );

        System.out.println(
                encoder.encode("client3")
        );

        System.out.println(
                encoder.encode("client4")
        );

        System.out.println(
                encoder.encode("client5")
        );
    }

}
