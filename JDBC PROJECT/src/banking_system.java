
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class banking_system {
     private static String url = "jdbc:mysql://localhost:3306/banking_system";
     private static String username = "root";
     private  static String password = "Hardik@123";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(url, username , password);
            System.out.println("Connection Established Successfully");
            Scanner scn = new Scanner(System.in);
            while (true) { 
                System.out.println(">>> Welcome to the Banking Management System <<<");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println();
                System.out.print("Enter Choice: ");
                int choice = scn.nextInt();
                switch(choice){
                    case 1:
                        register(con, scn);
                        break;
                    case 2:
                        login(con, scn);
                        break;
                    case 3:
                            System.out.println("Thank You For Using Banking System!!");
                            System.out.println("Exiting System.");
                            return;
                            default: System.out.println("Invalid Choice");
                }
                
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void register(Connection con, Scanner scn){
        scn.nextLine();
        System.out.print("Enter Full Name: ");
        String full_name = scn.nextLine();
        System.out.print("Enter Email: ");
        String email = scn.nextLine();
        System.out.print("Enter Password: ");
        String password = scn.nextLine();
        try {

            if(userExist(con, email)){
            System.out.println("User Already Exist.");
            return;
        }
        String insertQuery = "INSERT INTO user(full_name, email, password) VALUES (?, ?, ?) ";
        PreparedStatement preStm = con.prepareStatement(insertQuery);
        preStm.setString(1, full_name);
        preStm.setString(2, email);
        preStm.setString(3, password);
        
        int rowAffected = preStm.executeUpdate();
        if(rowAffected>0){
            System.out.println("Registration Successful");
        }
        else{
            System.out.println("Registration Failed");
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        }

        public static String login(Connection con, Scanner scn){
        scn.nextLine();
        System.out.print("Enter Email: ");
        String email = scn.nextLine();
        System.out.print("Enter Password: ");
        String password = scn.nextLine();
        if(!userExist(con, email)){
            System.out.println("No user found, Please Register first.");
            return null;
        }
        String query = "SELECT * FROM user WHERE Email = ? AND Password = ?;";
        try {
            PreparedStatement preStm = con.prepareStatement(query);
            preStm.setString(1, email);
            preStm.setString(2, password);
            ResultSet rst = preStm.executeQuery();
            if(rst.next()){
            System.out.println("Login Successfully");
        }
        else {
            System.out.println("Invalid Email or Password");
            return null;
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        try {
            if(!accountExist(con, email)){
                System.out.println("1. Open Account");
                System.out.println("2. Exit");
                if(scn.nextInt()==1){
                    openAccount(con, scn, email);
                }
                else{
                    System.out.println("Thank you.");
                    return null;
                }
            }
            else{

                while(true){
                System.out.println("1. Debit Money");
                System.out.println("2. Credit Money");
                System.out.println("3. Transfer Money");
                System.out.println("4. Get Account Balance");
                System.out.println("5. Logout");
                System.out.print("Enter Choice: ");
                int choice = scn.nextInt();
                switch(choice){
                    case 1:
                        debit_money(con, scn);
                        break;
                    case 2: 
                        credit_money(con, scn);
                        break;
                    case 3:
                        transfer_money(con, scn);
                        break;
                    case 4:
                        getBalance(con, scn);
                        break;
                    case 5:
                        break;
                        default:
                            System.out.println("Invalid Choice.");   
                }
                if(choice==5){
                    break;
                }
            }
                
            }
        } catch (Exception e) {
        }
        return null;
        }
        public static void exit(){

        }
        public static boolean userExist(Connection con, String email){
            String user_exist = "SELECT Email FROM user WHERE Email = ?;";
            try {
                PreparedStatement preStm= con.prepareStatement(user_exist);
                preStm.setString(1, email);
            ResultSet rst = preStm.executeQuery();
            if(rst.next()){
                return true;
            }
            else{
                return false;
            }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return false;
        }
        public static boolean accountExist(Connection con, String email){
            String accountQuery = "SELECT * FROM accounts WHERE Email = ?;";
            try {
                PreparedStatement preStm = con.prepareStatement(accountQuery);
                preStm.setString(1, email);
                ResultSet rst = preStm.executeQuery();
                return rst.next();
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return false;
        }
        public static void openAccount(Connection con ,  Scanner scn, String Email){
            String openAccountQuery = "INSERT INTO accounts(Full_Name, Email, Balance, Security_Pin) VALUES (?, ?, ?, ?); ";
            scn.nextLine();
            System.out.println("Enter Full Name: ");
            String fullName = scn.nextLine();
            System.out.println("Enter Balance: ");
            double balance = scn.nextDouble();
            scn.nextLine();
            System.out.println("Enter Security Pin: ");
            String securityPin = scn.nextLine();
            try {
                PreparedStatement preStm = con.prepareStatement(openAccountQuery);
                preStm.setString(1, fullName);
                preStm.setString(2, Email);
                preStm.setDouble(3, balance);
                preStm.setString(4, securityPin);
                
                int rowAffected = preStm.executeUpdate();
                if(rowAffected>0){
                    System.out.println("Account Open Successfully");
                }
                else{
                    System.out.println("Account Creation Failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public static void debit_money(Connection con, Scanner scn){
            System.out.print("Enter your Account Number: ");
            long account_number = scn.nextLong();
            scn.nextLine();
            System.out.print("Enter your Security Pin: ");
            String securityPin = scn.nextLine();
            System.out.print("Enter Amount: ");
            double Amount = scn.nextDouble();
            try {
                
                if(account_number!=0){
                    con.setAutoCommit(false);
                    String balanceQuery = "SELECT Balance FROM accounts WHERE Account_Number = ? AND Security_Pin = ?;";
                    PreparedStatement preStm = con.prepareStatement(balanceQuery);
                    preStm.setLong(1, account_number);
                    preStm.setString(2, securityPin);
                    ResultSet rst = preStm.executeQuery();
                    if(rst.next()){
                        Double currentBalance = rst.getDouble("Balance");
                        if(Amount<=currentBalance && Amount>=0){
                        String debitAmount = "UPDATE accounts SET Balance = Balance-? WHERE Account_Number = ?";
                        PreparedStatement preStm1 = con.prepareStatement(debitAmount);
                        preStm1.setDouble(1, Amount);
                        preStm1.setLong(2, account_number);
                        int rowAffected = preStm1.executeUpdate();
                        if(rowAffected>0){
                            System.out.println("Amount Debited Successfully");
                            con.commit();
                        }
                        else{
                           con.rollback();
                           System.out.println("Amount Not Debited"); 
                        }
                        con.setAutoCommit(true);
                        preStm1.close();
            }
                    else{
                        System.out.println("Insufficient Balance.");
                    }
                    preStm.close();
                    }
                    else{
                        System.out.println("Invalid Security Pin.");
                    }
                }
                else{
                    System.out.println("Invalid Account Number");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }   
        }
        public static void credit_money(Connection con, Scanner scn){
            System.out.print("Enter your Account Number: ");
            long account_number = scn.nextLong();
            scn.nextLine();
            System.out.print("Enter your Security Pin: ");
            String securityPin = scn.nextLine();
            System.out.print("Enter Amount: ");
            double Amount = scn.nextDouble();
            try {
                
                if(account_number!=0 && Amount>=0){
                    con.setAutoCommit(false);
                        String debitAmount = "UPDATE accounts SET Balance = Balance + ? WHERE Account_Number = ?";
                        PreparedStatement preStm = con.prepareStatement(debitAmount);
                        preStm.setDouble(1, Amount);
                        preStm.setLong(2, account_number);;
                        int rowAffected = preStm.executeUpdate();
                        if(rowAffected>0){
                            System.out.println("Amount Credit Successfully");
                            con.commit();
                        }
                        else{
                           con.rollback();
                           System.out.println("Amount Not Credit"); 
                        }
                        con.setAutoCommit(true);
                        preStm.close();
                    }    
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
        public static void transfer_money(Connection con, Scanner scn){
            System.out.print("Enter your Account Number: ");
            long senderAccountNumber = scn.nextLong();
            scn.nextLine();
            System.out.print("Enter your Security Pin: ");
            String senderSecurityPin = scn.nextLine();
            System.out.print("Enter Receiver Account Number: ");
            long receiverAccountNumber = scn.nextLong();
            System.out.print("Enter Amount: ");
            double Amount = scn.nextDouble();
            String debitQuery = "UPDATE accounts SET Balance = Balance - ? WHERE Account_Number = ?";
            String creditQuery = "UPDATE accounts SET Balance = Balance + ? WHERE Account_Number = ?";
            try {
                if(senderAccountNumber!=0 && receiverAccountNumber!=0){
                    con.setAutoCommit(false);
                    String balanceQuery = "SELECT Balance FROM accounts WHERE Account_Number = ? AND Security_Pin = ?;";
                    PreparedStatement preStm = con.prepareStatement(balanceQuery);
                    preStm.setLong(1, senderAccountNumber);
                    preStm.setString(2, senderSecurityPin);
                    ResultSet rst = preStm.executeQuery();
                    if(rst.next()){
                    Double currentBalance = rst.getDouble("Balance");
                    if(Amount<=currentBalance){
                    PreparedStatement preStmDebit = con.prepareStatement(debitQuery);
                    preStmDebit.setDouble(1, Amount);
                    preStmDebit.setLong(2, senderAccountNumber);
                    PreparedStatement preStmCredit = con.prepareStatement(creditQuery);
                    preStmCredit.setDouble(1, Amount);
                    preStmCredit.setLong(2, receiverAccountNumber);
                    int debitRowAffected = preStmDebit.executeUpdate();
                    int creditRowAffected = preStmCredit.executeUpdate();

                    if(debitRowAffected>0 && creditRowAffected>0){
                    System.out.println("Transfer Successful");
                    con.commit();
                }
            else{
                con.rollback();
                System.out.println("Transfer Failed");
            }
            con.setAutoCommit(true);
            preStmDebit.close();
            preStmCredit.close();
                }
                else{
                    System.out.println("Insufficient Balance");
                }
                preStm.close();
                rst.close();
            }
            else{
                System.out.println("Invalid Security Pin");
            }
                    }
                    
                else{
                    System.out.println("Invalid Account Number");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public static void getBalance(Connection con, Scanner scn){
            System.out.print("Enter your Account Number: ");
            long account_number = scn.nextLong();
            scn.nextLine();
            System.out.print("Enter your Security Pin: ");
            String securityPin = scn.nextLine();

            String getBalanceQuery = "SELECT Balance FROM accounts WHERE Account_Number = ? AND Security_Pin = ?;";
            try {
                PreparedStatement preStm = con.prepareStatement(getBalanceQuery);
                preStm.setLong(1, account_number);
                preStm.setString(2, securityPin);
                ResultSet rst = preStm.executeQuery();
                if(rst.next()){
                    Double balance = rst.getDouble("Balance");
                    System.out.println("Balance: " + balance);
                }
                else{
                    System.out.println("Invalid Account Number or Security Pin.");
                }
                preStm.close();
                rst.close();
            } catch (Exception e) {
            }
        }
    }

    
    