package pasha.grpc.wallet.practical.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pasha.grpc.wallet.practical.Wallet;
import pasha.grpc.wallet.practical.server.service.AccountService;
import pasha.grpc.wallet.practical.server.model.Account;
import pasha.grpc.wallet.practical.server.model.User;
import pasha.grpc.wallet.practical.server.repo.AccountRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    @Transactional
    public Wallet.BalanceResponse getBalance(Wallet.BalanceRequest request) {
        User user = new User();
        user.setId(request.getUserId());
        List<Account > accounts = accountRepository.getAccountsByUser(user);
        Wallet.BalanceResponse.Builder builder = Wallet.BalanceResponse.newBuilder();

        for (Account account: accounts) {
            builder.addFunds(Wallet.Funds.newBuilder().
                    setCurrency(Wallet.Currency.forNumber(account.getCurrency().getValue()))
                    .setAmount(account.getBalance().floatValue()));
        }
        return builder.build();
    }
}
