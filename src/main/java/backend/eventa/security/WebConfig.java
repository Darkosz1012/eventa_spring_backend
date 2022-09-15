package backend.eventa.security;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/**")
//                .addResourceLocations("/resources/eventa_angular");
//    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/urlNotFound")
                // the viewName should be specified, in our case we forward to the index.html
                .setViewName("forward:/index.html");
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
                    "/urlNotFound"));
        };
    }
}