package com.example.library.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// чатгпт сказав що це може потім знадобитись щоб не писати великі коди,
// але там як вийде, може потім можна прибрати
public class SecurityUtils {
        public static String
        getCurrentUsername() {
            Authentication auth =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            return auth.getName();
        }
}
