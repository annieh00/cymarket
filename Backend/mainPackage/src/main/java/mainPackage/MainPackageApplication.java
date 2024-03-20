package mainPackage;

import mainPackage.dbmsPackage.ConnectToDB;

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
			if(connection == null){
				System.out.println("something is wrong with db");
			}else{
				connection.close();
			}
			SpringApplication.run(MainPackageApplication.class, args);
			//SpringApplication.run(DirectChat.class, args);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
