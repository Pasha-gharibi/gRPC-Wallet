package pasha.grpc.wallet.cache.server.service;
import org.springframework.stereotype.Service;
import pasha.grpc.wallet.cache.Wallet;
import pasha.grpc.wallet.cache.server.model.Fund;

import java.util.List;

@Service
public interface FundService {

    Wallet.DepositResponse deposit(Wallet.DepositRequest value);

    Wallet.WithdrawResponse withdraw(Wallet.WithdrawRequest value);

    Wallet.BalanceResponse getBalance(Wallet.BalanceRequest balanceRequest);

    List<Fund> getLatestFunds();

}
