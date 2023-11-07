
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class createdv
{ //establish method for connecting databases
    static Connection con;
    public static Connection create() throws RuntimeException {   
        final String driver_class="com.mysql.cj.jdbc.Driver";
        try
        {
            Class.forName(driver_class);
            String user="root";
            String password="Ninshujain@0";
            String url="jdbc:mysql://localhost:3306/book";

            con=DriverManager.getConnection(url,user,password);
        }
        catch (SQLException s)
        {
            s.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }
}
class bookdalo{
    //method to add book to database
    public static boolean insertbook (Book bk){  
        boolean f = false;
    try{
        Connection con = createdv.create();
        String q = "insert into availbook(id, title, author) values(?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(q);
        pstmt.setInt(1, bk.getId());
        pstmt.setString(2, bk.getTitle());
        pstmt.setString(3, bk.getAuthor());

        pstmt.executeUpdate();
        f = true;
    }
    catch(Exception e){
        e.printStackTrace();
    } 
    
    return f;

    }
}

//Book and its attributes
class Book {
    private int id;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void borrow() {
        isAvailable = false;
    }

    public void returnBook() {
        isAvailable = true;
    }

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Not Available";
        return "Book ID: " + id + " | Title: " + title + " | Author: " + author + " | Status: " + status;
    }
}

class Library {
   
    // method to display the details of database bookavail 
    public void displayAllBooks() { 

           System.out.println("Library Catalog:");

         try{

        Connection con = createdv.create();
        String q = "select * from availbook";
        PreparedStatement pstmt = con.prepareStatement(q);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        System.out.println(id +" " + title +" "+ author );
       }
        
    }
    catch(Exception e){
        e.printStackTrace();
    } 
     

    }

    //method for finding book by its unique id in database
    public static Book findBookById(int id) {
                 try{
            
        Connection con = createdv.create();
        String q = "select * from availbook where id = ?";
        PreparedStatement pstmt = con.prepareStatement(q);
     
        pstmt.setInt(1, id);
         ResultSet rs = pstmt.executeQuery();
         while(rs.next()){
         String title = rs.getString("title");
         String author = rs.getString("author");
         System.out.println(id +" " + title +" "+ author );
         }
    }
    catch(Exception e){
        e.printStackTrace();
    } 
     

        return null;
    }
    // method to borrow book 
    public void borrowBook(int id, String borrower) {
        try{
         Connection con = createdv.create();
         String q2 = "select * from availbook where id = ?";
         PreparedStatement pstmt2 = con.prepareStatement(q2);
         String title=null ;
         String author =null;
     
         pstmt2.setInt(1, id);
         ResultSet rs = pstmt2.executeQuery();
         if(rs.next()){
         title = rs.getString("title");
         author = rs.getString("author");
        
         
 

        String q1 ="insert into returnbook(id, title , author, name2) values (?, ?, ?, ?)";
         PreparedStatement pstmt1= con.prepareStatement(q1);
         pstmt1.setInt(1,id);
         pstmt1.setString(2,title);
         pstmt1.setString(3,author);
         pstmt1.setString(4,borrower);
         pstmt1.executeUpdate();
         }
          String q = "delete  from availbook where id = ?";
          PreparedStatement pstmt = con.prepareStatement(q);
          pstmt.setInt(1, id);
          pstmt.executeUpdate();
       
        System.out.println("Borrowed Succesfully. ");
       
       
        
    }
    catch(Exception e){
        e.printStackTrace();
    } 
     

    }
    
    //method to return book
    public void returnBook(int id) { 
        try{
            
        Connection con = createdv.create();
        
        String q2 = "select * from returnbook where id = ?";
         PreparedStatement pstmt2 = con.prepareStatement(q2);
         String title=null ;
         String author =null;
         pstmt2.setInt(1, id);
         ResultSet rs = pstmt2.executeQuery();
         
         if(rs.next()){
         title = rs.getString("title");
         author = rs.getString("author");
         String q1 ="insert into availbook(id, title , author ) values (?, ?, ?)";
         PreparedStatement pstmt1= con.prepareStatement(q1);
         pstmt1.setInt(1,id);
         pstmt1.setString(2,title);
         pstmt1.setString(3,author);
         pstmt1.executeUpdate();
         }

          String q = "delete  from returnbook where id = ?";
          PreparedStatement pstmt = con.prepareStatement(q);
          pstmt.setInt(1, id);
          pstmt.executeUpdate();
         System.out.println("Returned Successfully.");
       
    }
    catch(Exception e){
        e.printStackTrace();
    } 
        
    }
}
//main class
public class here {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        //library management system and its menu
        while (true) {
            System.out.println("\nLibrary Management System Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Display All Available Books");
            System.out.println("3. Find Book by ID");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
 
         try{   
            int choice= Integer.parseInt(scanner.nextLine());
         
            

            switch (choice) {

                //choice 1 to add book
                case 1:
                   System.out.println("Enter book id:");
                  try{ int id= Integer.parseInt(scanner.nextLine());

                     System.out.print("Enter book title: ");
                     String title = scanner.nextLine();
                     System.out.print("Enter book author: ");
                     String author = scanner.nextLine();
                
                     Book bk = new Book(id, title, author);
                     boolean answer = bookdalo.insertbook(bk);
                     if(answer==true){
                
                    System.out.println("Book added successfully.");
                     }
                    }
                   catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid book ID.");
                        continue;
                    }
                     break;

                     //choice 2 to display books 
                case 2:
                    library.displayAllBooks();
                    break;

                    //choice 3 to find book yy id
                case 3:
                    System.out.print("Enter book ID: ");
                    int id1= Integer.parseInt(scanner.nextLine());
                   
                    Library.findBookById(id1);                  
                    break;

                    //choice 4 to borrow book by id
                case 4:
                    System.out.println("Enter book ID to borrow: ");
                    int borrowId  =  Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter name of the borrower: ");
                    String borrower = scanner.nextLine();
                   
                    library.borrowBook(borrowId, borrower);
                    break;

                    //choice 5 to return book by id
                case 5:
                     System.out.println("Enter book id to return");
                     int id2 = Integer.parseInt(scanner.nextLine());
                     
                     library.returnBook(id2);
                     break;
                    
                    //choice 6 to exit the system
                case 6:
                    System.out.println("Exiting the Library Management System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        catch (NumberFormatException e) {  //Exception handling
                       e.printStackTrace();
                        continue;
                    }

        }
    }
}
