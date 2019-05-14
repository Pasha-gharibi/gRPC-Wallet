package pasha.grpc.wallet.practical.server.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pasha.grpc.wallet.practical.server.model.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {

}
