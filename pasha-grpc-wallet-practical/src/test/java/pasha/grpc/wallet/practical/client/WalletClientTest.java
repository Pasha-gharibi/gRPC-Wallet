package pasha.grpc.wallet.practical.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pasha.grpc.wallet.practical.DemoApp;
import pasha.grpc.wallet.practical.server.GrpcServerTestBase;
import pasha.grpc.wallet.practical.server.enm.Currency;
import pasha.grpc.wallet.practical.server.model.Account;
import pasha.grpc.wallet.practical.server.model.User;
import pasha.grpc.wallet.practical.server.repo.AccountRepository;
import pasha.grpc.wallet.practical.server.repo.TransactionRepository;
import pasha.grpc.wallet.practical.server.repo.UserRepository;

import java.math.BigDecimal;

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

    /**
     * Accounts also added before test As I can still add them when I receive the first transaction  something like upsert (update/insert) but
     * it impacts performance because hibernate is not good at performing upsert and some transactions may result in deadlock which I need to retry them.
     * The best way to do that is to use stored procedure.
     */
    @Before
    public final void resetData() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        System.out.println("All data truncated!");

        walletClient.setNumberOfCuncurrentUsers(3);
        walletClient.setConcurrentThreadsPerUser(10);
        walletClient.setRoundsPerThread(4);

        User user;
        for (int i = 0; i <  walletClient.getNumberOfCuncurrentUsers() ; i++) {
            user = new User();
            user.setId(Long.valueOf(i+1));
            user.setUsername(Long.valueOf(i+1).toString());
            userRepository.save(user);

            for (Currency value : Currency.values()) {
                if (value.getValue() < 0) {
                    continue;
                }
                Account account = new Account();
                account.setUser(user);
                account.setBalance(BigDecimal.ZERO);
                account.setCurrency(value);
                accountRepository.save(account);
            }
        }

    }

    @Test
    public void testClient() throws InterruptedException {
        walletClient.run();
    }

}