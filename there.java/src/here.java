import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.Class.*;

class createdv
{
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
            throw new RuntimeException(e);
        }
        return con;
    }
}
class bookdalo{
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
    private ArrayList<Book> books = new ArrayList<>();
    private int nextId = 1;

    public void addBook(String title, String author) {
        Book book = new Book(nextId, title, author);
        books.add(book);
        nextId++;
    }

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

    public void borrowBook(int id) {
        try{
            
        Connection con = createdv.create();
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

    public void returnBook(int id) {
        Book book = findBookById(id);
        if (book != null) {
            if (!book.isAvailable()) {
                book.returnBook();
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Book is already available.");
            }
        } else {
            System.out.println("Book not found.");
        }
    }
}

public class here {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Management System Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Display All Books");
            System.out.println("3. Find Book by ID");
            System.out.println("4. Borrow Book");
          
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
 
         try{   
            int choice= Integer.parseInt(scanner.nextLine());
         
            

            switch (choice) {
                case 1:
                   System.out.println("Enter book id:");
                  try{ int id=  Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                

                    library.addBook(title, author);
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
                case 2:
                    library.displayAllBooks();
                    break;
                case 3:
                    System.out.print("Enter book ID: ");
                    int id1=scanner.nextInt();
                    Library.findBookById(id1);
                    
                    break;
                case 4:
                    System.out.print("Enter book ID to borrow: ");
                    int borrowId  = scanner.nextInt();
                    
                    library.borrowBook(borrowId);
                    break;
                
                case 5:
                    System.out.println("Exiting the Library Management System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid book ID.");
                        continue;
                    }

        }
    }
}
