import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String pass = "21Ebkcs@21";
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver mast");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
         String email;
        long accountNo;
        try{
            Connection con  = DriverManager.getConnection(url,username,pass);
            Scanner sc = new Scanner(System.in);
            User user = new User(con,sc);
            Accounts accounts = new Accounts(con, sc);
            AccountManager accountManager = new AccountManager(con,sc);


            while(true){
                System.out.println("Welcome To Our Banking System: ");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter Your Choice : ");
                int choice = sc.nextInt();
                    switch (choice){
                        case 1:
                            user.register();
                            break;
                        case 2:
                           email = user.login();
                           if(user.userExist(email)) {
                                   System.out.println();
                                   System.out.println("USER LOGGED IN");

                                   if (!accounts.accountexist(email)) {
                                       System.out.println("1. Open Account");
                                       System.out.println("2. Exit");
                                       System.out.println("Enter Your Choice :");
                                       int choose = sc.nextInt();
                                       switch (choose) {
                                           case 1:
                                               accountNo = accounts.openaccount(email);
                                               System.out.println("Account Created Successfully");
                                               System.out.println("Your AccountNo is-" + accountNo);
                                               break;
                                           case 2:
                                               return;
                                       }
                                   }

                               accountNo = accounts.getaccount_number(email);
                               System.out.println();
                               while (true) {
                                   System.out.println("1. Credit Money");
                                   System.out.println("1. Debit Money");
                                   System.out.println("3. Transfer Money");
                                   System.out.println("4. Check Balance");
                                   System.out.println("5. Exit");
                                   System.out.print("Enter Your Choice ");
                                   int choicee = sc.nextInt();
                                   switch (choicee) {
                                       case 1:
                                           accountManager.credit_money(accountNo);
                                           break;
                                       case 2:
                                           accountManager.debitMoney(accountNo);
                                           break;
                                       case 3:
                                           accountManager.transfer_money(accountNo);
                                           break;
                                       case 4:
                                           accountManager.getBalance(accountNo);
                                           break;
                                       case 5:
                                           return;
                                   }

                               }

                           }
                            System.out.println("User Don't Exist");
                           break;
                        case 3:
                            return;
                    }
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
