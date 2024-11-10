import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;




class BankingwithSQL{
   static String name;
   static String AccountNo;
   static int Balance;
   static int age;
   static String Address;
   static String Sex;
   static int CreditSc;
   static String phone;
   static String email;
   static int pin;
   static int Response = 0;

   static String url = "jdbc:mysql://localhost:3306/BankingSystem";
   static String user = "root";
   static String password = "22vishal";

   static Scanner scan = new Scanner(System.in);

   public static void main(String[] args) {
       options();
       System.out.println("\n");
       do{
        System.out.print("Enter your choice:");
        
        Response = scan.nextInt();
        if(Response > 7 || Response < 1){
            System.out.println("Try again with valid response!!!");
        }
       }while(Response > 7 || Response < 1);
 
       switch(Response){
        case 1:AccountDetail();break;
        case 2:BalanceDet();break;
        case 3:Withdraw();break;
        case 4:Deposit();break;
        case 5:Update();break;
        case 6:create();break;
        case 7:Delete();break;
       }
   }
    protected static void options() {
       System.out.println("Enter the number against which your work lies:");
       System.out.println("1.Account Details");
       System.out.println("2.Balance Details");
       System.out.println("3:Withdraw Cash");
       System.out.println("4.Deposite you money");
       System.out.println("5.Update Details ");
       System.out.println("6.Create new Account");
       System.out.println("7.Delete your existing account");
   }
    protected  static void create(){
        try(Connection conn = DriverManager.getConnection(url,user,password)){
            String sql = "INSERT INTO BankingSys values(?,?,?,?,?,?,?,?,?,?)"; //here '?' question marks work as a placehoder for value to be assigned letter
            PreparedStatement pstmt = conn.prepareStatement(sql);

            String ch = "YES";
            
            while(!ch.equals("NO")){
            System.out.println("Now We are creating your account just follow the following step and eneter details:");
            Scanner scan = new Scanner(System.in);

            Random random = new Random();
            AccountNo = "1000000" + random.nextInt(0,100000);
            System.out.println("We will allot a Account number for you!!!");
            pstmt.setString(1,AccountNo); //This assign values to '?' in the command to be executed store in String sql,it take index of '?' as first then assign value that is store as seconf parameter
            
            System.out.print("Enter your Name:");
            name = scan.next().toUpperCase();
            pstmt.setString(2,name);

            System.out.print("Enter the deposite at opening:");
            Balance = scan.nextInt();
            pstmt.setInt(3,Balance);

            System.out.print("Enter your Age:");
            age = scan.nextInt();
            pstmt.setInt(4,age);

            System.out.print("Enter your Sex:");
            Sex = scan.next().toUpperCase();
            pstmt.setString(5,Sex);

            System.out.print("Enter your Address:");
            Address = scan.next().toUpperCase();
            pstmt.setString(6,Address);

            System.out.print("Enter your phone Number:");
            phone = scan.next();
            pstmt.setString(7,phone);

            System.out.print("Enter your Email:");
            email = scan.next().toUpperCase();
            pstmt.setString(8,email);

            System.out.print("Create a pin:");
            pin = scan.nextInt();
            pstmt.setInt(9,pin);

            System.out.print("We'll keep upadating your credit score!!");
            CreditSc = random.nextInt(0,10);
            pstmt.setInt(10,CreditSc);
            
            pstmt.executeUpdate();
            System.out.print("Would you like to create more accounts:");
            ch = scan.next().toUpperCase();}

            scan.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    protected static void AccountDetail(){
        try(Connection conn = DriverManager.getConnection(url,user,password)){
            String sql = "Select * from BankingSys Where AccountNo = ? and Pin = ? ";

            PreparedStatement prep = conn.prepareStatement(sql);
            

            System.err.print("Enter Your Account : ");
            String account = scan.next();
            System.out.print("Enter Your Pin     : ");
            int pinny = scan.nextInt();
            
            prep.setString(1,account);
            prep.setInt(2,pinny);

            ResultSet rs = prep.executeQuery();
            if(rs.next()){
                System.out.println("Account Number : " + rs.getString(1));
                System.out.println("Name           : " + rs.getString(2));
                System.out.println("Balance        : " + rs.getString(3));
                System.out.println("Age            : " + rs.getString(4));
                System.out.println("Sex            : " + rs.getString(5));
                System.out.println("Address        : " + rs.getString(6));
                System.out.println("Phone Number   : " + rs.getString(7));
                System.out.println("Email          : " + rs.getString(8));
                System.out.println("PIN            : " + "******");
                System.out.println("Credit Score   : " + rs.getString(10));
            }
            else{
                System.out.println("Retry with correct credentials!!Rerun the Program to Retry!!!");
            }
            scan.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    protected static void Withdraw(){
        try(Connection conn = DriverManager.getConnection(url,user,password)){
            String sql = "Select * From BankingSys WHERE AccountNo = ? and Pin = ?";
            PreparedStatement prep = conn.prepareStatement(sql);

            System.out.println("Enter You Account No : ");
            String account  = scan.next();
            System.out.println("Enter Your Pin       : ");
            int pinny = scan.nextInt();


            prep.setString(1,account);
            prep.setInt(2,pinny);

            ResultSet rs = prep.executeQuery();
            if(rs.next()){
                System.out.println("Enter Amount to be withdrawn : ");
                int withdrawl = scan.nextInt();

                String sqlnext = "UPDATE BankingSys SET Balance = ? WHERE AccountNo = ? and Pin = ?";
                
                int update = rs.getInt("Balance");

                PreparedStatement prep1 = conn.prepareStatement(sqlnext);
                int finalbal = update - withdrawl;
                if(finalbal >= 0){prep1.setInt(1,finalbal);}
                else{
                    System.out.println("Unsufficient Balance!!!");
                    prep1.setInt(1,update);
                }
                
                prep1.setString(2, account);
                prep1.setInt(3,pinny);

                prep1.executeUpdate();

                PreparedStatement prep2 = conn.prepareStatement("Select Balance from BankingSys where AccountNo = ? and Pin = ?");

                prep2.setString(1,account);
                prep2.setInt(2,pinny);
                
                ResultSet rs1 = prep2.executeQuery();

                rs1.next();
                
                System.out.println("\nRemaining Balance : " + rs1.getInt("Balance"));
            }
            else{
                System.out.println("No such Data was found!!!");
            }
            scan.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    protected static void BalanceDet(){
        try(Connection conn = DriverManager.getConnection(url,user,password)){
            String sql = "Select AccountNo,Name,Balance from BankingSys where AccountNo = ? and Pin = ?";
            PreparedStatement prep = conn.prepareStatement(sql);
            
            System.out.println("Enter You Account No : ");
            String account = scan.next();
            System.out.println("Enter Your Pin       : ");
            int pinny = scan.nextInt();

            prep.setString(1,account);
            prep.setInt(2,pinny);

            ResultSet rs = prep.executeQuery();
            if(rs.next()){
                System.out.println("Account_No : "+rs.getString("AccountNo")
                                +"\nName       : "+rs.getString("Name") 
                                +"\nBalance    : "+rs.getInt("Balance"));
            }
            else{
                System.out.println("No Matching Data Found!!");
            }
            scan.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    protected static  void Deposit(){
        try(Connection conn = DriverManager.getConnection(url,user,password)){
            String sql = "Select * From BankingSys WHERE AccountNo = ? and Pin = ?";
            PreparedStatement prep = conn.prepareStatement(sql);

            System.out.println("Enter You Account No : ");
            String account  = scan.next();
            System.out.println("Enter Your Pin       : ");
            int pinny = scan.nextInt();


            prep.setString(1,account);
            prep.setInt(2,pinny);

            ResultSet rs = prep.executeQuery();
            if(rs.next()){
                System.out.println("Enter Amount to be Deposited : ");
                int Deposit = scan.nextInt();

                String sqlnext = "UPDATE BankingSys SET Balance = ? WHERE AccountNo = ? and Pin = ?";

                int update = rs.getInt("Balance");

                PreparedStatement prep1 = conn.prepareStatement(sqlnext);

                prep1.setInt(1,update + Deposit);
                prep1.setString(2, account);
                prep1.setInt(3,pinny);

                prep1.executeUpdate();

                PreparedStatement prep2 = conn.prepareStatement("Select Balance from BankingSys where AccountNo = ? and Pin = ?");

                prep2.setString(1,account);
                prep2.setInt(2,pinny);
                
                ResultSet rs1 = prep2.executeQuery();

                rs1.next();
                
                System.out.println("\nRemaining Balance : " + rs1.getInt("Balance"));
            }
            else{
                System.out.println("No such Data was found!!!");
            }
            scan.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    protected static void Update(){
        try(Connection conn = DriverManager.getConnection(url,user,password)){
            PreparedStatement prepa = conn.prepareStatement("Select * from BankingSys where AccountNo= ? and Pin = ?");

            System.out.println("Enter Your Account No. :");
            String account = scan.next();
            System.out.println("Enter Your Pin         :");
            int pinny = scan.nextInt();

            prepa.setString(1,account);
            prepa.setInt(2,pinny);

            ResultSet rs = prepa.executeQuery();

            if(rs.next()){
                System.out.println("If you want to Update the field Enter the new Information otherwise leave it blank and Press Enter !!!");
                PreparedStatement prepb = conn.prepareStatement("Update BankingSys Set Name = ? , Age = ? , Sex = ? , Address = ? , Phone = ? , Email = ? , Pin = ? where AccountNo = ? and Pin = ?");
                prepb.setString(8,account);
                prepb.setInt(9, pinny);

                System.out.print("Enter your Name:");
                scan.nextLine();
                name = scan.nextLine().trim().toUpperCase();
                if(name.isEmpty()){
                    prepb.setString(1,rs.getString("Name"));
                }
                else{prepb.setString(1,name);}
                
    
                System.out.print("Enter your Age:");
                //scan.nextLine();
                String strAge = scan.nextLine().trim().toUpperCase();
                age = Integer.parseInt(strAge);
                if(strAge.isBlank()){
                    prepb.setInt(2,rs.getInt("Age"));
                }
                else{prepb.setInt(2,age);}
    
                System.out.print("Enter your Sex:");
                Sex = scan.nextLine().trim().toUpperCase();
                if(Sex.isEmpty()){
                    prepb.setString(3,rs.getString("Sex"));
                }
                else{prepb.setString(3,Sex);}
    
                System.out.print("Enter your Address:");
                Address = scan.nextLine().trim().toUpperCase();
                if(Address.isEmpty()){
                    prepb.setString(4,rs.getString("Address"));
                }
                else{prepb.setString(4,Address);}
    
                System.out.print("Enter your phone Number:");
                phone = scan.nextLine().trim();
                if(phone.isEmpty()){
                    prepb.setString(5,rs.getString("Phone"));
                }
                else{prepb.setString(5,phone);}
    
                System.out.print("Enter your Email:");
                email = scan.nextLine().trim().toUpperCase();
                if(email.isBlank()){
                    prepb.setString(6,rs.getString("Email"));
                }
                else{prepb.setString(6,email);}
    
                System.out.print("Create a pin:");
                String strPin= scan.nextLine().trim().toUpperCase();
                pin = Integer.parseInt(strPin);
                if(strAge.isEmpty()){
                    prepb.setInt(7,rs.getInt("Pin"));
                }
                else{prepb.setInt(7,pin);}            
                prepb.executeUpdate();
            }
            else{
                System.out.println("Enter valid Account No and PIN !!!");
            }
            scan.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    protected static void Delete(){
    try(Connection conn = DriverManager.getConnection(url,user,password)){
        PreparedStatement prep = conn.prepareStatement("Delete from BankingSys where AccountNo = ? and Pin = ?");

        System.out.println("Enter Your Account NO :");
        String account = scan.next();

        System.out.println("Enter Your No         : ");
        int pinny = scan.nextInt();

        prep.setString(1, account);
        prep.setInt(2, pinny);

        int Resut = prep.executeUpdate();

        if(Resut > 0){
            System.out.println("Deleted Data Successfully");
        }
        else{
            System.out.println("No matching data found!!");
        }
        scan.close();
    }
    catch(SQLException e){
        e.printStackTrace();

    }

    }
}
