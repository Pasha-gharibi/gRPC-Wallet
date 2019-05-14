package pasha.grpc.wallet.cache.client;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import pasha.grpc.wallet.cache.server.enm.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by p.gharibi on 5/11/2019 at 14.
 */
@Component public class WalletClient {


    public static void main(String[] args) {
        SpringApplication.run(WalletClient.class, args);
    }

    Integer numberOfCuncurrentUsers;
    Integer concurrentThreadsPerUser;
    Integer roundsPerThread;

    public static Round roundA = new Round();
    public static Round roundB = new Round();
    public static Round roundC = new Round();

    public WalletClient(  ) {
        init();
    }

    private void init() {
        roundA.takeRound(new Deposit(new Operation(Currency.USD, 100L)));
        roundA.takeRound(new Withdraw(new Operation(Currency.USD, 200L)));
        roundA.takeRound(new Deposit(new Operation(Currency.EUR, 100L)));
        roundA.takeRound(new Balance(new Operation()));
        roundA.takeRound(new Withdraw(new Operation(Currency.USD, 100L)));
        roundA.takeRound(new Balance(new Operation()));
        roundA.takeRound(new Withdraw(new Operation(Currency.USD, 100L)));

        roundB.takeRound(new Withdraw(new Operation(Currency.GBP, 100L)));
        roundB.takeRound(new Deposit(new Operation(Currency.GBP, 300L)));
        roundB.takeRound(new Withdraw(new Operation(Currency.GBP, 100L)));
        roundB.takeRound(new Withdraw(new Operation(Currency.GBP, 100L)));
        roundB.takeRound(new Withdraw(new Operation(Currency.GBP, 100L)));

        roundC.takeRound(new Balance(new Operation()));
        roundC.takeRound(new Deposit(new Operation(Currency.USD, 100L)));
        roundC.takeRound(new Deposit(new Operation(Currency.USD, 100L)));
        roundC.takeRound(new Withdraw(new Operation(Currency.USD, 100L)));
        roundC.takeRound(new Deposit(new Operation(Currency.USD, 100L)));
        roundC.takeRound(new Balance(new Operation()));
        roundC.takeRound(new Withdraw(new Operation(Currency.USD, 200L)));
        roundC.takeRound(new Balance(new Operation()));
    }


    public Round getRandomElement() {
        List<Round> list = new ArrayList<>();
        list.add(roundA);
        list.add(roundB);
        list.add(roundC);
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }


    public void run() throws InterruptedException {

        List<ExecutorService> executorServices = new ArrayList<>(); // number of concurrent users

        for (int i = 0; i < numberOfCuncurrentUsers; i++) {
            ExecutorService ex = Executors.newFixedThreadPool(concurrentThreadsPerUser); // threads per user
            executorServices.add(ex);
        }
        for (int i = 0; i < numberOfCuncurrentUsers; i++) {
            ExecutorService ex = executorServices.get(i);
            for (int j = 0; j < concurrentThreadsPerUser; j++) {
                RoundBunch roundBunch = new RoundBunch();
                for (int k = 0; k < roundsPerThread; k++) {
                    roundBunch.addRound(getRandomElement());
                }
                roundBunch.setUserId(i + 1);
                ex.execute(roundBunch);
            }
            ex.shutdown();
            ex.awaitTermination(50, TimeUnit.SECONDS);

        }

    }


    public Integer getNumberOfCuncurrentUsers() {
        return numberOfCuncurrentUsers;
    }

    public void setNumberOfCuncurrentUsers(Integer numberOfCuncurrentUsers) {
        this.numberOfCuncurrentUsers = numberOfCuncurrentUsers;
    }

    public Integer getConcurrentThreadsPerUser() {
        return concurrentThreadsPerUser;
    }

    public void setConcurrentThreadsPerUser(Integer concurrentThreadsPerUser) {
        this.concurrentThreadsPerUser = concurrentThreadsPerUser;
    }

    public Integer getRoundsPerThread() {
        return roundsPerThread;
    }

    public void setRoundsPerThread(Integer roundsPerThread) {
        this.roundsPerThread = roundsPerThread;
    }

}
