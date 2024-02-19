package com.cs309.ta45.backend.mainPackage.usersPackage;

import org.springframework.data.repository.CrudRepository;

public interface GeneralUserRepository extends CrudRepository<GeneralUser,Long> {
    public GeneralUser findGeneralUserByUserName(String uid);
}
