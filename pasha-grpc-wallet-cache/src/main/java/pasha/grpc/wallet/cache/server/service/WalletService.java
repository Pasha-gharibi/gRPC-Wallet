package pasha.grpc.wallet.cache.server.service;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pasha.grpc.wallet.cache.LogInterceptor;
import pasha.grpc.wallet.cache.Wallet;
import pasha.grpc.wallet.cache.WalletServiceGrpc;

/**
 * Created by p.gharibi on 5/8/2019 at 15.
 */
@GRpcService(interceptors = {LogInterceptor.class})
public class WalletService extends WalletServiceGrpc.WalletServiceImplBase {

    @Autowired
    FundService fundService;

    @Override
    public void deposit(Wallet.DepositRequest request, StreamObserver<Wallet.DepositResponse> responseObserver) {
        responseObserver.onNext(fundService.deposit(request));
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(Wallet.WithdrawRequest request, StreamObserver<Wallet.WithdrawResponse> responseObserver) {
        responseObserver.onNext(fundService.withdraw(request));
        responseObserver.onCompleted();
    }

    @Override
    public void balance(Wallet.BalanceRequest request, StreamObserver<Wallet.BalanceResponse> responseObserver) {
        responseObserver.onNext(fundService.getBalance(request));
        responseObserver.onCompleted();
    }
}
