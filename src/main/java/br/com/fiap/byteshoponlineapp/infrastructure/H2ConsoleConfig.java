package br.com.fiap.byteshoponlineapp.infrastructure;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ConsoleConfig {

    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServlet() {
        ServletRegistrationBean<JakartaWebServlet> reg =
                new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
        // Opcional: parâmetros do console
        reg.addInitParameter("webAllowOthers", "false");
        reg.addInitParameter("trace", "false");
        return reg;
    }
}