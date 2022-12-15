package com.application.library.connector;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.application.library.Check;


public class Sql 
{
	static PreparedStatement preparedStatement ;
	public static Connection con;
	static
	{
		try 
		{
			con = getConnection();
		} 
		catch (Exception e) 
		{
			throw new RuntimeException();
		}
	}
	
	public static Connection getConnection() throws Exception
	{
		String url = "jdbc:postgresql://localhost:5432/Library";
		String uName = "postgres";
		String password = "1234";
		
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(url, uName, password);
		
		return con;
	}

	//find user
	static final String getUser = "SELECT user_name FROM users WHERE user_name = ?";
	public static boolean getUserName(String userName) throws SQLException 
	{
		preparedStatement = con.prepareStatement(getUser);
		preparedStatement.setString(1, userName);
		ResultSet rs = preparedStatement.executeQuery();
		
		boolean res = false;
		if(rs.next())
		{
			res = true;
		}
		return res;
	}
	
	//user login
	static final String login = "SELECT * FROM users WHERE user_name = ? AND password = ?";
	public static boolean login(String userName, String password) throws SQLException 
	{
		preparedStatement = con.prepareStatement(login);
		preparedStatement.setString(1, userName);
		preparedStatement.setString(2, password);
		ResultSet rs = preparedStatement.executeQuery();
		
		boolean res = false;
		if(rs.next())
		{
			res = true;
		}
		return res;
		
	}
	
	public static boolean isAdmin(String userName, String password) throws SQLException 
	{
		preparedStatement = con.prepareStatement(login);
		preparedStatement.setString(1, userName);
		preparedStatement.setString(2, password);
		ResultSet rs = preparedStatement.executeQuery();
		
		boolean res = false;
		if(rs.next())
		{
			String admin = rs.getString("admin");
			
			if(admin.equals("t"))
			{
				res = true;
			}
			
		}
		return res;
	}

	public static String getUserid(String userName, String password) throws SQLException 
	{
		preparedStatement = con.prepareStatement(login);
		preparedStatement.setString(1, userName);
		preparedStatement.setString(2, password);
		ResultSet rs = preparedStatement.executeQuery();
		
		if(rs.next())
		{
			return rs.getString("uid");
			
		}
		return null;
	}

	static final String availableBooks = "SELECT * FROM book where no_of_available > 0";
	public static void getAvaliableBooks() throws SQLException 
	{
		preparedStatement = con.prepareStatement(availableBooks);
		ResultSet rs = preparedStatement.executeQuery();
		
		System.out.printf("%-10s|%-30s|%-20s|%-10s","Book ID" ,"Book Name" , "Genre", "Price");
		System.out.println();
		System.out.println("---------------------------------------------------------------------");
		
		while(rs.next())
		{
			System.out.printf("%-10s|%-30s|%-20s|%-10s",rs.getString("bid"), rs.getString("book_name"), rs.getString("genre"), rs.getString("price"));
			System.out.println();
		}
		System.out.println("======================================================================");
		
	}

	static final String getMyBooks = "select * from issue join book on issue.bid=book.bid where issue.uid = ?";
	public static void getMyBooks(String UID) throws SQLException 
	{
		int uid = Integer.parseInt(UID);
		preparedStatement = con.prepareStatement(getMyBooks);
		preparedStatement.setInt(1, uid);
		ResultSet rs = preparedStatement.executeQuery();
		
		System.out.printf("%-10s|%-10s|%-10s|%-11s|%-11s|%-10s|%-10s|%-30s|%-20s|%-10s","Issue Id","User ID","Book ID", "Issue date", "Return Date","Period","Fine","Book Name","Genre","Price");
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
		while(rs.next())
		{
			System.out.printf("%-10s|%-10s|%-10s|%-11s|%-11s|%-10s|%-10s|%-30s|%-20s|%-10s",rs.getString("iid"),rs.getString("uid"),rs.getString("bid"),rs.getString("issue_date"),rs.getString("return_date"),rs.getString("period"),rs.getString("fine"),rs.getString("book_name"),rs.getString("genre"),rs.getString("price"));
			System.out.println();
		}
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println();
		
		
	}

	static final String getAllBooks = "SELECT * from book";
	public static void getAllBooks() throws SQLException 
	{
		preparedStatement = con.prepareStatement(getAllBooks);
		ResultSet rs = preparedStatement.executeQuery();
		
		System.out.printf("%-10s%-30s%-31s%-22s%-30s","Book Id", "|Book Name", " |Price", " |Genre", "|No of avaliable books");
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------------------------------------------");
		//System.out.println("BookID" + "\t|" + "Book Name" + "\t\t\t\t|" + "Price" + "\t\t\t\t|"  + "Genre" + "\t\t\t\t|" +  "No of avaliable books");
		while(rs.next())
		{
			System.out.printf("%-10s|%-30s|%-30s|%-20s|%-10s",rs.getString("bid"),rs.getString("book_name"),rs.getString("price"),rs.getString("genre"),rs.getString("no_of_available"));
			//System.out.println(rs.getString("bid") + "\t|" + rs.getString("book_name") + "\t\t\t\t|"  + rs.getString("price") + "\t\t\t\t|"  + rs.getString("genre") + "\t\t\t\t|" +  rs.getString("no_of_available"));
			System.out.println();
		}
		System.out.println("-------------------------------------------------------------------------------------------------------------------");
	}

	
	static final String addNewBook = "INSERT INTO book (book_name,price,genre,no_of_available) values (?,?,?,?)";
	public static void addNewBook(String bookName, int price, String genre, int count) throws SQLException 
	{
		preparedStatement = con.prepareStatement(addNewBook);
		preparedStatement.setString(1, bookName);
		preparedStatement.setInt(2, price);
		preparedStatement.setString(3, genre);
		preparedStatement.setInt(4, count);
		if(preparedStatement.executeUpdate() > 0)
			System.out.println("Book Added");
		else
			System.out.println("Error!!! No book Added");
		
	}

	static final String hasBookId = "SELECT bid FROM book WHERE bid = ?";
	public static boolean hasBookId(int bookId) throws SQLException 
	{
		preparedStatement = con.prepareStatement(hasBookId);
		preparedStatement.setInt(1, bookId);
		ResultSet rs = preparedStatement.executeQuery();
		
		if(rs.next())
			return true;
	
		return false;
	}

	static final String addOldBook = "UPDATE book SET no_of_available = no_of_available + ? where bid = ?";
	public static void addOldBook(int bookId, int count) throws SQLException 
	{
		preparedStatement = con.prepareStatement(addOldBook);
		preparedStatement.setInt(1, count);
		preparedStatement.setInt(2, bookId);
		
		if(preparedStatement.executeUpdate() > 0)
		{
			System.out.println("Book count Updated");
		}
		else
		{
			System.out.println("Update failure");
		}
		
	}

	static final String getAllUsers = "SELECT * FROM users";
	public static void getAllUsers() throws SQLException 
	{
		preparedStatement = con.prepareStatement(getAllUsers);
		ResultSet rs =preparedStatement.executeQuery();
		
		System.out.printf("%-10s|%-20s|%-10s%n", "User Id","Username","Admin");
		System.out.println("-------------------------------------------------------------------------------------------------------------------");
		while(rs.next())
		{
			System.out.printf("%-10s|%-20s|%-10B",rs.getString("uid"),rs.getString("user_name"),rs.getBoolean("admin"));
			System.out.println();
		}
		System.out.println("-------------------------------------------------------------------------------------------------------------------");

		
	}

	static final String addNewUser = "INSERT INTO users (user_name,password,admin) VALUES (?,?,?)";
	public static void addNewUser(String username, String password, boolean admin) throws SQLException 
	{
		preparedStatement = con.prepareStatement(addNewUser);
		preparedStatement.setString(1, username);
		preparedStatement.setString(2, password);
		preparedStatement.setBoolean(3, admin);
		if(preparedStatement.executeUpdate() > 0)
			System.out.println("User Added");
		else
			System.out.println("Error!!! Failed to add user.");
		
	}

	static final String hasUserId = "SELECT * FROM users where uid = ?";
	public static boolean hasUserId(int userId) throws SQLException 
	{
		preparedStatement = con.prepareStatement(hasUserId);
		preparedStatement.setInt(1, userId);
		ResultSet rs = preparedStatement.executeQuery();
		if(rs.next())
			return true;
		return false;
	}

	static final String issueBook = "INSERT INTO issue (uid,bid,issue_date,period) VALUES (?,?,?,?)";
	public static void issueBook(int userId, int bookId, int period, String date) throws SQLException 
	{
		preparedStatement = con.prepareStatement(issueBook);
		preparedStatement.setInt(1, userId);
		preparedStatement.setInt(2, bookId);
		preparedStatement.setString(3, date);
		preparedStatement.setInt(4, period);
		
		if(preparedStatement.executeUpdate() > 0)
		{
			getIssueId(userId,bookId);
			System.out.println("Book issued");
			
		}
		else
		{
			System.out.println("Error");
		}
		
	}

	static final String issueId = "SELECT iid FROM issue WHERE uid = ? AND bid = ?";
	
	private static void getIssueId(int userId, int bookId) throws SQLException 
	{
		preparedStatement = con.prepareStatement(issueId);
		preparedStatement.setInt(1, userId);
		preparedStatement.setInt(2, bookId);
		ResultSet rs = preparedStatement.executeQuery();
		if(rs.next())
		{
			reduceBookCount(bookId);
			System.out.println("Book issued with Issue ID : " + rs.getString("iid"));
			
		}
		else
		{
			System.out.println("Error!!!");
		}
		
	}

	static final String reduceBookCount = "UPDATE book SET no_of_available = no_of_available - 1 WHERE bid = ?";
	private static void reduceBookCount(int bookId) throws SQLException 
	{
		preparedStatement = con.prepareStatement(reduceBookCount);
		preparedStatement.setInt(1, bookId);
		preparedStatement.executeUpdate();
		
		
		
	}

	static final String getIssuedBook = "SELECT * FROM issue"; 
	public static void getIssuedBook() throws SQLException 
	{
		preparedStatement = con.prepareStatement(getIssuedBook);
		ResultSet rs = preparedStatement.executeQuery();
		
		System.out.printf("%-10s|%-10s|%-10s|%-11s|%-10s|%-11s|%-10s", "ISSUE ID", "USER ID", "BOOK ID", "ISSUE DATE", "PERIOD", "RETURN DATE", "FINE");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
		while(rs.next())
		{
			System.out.printf("%-10s|%-10s|%-10s|%-11s|%-10s|%-11s|%-10s", rs.getString("iid"),rs.getString("uid"),rs.getString("bid"),rs.getString("issue_date"),rs.getString("period"),rs.getString("return_date"),rs.getString("fine"));
			System.out.println();
		}
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

		
	}

	static final String isAvaliable = "SELECT no_of_available FROM book WHERE bid =?";
	public static boolean isAvailable(int bookId) throws SQLException 
	{
		preparedStatement = con.prepareStatement(isAvaliable);
		preparedStatement.setInt(1, bookId);
		ResultSet rs = preparedStatement.executeQuery();
		if(rs.next())
			return true;
		return false;
		
		
	}

	static final String hasIssueId = "SELECT * FROM issue WHERE iid = ?";
	public static boolean hasIssueId(int iid) throws SQLException 
	{
		preparedStatement = con.prepareStatement(hasIssueId);
		preparedStatement.setInt(1, iid);
		ResultSet rs =preparedStatement.executeQuery();
		
		if(rs.next())
		{
			if(rs.getString("return_date") == null)
				return true;
			else
			{
				System.out.println("Book already returned");
				return false;
			}
			
		}
		return false;
	}

	static final String getIssueDate = "SELECT * FROM issue WHERE iid = ?";
	public static String getIssueDate(int issueId2) throws SQLException
	{
		preparedStatement = con.prepareStatement(getIssueDate);
		preparedStatement.setInt(1, issueId2);
		ResultSet rs = preparedStatement.executeQuery();
		if(rs.next())
		{
			return rs.getString("issue_date");
		}
		return null;
	}

	static final String getIssuedBookDetail = "SELECT * FROM issue WHERE iid = ?";
	public static void returnBook(int issueId2, String returnDate, Scanner scan) throws SQLException, ParseException 
	{
		preparedStatement = con.prepareStatement(getIssuedBookDetail);
		preparedStatement.setInt(1, issueId2);
		ResultSet rs = preparedStatement.executeQuery();
		
		if(rs.next())
		{
			String iDate = rs.getString("issue_date");
			Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(iDate);
            Date date2=new SimpleDateFormat("dd-MM-yyyy").parse(returnDate);
            long date1InMs = date1.getTime();
            long date2InMs = date2.getTime();
            
            long timeDiff = Math.abs(date1InMs-date2InMs);
            int days = (int)TimeUnit.DAYS.convert(timeDiff,TimeUnit.MILLISECONDS);
            int period = rs.getInt("period");
            int bookId = rs.getInt("bid");
            
            int fine =0;
            if(period < days)
            {
            	fine = (days - period)*10;
            	System.out.println("Fine to be Paid: " + fine);
            	System.out.println("Do you want to pay!!!");
            	System.out.println("1.Yes");
            	System.out.println("2.No");
            	int ch = Check.getChoice(scan, "[1-2]{1}");
            	if(ch == 1)
            	{
            		updateReturnedBook(bookId);
            		updateReturn(issueId2,returnDate,fine);
            		System.out.println("Book Returned.");
            	}
            	else
            	{
            		System.out.println("Book Not returned!!!");
            	}
            }
            else
            {
            	updateReturn(issueId2, returnDate, 0);
            	updateReturnedBook(bookId);
            	System.out.println("Book returned.");
            }
		}
		
	}

	static final String updateReturned = "UPDATE issue set return_date = ?, fine = ? where iid = ?"; 
	private static void updateReturn(int issueId2, String returnDate, int fine) throws SQLException 
	{
		preparedStatement = con.prepareStatement(updateReturned);
		preparedStatement.setInt(3, issueId2);
		preparedStatement.setString(1, returnDate);
		preparedStatement.setInt(2, fine);
		preparedStatement.executeUpdate();
		
	}

	static final String updateBookCount = "UPDATE book set no_of_available = no_of_available + 1 where bid = ?";  
	private static void updateReturnedBook(int bookId) throws SQLException
	{
		preparedStatement = con.prepareStatement(updateBookCount);
		preparedStatement.setInt(1, bookId);
		preparedStatement.executeUpdate();
		
	}

	
	
	
}
