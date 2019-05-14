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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Account getAccountByUserIdAndCurrency(Long userId,Currency currency);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Account> getAccountsByUser(User user);

}
