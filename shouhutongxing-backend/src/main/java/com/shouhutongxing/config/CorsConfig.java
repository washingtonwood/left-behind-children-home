package com.shouhutongxing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * CORS 跨域配置
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许前端地址
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",
            "http://127.0.0.1:3000",
            "http://localhost:5500"
        ));

        // 允许所有请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // 允许所有请求方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 允许携带凭证
        configuration.setAllowCredentials(true);

        // 预检请求缓存时间（秒）
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
