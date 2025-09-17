package com.tustosc.setsail.Filters;

import com.tustosc.setsail.Entiy.Role;
import com.tustosc.setsail.Utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 从请求头获取 JWT
        String token = getTokenFromRequest(request);

        if (token != null) {
            try {
                // 2. 验证并解析 JWT
                String userId = jwtUtils.getUuidFromJwt(token);
                List<Role> roles = jwtUtils.getRoleFromJwt(token);
                // 3. 创建认证对象
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId, // 主体
                                null,   // 凭证（密码）
                                roles    // 权限
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 4. 设置认证上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效的Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken!=null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.split(" ")[1];
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    if (cookie.getValue()!=null && cookie.getValue().startsWith("Bearer ")) {
                        return cookie.getValue().split(" ")[1];
                    }
                }
            }
        }
        return null;
    }
}
