package backend.eventa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class EventaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventaApplication.class, args);
    }

}
