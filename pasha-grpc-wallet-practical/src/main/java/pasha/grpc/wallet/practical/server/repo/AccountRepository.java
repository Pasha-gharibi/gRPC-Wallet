package pasha.grpc.wallet.practical.server.repo;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pasha.grpc.wallet.practical.server.enm.Currency;
import pasha.grpc.wallet.practical.server.model.Account;
import pasha.grpc.wallet.practical.server.model.User;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * By default MySql uses REPEATABLE_READ as default isolation level. to Lock other transactions during READ we should use PESSIMISTIC_WRITE!
 */
@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {

    /**
     *  I conducted a performance benchmark for wallet client against both MySQL 5.7 and PostgreSQL 11.3 databases with both optimistic/pessimistic locking.
     *  It turned out that pessimistic locking performs better probably because the transactions are very short.
     *  Also PostgreSQL outperforms MySQL by a large margin but they want you to use MySQL.
     *  I got about 300 TPS vs 100 TPS with pessimistic locking.
     *  By default Account uses optimistic locking as it has set on Account Entity!
     */

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Account getAccountByUserIdAndCurrency(Long userId,Currency currency);

    List<Account> getAccountsByUser(User user);

}
