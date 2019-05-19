package pasha.grpc.wallet.practical.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pasha.grpc.wallet.practical.Wallet;
import pasha.grpc.wallet.practical.server.config.Dashboard;
import pasha.grpc.wallet.practical.server.model.Account;
import pasha.grpc.wallet.practical.server.enm.Currency;
import pasha.grpc.wallet.practical.server.model.Transaction;
import pasha.grpc.wallet.practical.server.model.User;
import pasha.grpc.wallet.practical.server.repo.TransactionRepository;
import pasha.grpc.wallet.practical.server.repo.AccountRepository;
import pasha.grpc.wallet.practical.server.service.TransactionService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    /**
     * By default Account has declared in database.
     * To provide ACID transaction I have forced to follow 'https://vladmihalcea.com/a-beginners-guide-to-acid-and-database-transactions/' to
     * prevent concurrency LOST_UPDATE problem throw multiple thread.
     */
    @Override
    @Transactional
    public Wallet.DepositResponse deposit(Wallet.DepositRequest request) {
        User user = new User();
        user.setId(request.getUserId());
        Account account = accountRepository
                .getAccountByUserIdAndCurrency(request.getUserId(), Currency.valueOf(request.getCurrency().getNumber()).get());
        account.setBalance(account.getBalance().add(BigDecimal.valueOf(request.getAmount())));
        accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setUser(user);
        transaction.setDate(new Date());
        transaction.setAmount(BigDecimal.valueOf(request.getAmount()));
        transaction.setCurrency(Currency.valueOf(request.getCurrencyValue()).get());
        transactionRepository.save(transaction);
        return Wallet.DepositResponse.newBuilder().setResult(Wallet.Result.newBuilder()
                .setCode(0).setMessage(Dashboard.ok).build()).build();
    }

    @Override
    @Transactional
    public Wallet.WithdrawResponse withdraw(Wallet.WithdrawRequest request) {
        User user = new User();
        user.setId(request.getUserId());
        Account account = accountRepository
                .getAccountByUserIdAndCurrency(request.getUserId(), Currency.valueOf(request.getCurrency().getNumber()).get());

        if (account == null || account.getBalance().compareTo(BigDecimal.valueOf(request.getAmount())) < 0) {
            return Wallet.WithdrawResponse.newBuilder().setResult(Wallet.Result.newBuilder()
                    .setCode(1).setMessage(Dashboard.insufficient_funds).build()).build();
        } else {
            account.setBalance(account.getBalance().subtract(BigDecimal.valueOf(request.getAmount())));
            accountRepository.save(account);
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setAccount(account);
            transaction.setDate(new Date());
            transaction.setAmount(BigDecimal.ZERO.subtract(BigDecimal.valueOf(request.getAmount())));
            transaction.setCurrency(Currency.valueOf(request.getCurrencyValue()).get());
            transactionRepository.save(transaction);
            return Wallet.WithdrawResponse.newBuilder().setResult(Wallet.Result.newBuilder()
                    .setCode(0).setMessage(Dashboard.ok).build()).build();
        }
    }


}