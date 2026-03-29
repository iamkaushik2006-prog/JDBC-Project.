import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class hotelReservationManagement {
    private static String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static String userName = "root";
    private static String password = "Hardik@123";
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(url, userName, password);
            Statement stm = con.createStatement();
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println();
                System.out.println("1. Reserve A Room ");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.println("Choose Option: ");
                Scanner scn = new Scanner(System.in);
                int choice = scn.nextInt();
                switch(choice){
                    case 1 :
                        reserveAroom(con,stm,scn);
                        break;
                    case 2 :
                        viewReservations(con, stm);
                        break;
                    case 3 :
                        getRoomNumber(con, stm, scn);
                        break;
                    case 4 :
                        updateReservations(con, stm, scn);
                        break;
                    case 5 :
                        deleteReservations(con, stm, scn);
                        break;
                    case 0 :
                        con.close();
                        stm.close();
                        exit();
                        return;
                    default:
                        System.out.println("Invalid Choice");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void reserveAroom(Connection con, Statement stm, Scanner scn) {
        System.out.println("Enter guest_name");
        String guest_name = scn.next();
        scn.nextLine();
        System.out.println("Enter contact_number");
        String contact_number = scn.next();
        System.out.println("Enter the room_number");
        int room_number = scn.nextInt();
        String query = "INSERT INTO reservation(guest_name, contact_number, room_number) VALUES ('" + guest_name + "'," + contact_number + "," + room_number + ")";
        try {
            int rowsAffected = stm.executeUpdate(query);
            if(rowsAffected>0){
                System.out.println("Reservation Successful");
            }
            else{
                System.out.println("Reservation Failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void viewReservations(Connection con, Statement stm) {
       String query =  "SELECT * FROM reservation;";
       try {
           ResultSet rst = stm.executeQuery(query);
           while(rst.next()){
            int reservation_id = rst.getInt("reservation_id");
            String guest_name = rst.getString("guest_name");
            int room_number = rst.getInt("room_number");
            String contact_number = rst.getString("contact_number");
            String reservation_date = rst.getString("reservation_date").toString();
            System.out.println("Reservation ID: "+ reservation_id);
            System.out.println("Guest Name: " + guest_name);
            System.out.println("Room Number: " +  room_number);
            System.out.println("Contact Number: "+ contact_number);
            System.out.println("Reservation Date: " + reservation_date );
            System.out.println();
           }
           rst.close();
       } catch (SQLException e) {
        System.out.println(e.getMessage());
       }
    }
    
    public static void getRoomNumber(Connection con, Statement stm, Scanner scn) {
        System.out.println("Enter reservation_id");
        int reservationId = scn.nextInt();

        String query = "SELECT room_number FROM reservation"  + " " + "WHERE reservation_id = " + reservationId;
        try {
            ResultSet rst = stm.executeQuery(query);
            if(rst.next()){
                int room_number = rst.getInt("room_number");
                System.out.println("The room_number of reservationId"+" " + reservationId +" "+ "is: " + room_number);
            }
            else{
                System.out.println("Reservation Not Found");
            }
            rst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateReservations(Connection con, Statement stm, Scanner scn) {
        System.out.println("Enter reservation_id");
        int reservationId = scn.nextInt();

        if(!reservationExist(con, stm, reservationId)){
            System.out.println("Reservation not found");
            return;
        }
        System.out.println("Enter newGuest_name");
        String newGuest_name = scn.next();
        scn.nextLine();
        System.out.println("Enter newContact_number");
        String newContact_number = scn.next();
        scn.nextLine();
        System.out.println("Enter the newRoom_number");
        int newRoom_number = scn.nextInt();
        scn.nextLine();
        String query = "UPDATE reservation SET guest_name = '"+ newGuest_name+ "', contact_number = '"+ newContact_number + "', room_number = "+ newRoom_number  + " "+ "WHERE reservation_id = " + reservationId;
        try {
            int rowAffected = stm.executeUpdate(query);
            if(rowAffected>0){
                System.out.println("Updated Successfully");
            }
            else{
                System.out.println("Update Unsuccessful");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void deleteReservations(Connection con, Statement stm, Scanner scn) {
        System.out.println("Enter reservation_id");
        int reservationId = scn.nextInt();
        if(!reservationExist(con, stm,  reservationId)){
            System.out.println("No Guest found");
        }
        String query = "DELETE FROM reservation Where reservation_id="+ reservationId;
        try {
            int rowAffected = stm.executeUpdate(query);
            if(rowAffected>0){
                System.out.println("Deleted Succussfully");
            }
            else{
                System.out.println("Deletion Failed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    
    public static void exit() {
        System.out.print("Exit System");
        int i = 0;
        while(i<5){
            
            try {
                System.out.print(".");
                Thread.sleep(450);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            i++;
        }
        System.out.println();
        System.out.println();
        System.out.println("Thank you for using Hotel Management System!");
    }
    public static boolean reservationExist (Connection con, Statement stm, int reservationId){
            try {
                String query = "SELECT reservation_id FROM reservation WHERE reservation_id="+ reservationId;
                ResultSet rst = stm.executeQuery(query);
                return rst.next();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }
}

