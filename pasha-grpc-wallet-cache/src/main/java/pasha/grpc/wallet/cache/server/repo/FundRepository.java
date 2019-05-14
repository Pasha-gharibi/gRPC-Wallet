package pasha.grpc.wallet.cache.server.repo;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pasha.grpc.wallet.cache.server.model.Fund;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface FundRepository extends CrudRepository<Fund, Long> {
    @Query(value = "select * from fund where id in ( select max(id ) from fund group by user_Id, currency ) ",nativeQuery = true)
    List<Fund> getLatestFunds();

 }
