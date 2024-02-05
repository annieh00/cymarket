package com.cs309.ta45.backend.dbmsPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Junhyung_Shim
 * */
public class ConnectToDB {

    private Connection connection;

    public ConnectToDB(Connection connect){
        connection = connect;
    }

    /**
     * @return Connection connection
     * This method returns a connection to dbms. It is to access and update the dbms
     * Caller should call .closeConnection() to close the saved connection
     *
     * */
    public Connection getCurrentConnection(){
        return connection;
    }

    /**
     * @return boolean safelyClosed
     * This method closes the saved connection
     * returns true if successfully closed
     * returns false otherwise
     * */
    public boolean closeConnection(){
        try{
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return Connection connection
     * This method returns a one time connection to dbms to check if connection works
     * before the application starts
     * It is the caller's job to manually close the connection returned by this method
     * */
    public static Connection getOneTimeConnection() throws SQLException{
        Connection ret = null;
        try{
            ret = DriverManager.getConnection("jdbc:mysql://localhost/cs309","cs309","cs309ta45");
            return ret;
        }catch(SQLException ex){
            System.out.println("Error at ConnectToDB.getConnection()");
            throw ex;
        }

    }

}
