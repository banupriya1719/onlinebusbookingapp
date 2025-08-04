package com.busbooking.busbooking.repository;

import com.busbooking.busbooking.model.Booking;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends ReactiveMongoRepository<Booking, String> {
}
