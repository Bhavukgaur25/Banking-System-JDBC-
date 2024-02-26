import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/****
 *  1. getBalance();
 *  2. debitMoney();
 *  3. creditMoney();
 *  4. transferMoney();
 * ***/
public class AccountManager {
    private Connection con;
    private Scanner sc;

    public AccountManager(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount<=current_balance){

                        // Write debit and credit queries
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                        // Debit and Credit prepared Statements
                        PreparedStatement creditPreparedStatement = con.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = con.prepareStatement(debit_query);

                        // Set Values for debit and credit prepared statements
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs."+amount+" Transferred Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }else{
                System.out.println("Invalid account number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void credit_money(long account_number)throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = con.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs."+amount+" credited Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void getBalance(Long accountNo) {
        System.out.print("Enter Scurity Pin :-");
        int pass = sc.nextInt();
        String query = "select balance from accounts where accountNo = ? and pin = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setLong(1, accountNo);
            preparedStatement.setInt(2, pass);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                long balance = rs.getLong("balance");
                System.out.println("Balance :-" + balance);
            } else {
                System.out.println("Invalid Pin");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void debitMoney(Long accountNo) throws SQLException {
        con.setAutoCommit(false);
        System.out.print("Enter Amount :-");
        double amount = sc.nextLong();
        sc.nextLine();
        System.out.print("Enter Pin :-");
        int pass = sc.nextInt();
        String query = "select balance from accounts where accountNo = ? and pin = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setLong(1, accountNo);
            preparedStatement.setInt(2, pass);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                double balance = rs.getLong("balance");
                if(amount<=balance){
                    String queryy= "update accounts balance = balance-? where accountNo = ?";
                    preparedStatement.setDouble(1,amount);
                    preparedStatement.setDouble(2,accountNo);
                    int row = preparedStatement.executeUpdate();
                    if(row>1){
                        con.commit();
                        con.setAutoCommit(true);
                        System.out.println("Successfully Debited");
                    }else{
                        con.rollback();
                        con.setAutoCommit(true);
                        System.out.println("Transaction failed");
                    }
                }else {
                    System.out.println("Insufficient Balance ");
                }
            } else {
                System.out.println("Invalid Pin");
            }
        } catch (SQLException e) {

        }
    }
}