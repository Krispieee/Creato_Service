package com.creato.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yaml.snakeyaml.util.ArrayUtils;

import com.creato.Utils.JwtUtil;

import io.jsonwebtoken.Claims;

@Component
public class JwtInterceptors implements HandlerInterceptor  {
	@Autowired
    private JwtUtil jwtUtils;
	
	private static String array[] = { "usernameValidation", "login", "users", "picture" };
	
	private static List<String> publicApis = Arrays.asList(array);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth = request.getHeader("authorization");
        
        if( publicApis.stream().filter((api) -> {
        	return request.getRequestURI().contains(api);
        }).collect(Collectors.toList()).size() == 0 ){
            Claims claims = jwtUtils.verify(auth);

        }

        return true;
    }
}
