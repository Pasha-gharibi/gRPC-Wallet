package pasha.grpc.wallet.practical.server.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity(name = "T_TRANSACTION")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private BigDecimal amount ;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_USER")
    private User user;

    @ManyToOne(targetEntity = Account.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_ACCOUNT")
    private Account account;

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
}

