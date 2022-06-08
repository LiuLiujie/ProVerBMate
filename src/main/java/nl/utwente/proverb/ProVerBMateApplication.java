package nl.utwente.proverb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ProVerBMateApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ProVerBMateApplication.class, args);
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }
}
