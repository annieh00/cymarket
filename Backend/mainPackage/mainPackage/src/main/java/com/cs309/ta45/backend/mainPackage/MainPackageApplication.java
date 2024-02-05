package com.cs309.ta45.backend.mainPackage;

import com.cs309.ta45.backend.dbmsPackage.ConnectToDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;


@SpringBootApplication
public class MainPackageApplication {

	public static void main(String[] args) {

		Connection connection =  null; //According to google: no multiple sessions over a single connection is allowed.
		ConnectToDB db = null;
		try{
			connection = ConnectToDB.getOneTimeConnection();
			connection.close();
			SpringApplication.run(MainPackageApplication.class, args);
		}catch(Exception e){
			e.printStackTrace();
		}


	}

}
