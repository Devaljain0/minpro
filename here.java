import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

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
    public static boolean insertbook (book bk){
        boolean f = false;
    try{
        Connection co = createdv.create();
        String q = "insert into book(id, title, author) values(?, ?, ?)";
        PreparedStatementt pstmt =con.prepareStatement(q);
        pstmt.setString(1, bk.getId());
        pstmt.setStringg(2, bk.getName());
        pstmt.setString(3, bk.getAuthor());

        pstmt.executeUpdate();
        f = true;
    }
    catch(Exceptiom e){
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
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public Book findBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public void borrowBook(int id) {
        Book book = findBookById(id);
        if (book != null) {
            if (book.isAvailable()) {
                book.borrow();
                System.out.println("Book borrowed successfully.");
            } else {
                System.out.println("Book is not available for borrowing.");
            }
        } else {
            System.out.println("Book not found.");
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
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                continue;
            }

            switch (choice) {
                case 1:
                   System.out.println("Enter book id:");
                   int id= scanner.nextInt();

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
                     break;
                case 2:
                    library.displayAllBooks();
                    break;
                case 3:
                    System.out.print("Enter book ID: ");
                    int id;
                    try {
                        id = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid book ID.");
                        continue;
                    }
                    Book foundBook = library.findBookById(id);
                    if (foundBook != null) {
                        System.out.println(foundBook);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter book ID to borrow: ");
                    int borrowId;
                    try {
                        borrowId = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid book ID.");
                        continue;
                    }
                    library.borrowBook(borrowId);
                    break;
                case 5:
                    System.out.print("Enter book ID to return: ");
                    int returnId;
                    try {
                        returnId = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid book ID.");
                        continue;
                    }
                    library.returnBook(returnId);
                    break;
                case 6:
                    System.out.println("Exiting the Library Management System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
