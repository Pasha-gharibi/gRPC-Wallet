package pasha.grpc.wallet.practical.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pasha.grpc.wallet.practical.DemoApp;
import pasha.grpc.wallet.practical.Wallet;
import pasha.grpc.wallet.practical.WalletServiceGrpc;
import pasha.grpc.wallet.practical.server.config.Dashboard;
import pasha.grpc.wallet.practical.server.enm.Currency;
import pasha.grpc.wallet.practical.server.model.Account;
import pasha.grpc.wallet.practical.server.model.User;
import pasha.grpc.wallet.practical.server.repo.AccountRepository;
import pasha.grpc.wallet.practical.server.repo.TransactionRepository;
import pasha.grpc.wallet.practical.server.repo.UserRepository;

import java.awt.*;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by P.Gharibi
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApp.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"grpc.enableReflection=true", "grpc.port=0"})
public class ServerApiTest extends GrpcServerTestBase {

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

        User user = new User();
        user.setId(1L);
        user.setUsername("Paola");
        System.out.println("user : " +user.getUsername());
        userRepository.save(user);

        Account account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(Currency.USD);
        accountRepository.save(account);

        account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(Currency.EUR);
        accountRepository.save(account);


        account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(Currency.GBP);
        accountRepository.save(account);

    }

    @Test
    public void serverTest() {


        // Level. 1
        Iterator<Wallet.WithdrawResponse> withdraw1 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        Assert.assertEquals(withdraw1.next().getResult().getMessage(), Dashboard.insufficient_funds);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));



        // Level. 2
        Iterator<Wallet.DepositResponse> deposit2 = WalletServiceGrpc.newBlockingStub(channel).deposit(Wallet.DepositRequest.newBuilder().setUserId(1).setAmount(100).setCurrency(Wallet.Currency.USD).build());
        Assert.assertEquals(deposit2.next().getResult().getMessage(), Dashboard.ok);
        System.out.println(MessageFormat.format("---------Deposit Successful for user {0}! Deposit : {1}, Currency = {2}---------", 1, 100, Wallet.Currency.USD));

        // Level. 3
        Iterator<Wallet.BalanceResponse> balance3 = WalletServiceGrpc.newBlockingStub(channel).balance(Wallet.BalanceRequest.newBuilder().setUserId(1).build());
        Wallet.BalanceResponse result3 = balance3.next();
        System.out.println(MessageFormat.format("---------There should be just one Found for user {0}.---------", 1));
        Assert.assertEquals(result3.getFunds(0).getCurrency(), Wallet.Currency.USD);
        Assert.assertEquals(result3.getFunds(0).getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.USD, result3.getFunds(0).getAmount()));

        //Level. 4
        Iterator<Wallet.WithdrawResponse> withdraw4 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        Assert.assertEquals(withdraw4.next().getResult().getMessage(), Dashboard.insufficient_funds);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));

        //Level. 5
        Iterator<Wallet.DepositResponse> deposit5 = WalletServiceGrpc.newBlockingStub(channel).deposit(Wallet.DepositRequest.newBuilder().setUserId(1).setAmount(100).setCurrency(Wallet.Currency.EUR).build());
        Assert.assertEquals(deposit5.next().getResult().getMessage(), Dashboard.ok);
        System.out.println(MessageFormat.format("---------Deposit Successful for user {0}! Deposit : {1}, Currency = {2}---------", 1, 100, Wallet.Currency.EUR));

        //Level. 6
        Iterator<Wallet.BalanceResponse> balance6 = WalletServiceGrpc.newBlockingStub(channel).balance(Wallet.BalanceRequest.newBuilder().setUserId(1).build());
        Wallet.BalanceResponse result6 = balance6.next();
        System.out.println(MessageFormat.format("---------There should be two Founds for user {0}.---------", 1));
        Assert.assertEquals(result6.getFunds(0).getCurrency(), Wallet.Currency.USD);
        Assert.assertEquals(result6.getFunds(0).getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.USD, result6.getFunds(0).getAmount()));
        Assert.assertEquals(result6.getFunds(1).getCurrency(), Wallet.Currency.EUR);
        Assert.assertEquals(result6.getFunds(1).getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.EUR, result6.getFunds(1).getAmount()));

        //Level. 7
        Iterator<Wallet.WithdrawResponse> withdraw7 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        Assert.assertEquals(withdraw7.next().getResult().getMessage(), Dashboard.insufficient_funds);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));

        //Level. 8
        Iterator<Wallet.DepositResponse> deposit8 = WalletServiceGrpc.newBlockingStub(channel).deposit(Wallet.DepositRequest.newBuilder().setUserId(1).setAmount(100).setCurrency(Wallet.Currency.USD).build());
        Assert.assertEquals(deposit8.next().getResult().getMessage(), Dashboard.ok);
        System.out.println(MessageFormat.format("---------Deposit Successful for user {0}! Deposit : {1}, Currency = {2}---------", 1, 100, Wallet.Currency.USD));

        //Level. 9
        Iterator<Wallet.BalanceResponse> balance9 = WalletServiceGrpc.newBlockingStub(channel).balance(Wallet.BalanceRequest.newBuilder().setUserId(1).build());
        Wallet.BalanceResponse result9 = balance9.next();
        System.out.println(MessageFormat.format("---------There should be two Founds for user {0}.---------", 1));
        Optional<Wallet.Funds> fundEUR9 =  result9.getFundsList().stream().filter(r->r.getCurrency().equals(Wallet.Currency.EUR)).findFirst();
        Assert.assertTrue(fundEUR9.isPresent());
        Assert.assertEquals(fundEUR9.get().getCurrency(), Wallet.Currency.EUR);
        Assert.assertEquals(fundEUR9.get().getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.EUR,fundEUR9.get().getAmount()));
        Optional<Wallet.Funds> fundUSD9  =  result9.getFundsList().stream().filter(r->r.getCurrency().equals(Wallet.Currency.USD)).findFirst();
        Assert.assertTrue(fundUSD9.isPresent());
        Assert.assertEquals(fundUSD9.get().getCurrency(), Wallet.Currency.USD);
        Assert.assertEquals(fundUSD9.get().getAmount(), 200, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.USD, fundUSD9.get().getAmount()));

        //Level. 10
        Iterator<Wallet.WithdrawResponse> withdraw10 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        Assert.assertEquals(withdraw10.next().getResult().getMessage(), Dashboard.ok);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));

        //Level. 11
        Iterator<Wallet.BalanceResponse> balance11 = WalletServiceGrpc.newBlockingStub(channel).balance(Wallet.BalanceRequest.newBuilder().setUserId(1).build());
        Wallet.BalanceResponse result11 = balance11.next();
        Optional<Wallet.Funds> fundEUR11 =  result11.getFundsList().stream().filter(r->r.getCurrency().equals(Wallet.Currency.EUR)).findFirst();
        System.out.println(MessageFormat.format("---------There should be two Founds for user {0}.---------", 1));
        Assert.assertTrue(fundEUR11.isPresent());
        Assert.assertEquals(fundEUR11.get().getCurrency(), Wallet.Currency.EUR);
        Assert.assertEquals(fundEUR11.get().getAmount(), 100, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.EUR, fundEUR11.get().getAmount()));
        Optional<Wallet.Funds> fundUSD11 =  result11.getFundsList().stream().filter(r->r.getCurrency().equals(Wallet.Currency.USD)).findFirst();
        Assert.assertEquals(fundUSD11.get().getCurrency(), Wallet.Currency.USD);
        Assert.assertEquals(fundUSD11.get().getAmount(), 0, 0);
        System.out.println(MessageFormat.format("---------Currency : {0}, Balance : {1}.---------", Wallet.Currency.USD, fundUSD11.get().getAmount()));

        //Level. 12
        Iterator<Wallet.WithdrawResponse> withdraw12 = WalletServiceGrpc.newBlockingStub(channel).withdraw(Wallet.WithdrawRequest.newBuilder().setUserId(1).setAmount(200).setCurrency(Wallet.Currency.USD).build());
        Assert.assertEquals(withdraw12.next().getResult().getMessage(), Dashboard.insufficient_funds);
        System.out.println(MessageFormat.format("---------There is no enough money for user {0}! Withdraw = {1}, Currency = {2}; ---------", 1, 200, Wallet.Currency.USD));

    }

}
