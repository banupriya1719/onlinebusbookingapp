package com.busbooking.busbooking.repository;

import com.busbooking.busbooking.model.Passenger;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PassengerRepository extends ReactiveMongoRepository<Passenger, String> {
}
