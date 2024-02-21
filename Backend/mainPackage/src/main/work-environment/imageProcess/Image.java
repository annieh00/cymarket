package com.cs309.ta45.backend.mainPackage.imageProcess;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="images")
public class Image {
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "base64Encoding")
    private String base64Encoding;

    @Id
    @Column(name="id")
    private int id;

    public int getId(){
        return id;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64Encoding() {
        return base64Encoding;
    }

    public void setBase64Encoding(String base64Encoding) {
        this.base64Encoding = base64Encoding;
    }

}
