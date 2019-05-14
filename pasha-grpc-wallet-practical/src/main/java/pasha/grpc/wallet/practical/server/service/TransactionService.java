package pasha.grpc.wallet.practical.server.service;

import pasha.grpc.wallet.practical.Wallet;

public interface TransactionService {
    Wallet.DepositResponse deposit(Wallet.DepositRequest request);
    Wallet.WithdrawResponse withdraw(Wallet.WithdrawRequest request);
}
