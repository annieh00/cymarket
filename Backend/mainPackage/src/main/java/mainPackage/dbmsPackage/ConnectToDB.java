package mainPackage.dbmsPackage;

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
    public static Connection getOneTimeConnection() {
        Connection ret = null;
    
        try {
            String url = System.getenv("DB_URL");
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");
    
            ret = DriverManager.getConnection(url, username, password);
            return ret;
        } catch (SQLException ex) {
            System.out.println("Error at ConnectToDB.getOneTimeConnection()");
        }
    
        return null;
    }

}
