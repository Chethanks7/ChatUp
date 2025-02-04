package com.ChatUp.ChatUp.user;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserMapper {
    public User fromTokenAttributes(@NotNull Map<String, Object> claims) {
        User user = new User();

        if(claims.containsKey("sub"))
            user.setId(claims.get("sub").toString());
        if(claims.containsKey("given_name"))
            user.setFirstname(claims.get("given_name").toString());
        else if (claims.containsKey("nickname")) {
            user.setFirstname(claims.get("nickname").toString());
        }

        if(claims.containsKey("family-name"))
            user.setLastname(claims.get("family-name").toString());

        if(claims.containsKey("email"))
            user.setEmail(claims.get("email").toString());

        user.setLastSeen(LocalDateTime.now());
        return user ;

    }
}
