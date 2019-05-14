package pasha.grpc.wallet.practical.server.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pasha.grpc.wallet.practical.server.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
}
