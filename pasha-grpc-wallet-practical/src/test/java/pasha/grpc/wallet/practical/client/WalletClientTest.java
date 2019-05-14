package pasha.grpc.wallet.practical.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pasha.grpc.wallet.practical.DemoApp;
import pasha.grpc.wallet.practical.server.GrpcServerTestBase;
import pasha.grpc.wallet.practical.server.model.User;
import pasha.grpc.wallet.practical.server.repo.AccountRepository;
import pasha.grpc.wallet.practical.server.repo.TransactionRepository;
import pasha.grpc.wallet.practical.server.repo.UserRepository;

/**
 * Created by p.gharibi on 5/11/2019 at 15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApp.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"grpc.enableReflection=true", "grpc.port=0"})
public class WalletClientTest extends GrpcServerTestBase {

    @Autowired WalletClient walletClient;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    AccountRepository accountRepository;

    @Before
    public final void resetData() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        System.out.println("All data truncated!");

        walletClient.setNumberOfCuncurrentUsers(15);
        walletClient.setConcurrentThreadsPerUser(5);
        walletClient.setRoundsPerThread(4);

        User user;
        for (int i = 0; i <  walletClient.getNumberOfCuncurrentUsers() ; i++) {
            user = new User();
            user.setId(Long.valueOf(i+1));
            user.setUsername(Long.valueOf(i+1).toString());
            userRepository.save(user);
        }

    }

    @Test
    public void testClient() throws InterruptedException {
        walletClient.run();
    }

}