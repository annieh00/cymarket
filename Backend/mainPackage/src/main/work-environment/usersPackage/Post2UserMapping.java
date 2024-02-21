package com.cs309.ta45.backend.mainPackage.usersPackage;

import jakarta.persistence.*;
/**
 * @author Junhyung Shim
 * */
@Entity
@IdClass(P2UKey.class)
@Table(name = "postToUserMapping")
public class Post2UserMapping {
    @Id
    @Column(name = "pid")
    private int pid;

    @Id
    @Column(name = "uid")
    private String uid;


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
