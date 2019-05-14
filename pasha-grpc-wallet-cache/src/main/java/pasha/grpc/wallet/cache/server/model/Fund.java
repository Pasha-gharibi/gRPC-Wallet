package pasha.grpc.wallet.cache.server.model;

import pasha.grpc.wallet.cache.server.enm.Currency;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 *
 * 1) Fund table is not normalized as it has functional dependencies to other entities so I can break it down into
 * three separate tables: User, Transaction and Wallet with the following schemas:
 * User: user_id, user_name, ...
 * Transaction: tx_id, user_id, amount, currency, date, ...
 * Wallet: user_id, currency, balance, ...
 *
 * BUT IN ORDER TO DECREASING BUSINESS AND APPLICATION SCOPE I HAVE JUST CONSIDERED A TABLE!
 *
 * 2) User and transaction have a one-to-many relationship as do User and Wallet,
 * so I can annotate primary and foreign keys with the appropriate hibernate annotations.
 *
 * 3) Its possible to use check constraint for the currency field to limit its value to EUR, USD, GBP.
 * Although I can create a separate table for currency and use its id in other tables but it may be overkill for only three currencies .
 */

@Entity(name = "FUND")
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "BALANCE_AMOUNT")
    private BigDecimal balanceAmount;

    @Column(name = "TRANSACTION_AMOUNT")
    private BigDecimal transactionAmount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "TRANSACTION_TIME")
    private Date transactionTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Override
    public boolean equals(Object obj) {
        return (this.getUserId() == ((Fund) obj).getUserId() && this.getCurrency() == ((Fund) obj).getCurrency());
    }
}
