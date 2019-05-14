package pasha.grpc.wallet.practical.server;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pasha.grpc.wallet.cache.DemoApp;
import pasha.grpc.wallet.cache.Wallet;
import pasha.grpc.wallet.cache.WalletServiceGrpc;
import pasha.grpc.wallet.cache.server.config.Dashboard;
import pasha.grpc.wallet.cache.server.model.Fund;
import pasha.grpc.wallet.cache.server.repo.FundRepository;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by P.Gharibi
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApp.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"grpc.enableReflection=true", "grpc.port=0"})
public class ServerApiTest extends GrpcServerTestBase {

    @Autowired
    FundRepository fundRepository;

    @Autowired
    @Qualifier(value = "fundCache")
    ConcurrentHashMap<Long, Fund> cache;

    @Before
    public final void resetData() {
        fundRepository.deleteAll();
        cache.clear();
        System.out.println("All data truncated!");
    }

    @Test
    public void interceptorsTest() {


        // Level. 1
        Iterator<Wallet.WithdrawResponse> withdraw1 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        assertEquals(withdraw1.next().getResult().getMessage(), Dashboard.insufficient_funds);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));



        // Level. 2
        Iterator<Wallet.DepositResponse> deposit2 = WalletServiceGrpc.newBlockingStub(channel).deposit(Wallet.DepositRequest.newBuilder().setUserId(1).setAmount(100).setCurrency(Wallet.Currency.USD).build());
        assertEquals(deposit2.next().getResult().getMessage(), Dashboard.ok);
        System.out.println(MessageFormat.format("---------Deposit Successful for user {0}! Deposit : {1}, Currency = {2}---------", 1, 100, Wallet.Currency.USD));

        // Level. 3
        Iterator<Wallet.BalanceResponse> balance3 = WalletServiceGrpc.newBlockingStub(channel).balance(Wallet.BalanceRequest.newBuilder().setUserId(1).build());
        Wallet.BalanceResponse result3 = balance3.next();
        assertEquals(result3.getFundsCount(), 1);
        System.out.println(MessageFormat.format("---------There should be just one Found for user {0}.---------", 1));
        assertEquals(result3.getFunds(0).getCurrency(), Wallet.Currency.USD);
        assertEquals(result3.getFunds(0).getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.USD, result3.getFunds(0).getAmount()));

        //Level. 4
        Iterator<Wallet.WithdrawResponse> withdraw4 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        assertEquals(withdraw4.next().getResult().getMessage(), Dashboard.insufficient_funds);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));

        //Level. 5
        Iterator<Wallet.DepositResponse> deposit5 = WalletServiceGrpc.newBlockingStub(channel).deposit(Wallet.DepositRequest.newBuilder().setUserId(1).setAmount(100).setCurrency(Wallet.Currency.EUR).build());
        assertEquals(deposit5.next().getResult().getMessage(), Dashboard.ok);
        System.out.println(MessageFormat.format("---------Deposit Successful for user {0}! Deposit : {1}, Currency = {2}---------", 1, 100, Wallet.Currency.EUR));

        //Level. 6
        Iterator<Wallet.BalanceResponse> balance6 = WalletServiceGrpc.newBlockingStub(channel).balance(Wallet.BalanceRequest.newBuilder().setUserId(1).build());
        Wallet.BalanceResponse result6 = balance6.next();
        assertEquals(result6.getFundsCount(), 2);
        System.out.println(MessageFormat.format("---------There should be two Founds for user {0}.---------", 1));
        assertEquals(result6.getFunds(0).getCurrency(), Wallet.Currency.USD);
        assertEquals(result6.getFunds(0).getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.USD, result6.getFunds(0).getAmount()));
        assertEquals(result6.getFunds(1).getCurrency(), Wallet.Currency.EUR);
        assertEquals(result6.getFunds(1).getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.EUR, result6.getFunds(1).getAmount()));

        //Level. 7
        Iterator<Wallet.WithdrawResponse> withdraw7 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        assertEquals(withdraw7.next().getResult().getMessage(), Dashboard.insufficient_funds);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));

        //Level. 8
        Iterator<Wallet.DepositResponse> deposit8 = WalletServiceGrpc.newBlockingStub(channel).deposit(Wallet.DepositRequest.newBuilder().setUserId(1).setAmount(100).setCurrency(Wallet.Currency.USD).build());
        assertEquals(deposit8.next().getResult().getMessage(), Dashboard.ok);
        System.out.println(MessageFormat.format("---------Deposit Successful for user {0}! Deposit : {1}, Currency = {2}---------", 1, 100, Wallet.Currency.USD));

        //Level. 9
        Iterator<Wallet.BalanceResponse> balance9 = WalletServiceGrpc.newBlockingStub(channel).balance(Wallet.BalanceRequest.newBuilder().setUserId(1).build());
        Wallet.BalanceResponse result9 = balance9.next();
        assertEquals(result9.getFundsCount(), 2);
        System.out.println(MessageFormat.format("---------There should be two Founds for user {0}.---------", 1));
        assertEquals(result9.getFunds(0).getCurrency(), Wallet.Currency.EUR);
        assertEquals(result9.getFunds(0).getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.EUR, result9.getFunds(0).getAmount()));
        assertEquals(result9.getFunds(1).getCurrency(), Wallet.Currency.USD);
        assertEquals(result9.getFunds(1).getAmount(), 200, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.USD, result9.getFunds(1).getAmount()));

        //Level. 10
        Iterator<Wallet.WithdrawResponse> withdraw10 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        assertEquals(withdraw10.next().getResult().getMessage(), Dashboard.ok);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));

        //Level. 11
        Iterator<Wallet.BalanceResponse> balance11 = WalletServiceGrpc.newBlockingStub(channel).balance(Wallet.BalanceRequest.newBuilder().setUserId(1).build());
        Wallet.BalanceResponse result11 = balance11.next();
        assertEquals(result11.getFundsCount(), 2);
        System.out.println(MessageFormat.format("---------There should be two Founds for user {0}.---------", 1));
        assertEquals(result11.getFunds(0).getCurrency(), Wallet.Currency.EUR);
        assertEquals(result11.getFunds(0).getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.EUR, result11.getFunds(0).getAmount()));
        assertEquals(result11.getFunds(1).getCurrency(), Wallet.Currency.USD);
        assertEquals(result11.getFunds(1).getAmount(), 0, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.USD, result11.getFunds(1).getAmount()));

        //Level. 12
        Iterator<Wallet.WithdrawResponse> withdraw12 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        assertEquals(withdraw12.next().getResult().getMessage(), Dashboard.insufficient_funds);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));

    }

}
