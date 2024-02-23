package com.cs309.ta45.backend.mainPackage.usersPackage;

import org.springframework.data.repository.CrudRepository;
/**
 * @author Junhyung Shim
 * */
public interface Post2UserMappingRepository extends CrudRepository<Post2UserMapping,Long> {
    public Post2UserMapping findPost2UserMappingByPidAndUid(int pid, String uid);
}
