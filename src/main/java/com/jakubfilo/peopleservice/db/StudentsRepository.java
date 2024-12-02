package com.jakubfilo.peopleservice.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jakubfilo.peopleservice.db.dbo.StudentDbo;

@Repository
public interface StudentsRepository extends MongoRepository<StudentDbo, String> {
}
