package pasha.grpc.wallet.practical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by alexf on 28-Jan-16.
 */

@SpringBootApplication
@ComponentScan(value = "pasha.grpc.wallet.practical.*")
public class DemoApp {
    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class,args);
    }
}
