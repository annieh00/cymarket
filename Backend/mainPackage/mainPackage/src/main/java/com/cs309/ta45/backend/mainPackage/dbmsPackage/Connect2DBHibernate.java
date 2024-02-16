package com.cs309.ta45.backend.mainPackage.dbmsPackage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Connect2DBHibernate {
    private Session sess;
    private SessionFactory sessFact;
    public Session getSession(){
        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");
        ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        sessFact = config.buildSessionFactory(sr);
        sess = sessFact.getCurrentSession();
        return sess;
    }



}
