package pasha.grpc.wallet.practical.server.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import pasha.grpc.wallet.practical.server.enm.Currency;


@Entity(name = "T_TRANSACTION")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount ;

    @Column(columnDefinition = "TIMESTAMP(6) not null")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_USER")
    private User user;

    @ManyToOne(targetEntity = Account.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_ACCOUNT", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}

