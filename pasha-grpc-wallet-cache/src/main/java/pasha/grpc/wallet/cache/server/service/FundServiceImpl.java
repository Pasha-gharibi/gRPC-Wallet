package pasha.grpc.wallet.cache.server.service;
import pasha.grpc.wallet.cache.server.enm.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pasha.grpc.wallet.cache.Wallet;
import pasha.grpc.wallet.cache.server.config.Dashboard;
import pasha.grpc.wallet.cache.server.model.Fund;
import pasha.grpc.wallet.cache.server.repo.FundRepository;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class FundServiceImpl implements FundService {

    @Autowired
    FundRepository fundRepository;

    /**
     * this cache would be used much time under lots of transactions. so it has marked as volatile to do much faster
     * however in distributed applications this is useless and makes conflicts.
     */
    @Autowired
    @Qualifier(value = "fundCache")
    volatile ConcurrentHashMap<Long, Fund> cache;

    public Wallet.DepositResponse deposit(Wallet.DepositRequest request) {

        Fund newFund = new Fund();
        newFund.setUserId(request.getUserId());
        newFund.setCurrency(Currency.valueOf(request.getCurrency().getNumber()).get());

        Fund last = getLatestTransaction(newFund);
        if (last != null) {
            newFund.setBalanceAmount(last.getBalanceAmount().add(BigDecimal.valueOf(request.getAmount())));
        } else {
            newFund.setBalanceAmount(BigDecimal.valueOf(request.getAmount()));
        }

        newFund.setTransactionAmount(BigDecimal.valueOf(request.getAmount()));
        newFund.setTransactionTime(new Date());
        Fund saved = fundRepository.save(newFund);

        updateCache(last, saved);

        return Wallet.DepositResponse.newBuilder().setResult(Wallet.Result.newBuilder().setCode(0).setMessage(Dashboard.ok)).build();

    }

    public Wallet.WithdrawResponse withdraw(Wallet.WithdrawRequest request) {
        Fund newFund = new Fund();
        newFund.setUserId(request.getUserId());
        newFund.setCurrency(Currency.valueOf(request.getCurrency().getNumber()).get());

        Fund last = getLatestTransaction(newFund);
        if (last != null) {
            if (last.getBalanceAmount().compareTo(BigDecimal.valueOf(request.getAmount()))<0) {
                return Wallet.WithdrawResponse.newBuilder().setResult(Wallet.Result.newBuilder().setCode(1).setMessage(Dashboard.insufficient_funds)).build();
            }
            newFund.setBalanceAmount(last.getBalanceAmount().subtract(BigDecimal.valueOf( request.getAmount())));

        } else {
            return Wallet.WithdrawResponse.newBuilder().setResult(Wallet.Result.newBuilder().setCode(1).setMessage(Dashboard.insufficient_funds)).build();
        }


        newFund.setTransactionAmount(BigDecimal.ZERO.subtract(BigDecimal.valueOf(request.getAmount())));
        newFund.setTransactionTime(new Date());
        Fund saved = fundRepository.save(newFund);

        updateCache(last, saved);

        return Wallet.WithdrawResponse.newBuilder().setResult(Wallet.Result.newBuilder().setCode(0).setMessage(Dashboard.ok)).build();
    }


    /**
     * Upto updating cache in any transaction we can just refer to Cache to fetch balance.
     * Its better to make another strategy to synchronize chace whit permanent persist db
     */
    @Override
    public Wallet.BalanceResponse getBalance(Wallet.BalanceRequest balanceRequest) {

        List<Fund> fundList = cache.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getUserId().equals(balanceRequest.getUserId()))
                .map(Map.Entry::getValue).collect(Collectors.toList());
        Wallet.BalanceResponse.Builder builder = Wallet.BalanceResponse.newBuilder();

        for (Fund fund : fundList) {
            builder.addFunds(Wallet.Funds.newBuilder().setCurrency(Wallet.Currency.forNumber(fund.getCurrency().getValue())).setAmount(fund.getBalanceAmount().floatValue()));
        }
        return builder.build();
    }

    /**
     * However I have used Blocking Stub in gRPC and it makes me no worry about concurrency, I use synchronized to manage order of threads
     */
    private  void updateCache(Fund last, Fund saved) {
        if (last != null) {
            cache.remove(last.getId(), last);
        }
        cache.put(saved.getId(), saved);
    }


    public Optional<Long> getKeysByValue(Fund value) {
        return cache.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey).findFirst();
    }


    @Override
    public List<Fund> getLatestFunds() {
        return fundRepository.getLatestFunds();
    }


    private Fund getLatestTransaction(Fund fund) {
        Optional<Long> key = getKeysByValue(fund);
        if (key.isPresent()) {
            return cache.get(key.get());
        } else return null;
    }


}
