package pasha.grpc.wallet.practical.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pasha.grpc.wallet.practical.Wallet;
import pasha.grpc.wallet.practical.WalletServiceGrpc;
import pasha.grpc.wallet.practical.server.enm.Currency;
import java.util.Iterator;

/**
 * Created by p.gharibi on 5/11/2019 at 15.
 */
public class Operation {

    /**
     * Here BlockingStub is used, since StreamObserver is not thread-safe so it can not be called with multiple thread,
     * So I used synchronized call ( Blocking Stub)
     */


    public static ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
    protected static WalletServiceGrpc.WalletServiceBlockingStub blockingStub = WalletServiceGrpc.newBlockingStub(managedChannel);

    Currency currency;
    Long amount;

    public Operation() {
    }

    public Operation(Currency currency, Long amount) {
        this.currency = currency;
        this.amount = amount;
    }


    public void deposit(Integer userId) {
       Iterator<Wallet.DepositResponse>  iterator = blockingStub.deposit(Wallet.DepositRequest.newBuilder()
                    .setCurrency(Wallet.Currency.forNumber(currency.getValue()))
                    .setAmount(amount)
                    .setUserId(userId).build());

       iterator.forEachRemaining(d-> System.out.println( d.toString()));
    }

    public void withdraw(Integer userId) {

        Iterator<Wallet.WithdrawResponse>  iterator = blockingStub.withdraw(Wallet.WithdrawRequest.newBuilder()
                .setCurrency(Wallet.Currency.forNumber(currency.getValue()))
                .setAmount(amount)
                .setUserId(userId).build());

        iterator.forEachRemaining(d-> System.out.println(d.toString()));

    }

    public void balance(Integer userId) {

        Iterator<Wallet.BalanceResponse>  iterator = blockingStub.balance(Wallet.BalanceRequest.newBuilder().setUserId(userId).build());
        iterator.forEachRemaining(d-> System.out.println(d.toString()));
    }


    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
