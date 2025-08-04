package com.busbooking.busbooking.repository;

import com.busbooking.busbooking.model.Bus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends ReactiveMongoRepository<Bus, String> {
}
