package serverDB;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;


public class Database {
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    public void read(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradingsimulator?useSSL=false&serverTimezone=UTC", "ts",
                    "password");
            preparedStatement = connection.prepareStatement("SELECT * FROM userinfo WHERE username='demo'");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.println(resultSet.getString("username"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param username The username to check
     * @return
     * 1 if the username has appeared in the database
     * 0 if the username is not in the database
     * -1 if errors occur
     */
    public int checkUsernameRepeat(String username){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradingsimulator?useSSL=false&serverTimezone=UTC", "ts",
                    "password");
            preparedStatement = connection.prepareStatement("SELECT * FROM userinfo WHERE username=?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return 1;
            }
            else{
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getPassword(String username){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradingsimulator?useSSL=false&serverTimezone=UTC", "ts",
                    "password");
            preparedStatement = connection.prepareStatement("SELECT password FROM userinfo WHERE username=?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next() == false){
                return null;
            }
            return resultSet.getString("password");
        } catch (Exception e) {
            return null;
        }
    }

    public boolean putUserInfo(String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradingsimulator?useSSL=false&serverTimezone=UTC", "ts",
                    "password");
            preparedStatement = connection.prepareStatement("INSERT INTO userinfo VALUES (0,?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            return preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
