package com.busbooking.busbooking.repository;

import com.busbooking.busbooking.model.AppUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AppUserRepository extends ReactiveMongoRepository<AppUser, String> {
    Mono<AppUser> findByUsername(String username);
}
