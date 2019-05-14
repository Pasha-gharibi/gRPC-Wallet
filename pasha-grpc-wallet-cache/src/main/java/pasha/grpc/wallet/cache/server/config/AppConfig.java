package pasha.grpc.wallet.cache.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import pasha.grpc.wallet.cache.server.model.Fund;
import pasha.grpc.wallet.cache.server.repo.FundRepository;
import pasha.grpc.wallet.cache.server.service.FundService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by p.gharibi on 5/5/2019 at 08.
 */

@Configuration
public class AppConfig {

    @Order(100)
    @Autowired
    FundRepository fundRepository;

    /**
     * Caching is generally used for static/semi-static tables with finite data but the Fund(Transaction/Wallet)
     * table has highly volatile data and the number of records can be very high (millions of users)
     * so in a real project I omit these kind of caching.
     * However up to logical rules, it maintains the latest transactions which makes les fetch from Database, so it speeds up
     * Balance Requests  as I have got balances from just this cache.
     */

    @Bean(name = "fundCache")
    @Scope("singleton")
    public ConcurrentHashMap<Long, Fund> cache() {

        ConcurrentHashMap<Long, Fund>  cache = new ConcurrentHashMap<Long,Fund>();
        List<Fund> funds =  fundRepository.getLatestFunds();
        for (Fund fund : funds) {
            cache.put(fund.getId(),fund);
        }
        return cache;
    }

}
