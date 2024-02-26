import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection con;
    private   Scanner sc;

    public Accounts(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    //operations of accounts class
    public long openaccount(String email){
        if(!accountexist(email)){
            String open_account_query = "INSERT INTO Accounts(accountNo, fullName, email, balance, pin) VALUES(?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = sc.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = sc.nextLine();
            try {
                long account_number = genaccount_number(email);
                PreparedStatement preparedStatement = con.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation failed!!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist");
        }

    public long getaccount_number(String email){
      String query = "select accountno from accounts where email = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getLong("accountNo");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account does not exist");
    }

    public long genaccount_number(String email){
        try {
            Statement stmt = con.createStatement();
            String query = "select accountNo from accounts order by accountNo desc limit 1";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                long lastaccount_no = rs.getLong("accountNo");
                return lastaccount_no +1;
            }else{
                return 10000100;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean accountexist(String email){
        String query = "select * from accounts where email = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
