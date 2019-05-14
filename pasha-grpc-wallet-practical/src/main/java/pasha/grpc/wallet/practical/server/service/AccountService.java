package pasha.grpc.wallet.practical.server.service;

import pasha.grpc.wallet.practical.Wallet;

public interface AccountService {
    Wallet.BalanceResponse getBalance(Wallet.BalanceRequest request);
}
