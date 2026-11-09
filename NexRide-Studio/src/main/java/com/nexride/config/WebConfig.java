package com.nexride.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Value("${nexride.data-dir:./data}")
  private String data;

  @Bean
  public org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
      fieldVisibility() {
    return b -> b.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  @Bean
  public Filter csrfAndSecurityHeaders() {
    return new Filter() {
      public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
          throws IOException, ServletException {
        HttpServletRequest q = (HttpServletRequest) req;
        HttpServletResponse p = (HttpServletResponse) res;
        p.setHeader("X-Content-Type-Options", "nosniff");
        p.setHeader("X-Frame-Options", "DENY");
        p.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        p.setHeader(
            "Content-Security-Policy",
            "default-src 'self'; img-src 'self' data:; script-src 'self'; style-src 'self'"
                + " 'unsafe-inline' https://fonts.googleapis.com; font-src 'self'"
                + " https://fonts.gstatic.com; form-action 'self' https://sandbox.payhere.lk"
                + " https://www.payhere.lk; frame-ancestors 'none'; base-uri 'self'");
        String path = q.getRequestURI();
        if (path.startsWith("/api/")
            && !Set.of("GET", "HEAD", "OPTIONS").contains(q.getMethod())
            && !Set.of(
                    "/api/login",
                    "/api/admin/login",
                    "/api/register",
                    "/api/payments/payhere/notify")
                .contains(path)) {
          HttpSession session = q.getSession(false);
          Object token = session == null ? null : session.getAttribute("csrf");
          if (token == null || !token.equals(q.getHeader("X-CSRF-Token"))) {
            p.setStatus(403);
            p.setContentType("application/json");
            p.getWriter().write("{\"message\":\"Session expired. Refresh and try again.\"}");
            return;
          }
        }
        chain.doFilter(req, res);
      }
    };
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry r) {
    r.addResourceHandler("/uploads/**")
        .addResourceLocations(
            java.nio.file.Path.of(data).toAbsolutePath().resolve("uploads").toUri().toString());
  }

  @Override
  public void addViewControllers(ViewControllerRegistry r) {
    r.addViewController("/login").setViewName("forward:/index.html");
    r.addViewController("/signin").setViewName("forward:/index.html");
    r.addViewController("/register").setViewName("forward:/index.html");
    r.addViewController("/admin").setViewName("forward:/index.html");
    r.addViewController("/admin/**").setViewName("forward:/index.html");
  }
}
