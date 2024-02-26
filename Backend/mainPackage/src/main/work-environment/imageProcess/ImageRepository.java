package com.cs309.ta45.backend.mainPackage.imageProcess;


import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<Image,Long> {
    public Image getImageById(int id);
    public Image getImageByFileName(String fileName);
}
