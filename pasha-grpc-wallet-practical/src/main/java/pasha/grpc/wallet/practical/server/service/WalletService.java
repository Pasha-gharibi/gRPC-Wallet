package pasha.grpc.wallet.practical.server.service;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pasha.grpc.wallet.practical.LogInterceptor;
import pasha.grpc.wallet.practical.Wallet;
import pasha.grpc.wallet.practical.WalletServiceGrpc;

/**
 * Created by p.gharibi on 5/8/2019 at 15.
 */
@GRpcService(interceptors = {LogInterceptor.class})
public class  WalletService extends WalletServiceGrpc.WalletServiceImplBase {

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @Override
    public void deposit(Wallet.DepositRequest request, StreamObserver<Wallet.DepositResponse> responseObserver) {
        responseObserver.onNext(transactionService.deposit(request));
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(Wallet.WithdrawRequest request, StreamObserver<Wallet.WithdrawResponse> responseObserver) {
        responseObserver.onNext(transactionService.withdraw(request));
        responseObserver.onCompleted();
    }

    @Override
    public void balance(Wallet.BalanceRequest request, StreamObserver<Wallet.BalanceResponse> responseObserver) {
        responseObserver.onNext(accountService.getBalance(request));
        responseObserver.onCompleted();
    }
}
