package com.cs309.ta45.backend.mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
/**
 * @author Junhyung Shim
 * */
public interface PostingRepository extends JpaRepository<Posting,Long> {
    ArrayList<Posting> findPostingByUserName(String author);
    Posting findPostingById(int id);
}
