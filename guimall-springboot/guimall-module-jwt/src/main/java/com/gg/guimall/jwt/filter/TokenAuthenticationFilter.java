package com.gg.guimall.jwt.filter;

import com.gg.guimall.jwt.utils.JwtTokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/1/31
 * @description: Token 校验过滤器
 **/
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.tokenHeaderKey}")
    private String tokenHeaderKey;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 优先从 Authorization 获取管理员 Token
        String header = request.getHeader(tokenHeaderKey);

        // 如果 Authorization 为空，尝试从 MemberToken 获取前台会员 Token
        if (StringUtils.isBlank(header) || !StringUtils.startsWith(header, tokenPrefix)) {
            String memberHeader = request.getHeader("MemberToken");
            if (StringUtils.startsWith(memberHeader, tokenPrefix)) {
                header = memberHeader;
            }
        }

        // 判断 value 值是否以 Bearer 开头
        if (StringUtils.startsWith(header, tokenPrefix)) {
            // 截取 Token 令牌
            String token = StringUtils.substring(header, 7);
            log.debug("Token: {}", token);

            // 判空 Token
            if (StringUtils.isNotBlank(token)) {
                try {
                    // 校验 Token 是否可用, 若解析异常，针对不同异常做出不同的响应参数
                    jwtTokenHelper.validateToken(token);
                } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                    // 抛出异常，统一让 AuthenticationEntryPoint 处理响应参数
                    authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 不可用"));
                    return;
                } catch (ExpiredJwtException e) {
                    authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 已失效"));
                    return;
                }

                // 从 Token 中解析出用户名
                String username = jwtTokenHelper.getUsernameByToken(token);

                if (StringUtils.isNotBlank(username)
                        && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                    // 前台会员 token 的 subject 为纯数字 ID，跳过 admin 用户表查询
                    if (StringUtils.isNumeric(username)) {
                        log.debug("Token subject is numeric (memberId={}), skip admin authentication", username);
                    } else {
                        // 根据用户名获取用户详情信息
                        try {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            // 将用户信息存入 authentication，方便后续校验
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } catch (Exception e) {
                            log.info("Token subject not found in admin user table, ignore authentication. subject={}", username);
                        }
                    }
                }
            }
        }

        // 继续执行下一个过滤器
        filterChain.doFilter(request, response);
    }
}