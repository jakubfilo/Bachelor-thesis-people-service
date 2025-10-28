package com.jakubfilo.peopleservice.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jakubfilo.peopleservice.db.dbo.NotificationDbo;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationDbo, String> {
}
