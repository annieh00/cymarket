package com.cs309.ta45.backend.mainPackage.usersPackage;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PostingRepository extends CrudRepository<Posting,Long> {
    ArrayList<Posting> findPostingByAuthor(String author);
    Posting findPostingById(int id);
}
