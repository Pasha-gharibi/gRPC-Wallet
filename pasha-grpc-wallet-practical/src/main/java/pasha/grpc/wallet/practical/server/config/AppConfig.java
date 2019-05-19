package pasha.grpc.wallet.practical.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pasha.grpc.wallet.practical.ConcurrentOperationExecutor;

/**
 * Created by p.gharibi on 5/19/2019 at 15.
 */
@Component
public class AppConfig {

    @Bean
    public ConcurrentOperationExecutor getConcurrentOperationExecutor() {
        return new ConcurrentOperationExecutor();
    }
}
