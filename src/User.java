import java.sql.*;
import java.util.Scanner;

public class User {
private Connection con;
private Scanner sc;

public User(Connection con, Scanner sc){
    this.con = con;
    this.sc = sc;
}
public void register(){
    sc.nextLine();
    System.out.print("Full_Name: ");
    String name = sc.nextLine();
    System.out.print("Email: ");
    String email = sc.nextLine();
    System.out.print("Password: ");
    String pass = sc.nextLine();
    if(userExist(email)){
        System.out.println("USER ALREADY EXIST");
        return;
    }
    String query = "insert into user(name,email,password) values(?,?,?);";
    try{
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,email);
        preparedStatement.setString(3,pass);
        int rows = preparedStatement.executeUpdate();
        if(rows>0){
            System.out.println("registration successful");
        }else{
            System.out.println("Error Registration Failed");
        }
    }catch (SQLException e){
       e.printStackTrace();
    }
}

public String login(){
    sc.nextLine();
    System.out.print("Email: ");
    String email = sc.nextLine();
    System.out.print("Password: ");
    String pass = sc.nextLine();
    String query = "select*from user where email =? and password = ?";
    try{
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2,pass);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            return email;
        }else {
            return null;
        }
    }catch (SQLException e){
        e.printStackTrace();
    }
    return null;
}
public boolean userExist(String email){
    String query= "select*from user where email = ?";
    try {
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, email);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            return true;
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    return false;
}
}
