package com.example.wealthratingms.slices.clients.db;

import com.example.wealthratingms.slices.clients.db.daos.ClientDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Repository
public interface ClientsRepository extends MongoRepository<ClientDao, Long> {
}
