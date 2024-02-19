package com.cs309.ta45.backend.mainPackage.imageProcess;

import com.cs309.ta45.backend.mainPackage.usersPackage.GeneralUser;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image,Long> {
    public Image getImageById(int id);
    public Image getImageByFileName(String fileName);
}
