package cn.com.mfqh.service.user.api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    public String getInfo(@AuthenticationPrincipal Jwt jwt) {
        // 这里能拿到token里的用户信息，说明验证成功了
        return "登录用户：" + jwt.getSubject() + "，权限：" + jwt.getClaimAsString("scope");
    }
}
