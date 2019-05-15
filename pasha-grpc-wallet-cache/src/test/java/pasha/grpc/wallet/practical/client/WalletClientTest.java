package pasha.grpc.wallet.practical.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pasha.grpc.wallet.cache.DemoApp;
import pasha.grpc.wallet.cache.client.WalletClient;
import pasha.grpc.wallet.cache.server.repo.FundRepository;
import pasha.grpc.wallet.practical.server.GrpcServerTestBase;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by p.gharibi on 5/11/2019 at 15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApp.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"grpc.enableReflection=true", "grpc.port=0"})
public class WalletClientTest extends GrpcServerTestBase {

    @Autowired
    WalletClient walletClient;

    @Autowired
    FundRepository fundRepository;



    @Autowired
    @Qualifier("fundCache")
    ConcurrentHashMap cache;

    @Before
    public final void resetData() {
        fundRepository.deleteAll();
        cache.clear();
        System.out.println("All data truncated!");

        walletClient.setNumberOfCuncurrentUsers(10);
        walletClient.setConcurrentThreadsPerUser(11);
        walletClient.setRoundsPerThread(12);

    }

    @Test
    public void testClient() throws InterruptedException {
        walletClient.setNumberOfCuncurrentUsers(6);
        walletClient.setConcurrentThreadsPerUser(5);
        walletClient.setRoundsPerThread(4);
        walletClient.run();
    }

}