package com.cs309.ta45.backend.mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
/**
 * @author Junhyung Shim
 * */
public interface Post2UserMappingRepository extends JpaRepository<Post2UserMapping,Long> {
    public Post2UserMapping findPost2UserMappingByPidAndUid(int pid, String uid);
}
