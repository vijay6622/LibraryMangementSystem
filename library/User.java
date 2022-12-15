package com.application.library;


import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.application.library.connector.Sql;


public class User 
{
	Scanner scan = new Scanner(System.in);
	public void login() throws SQLException, ParseException 
	{
		
		System.out.print("Enter user name:");
		String userName = Check.getUserName(scan);
		
		System.out.print("Enter password :");
		String password = Check.getPassword(scan);
		if(Sql.getUserName(userName))
		{
			if(Sql.login(userName, password))
			{
				System.out.println("logged in...");
				if(Sql.isAdmin(userName,password))
				{
					System.out.println("Admin");
					adminMenu();
				}
				else
				{
					final String UID = Sql.getUserid(userName,password);
					userMenu(UID);
				}
				
				
			}
			else
			{
				System.out.println("Incorrect password!!!");
				System.out.println();
				login();
			}
		}
		else
		{
			System.out.println("User Not Found!!!");
			System.out.println();
			login();
		}
		
		
	}

	private void adminMenu() throws SQLException, ParseException 
	{
		int choice = -1;
		while(choice != 0)
		{
			System.out.println("---------Admin menu---------");
			System.out.println("1.View Books");
			System.out.println("2.Add book");
			System.out.println("3.View Users");
			System.out.println("4.Add user");
			System.out.println("5.Issue book");
			System.out.println("6.View issued book");
			System.out.println("7.Return book");
			System.out.println("0.Exit");
			choice = Check.getChoice(scan,"[0-7]");
			System.out.println();
			
			switch (choice) 
			{
			case 1:
				Sql.getAllBooks();
				break;
			case 2:
				addBook();
				break;
			case 3:
				Sql.getAllUsers();
				break;
			case 4:
				addUser();
				break;
			case 5:
				issueBook();
				break;
			case 6:
				Sql.getIssuedBook();
				break;
			case 7:
				returnBook();
				break;
			case 0:
				login();
				break;
			
			}
			
		}
		
		
	}

	private void returnBook() throws SQLException, ParseException 
	{
		int issueId = getIssueId();
		String returnDate = getReturnDate(issueId);
		Sql.returnBook(issueId,returnDate,scan);
		
		
	}

	private String getReturnDate(int issueId) 
	{
		try
		{
			String rDate = getDate();
			String iDate = Sql.getIssueDate(issueId);
			Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(rDate);
            Date date2=new SimpleDateFormat("dd-MM-yyyy").parse(iDate);
            if(date1.after(date2))
            {
            	return rDate;
            }
            else
            {
            	System.out.println("Return date must be greater than issue date.");
            	return getReturnDate(issueId);
            }
			
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return getReturnDate(issueId);
		}
	}

	private int getIssueId() 
	{
		try
		{
			System.out.print("Enter issue Id:");
			int iid = scan.nextInt();
			if(Check.isValid(Integer.toString(iid), "[0-9]+"))
			{
				if(Sql.hasIssueId(iid))
				{
					return iid;
				}
				else
				{
					System.out.println("Invalid issue id");
					return getIssueId();
				}
			}
			else
			{
				System.out.println("Invalid issue id");
				return getIssueId();
			}
			
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return getIssueId();
		}
		
	}

	private void issueBook() throws SQLException 
	{
		int userId = getUserId();
		int bookId = getAvailableBookId();
		
		int period = getPeriod();
		String date = getDate();
		Sql.issueBook(userId,bookId,period,date);
		
	}

	private int getAvailableBookId() throws SQLException 
	{
		int bid = getBookId();
		if(Sql.isAvailable(bid))
		{
			return bid;
		}
		else
		{
			System.out.println("Book not available");
			return getAvailableBookId();
		}
		
	}

	private String getDate() 
	{
		try 
		{
			System.out.print("Enter date(DD-MM-YYY):");
			String date = scan.next();
			if(Check.isValid(date, "\\d{2}-\\d{2}-\\d{4}"))
			{
				return date;
			}
			else
			{
				System.out.println("Invalid date.");
				return getDate();
			}
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return getDate();
		}
		
	}

	private int getPeriod() 
	{
		
		try 
		{
			System.out.print("Enter Period(days):");
			int period = scan.nextInt();
			if(Check.isValid(Integer.toString(period), "[0-9]+") && period > 0)
			{
				return period;
			}
			else
			{
				System.out.println("Invalid Period!!!");
				return getPeriod();
			}
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return getPeriod();
		}
	}

	private int getUserId() 
	{
		try 
		{
			System.out.print("Enter user Id:");
			int userId = scan.nextInt();
			if(Check.isValid(Integer.toString(userId), "[0-9]+"))
			{
				if(Sql.hasUserId(userId))
				{
					return userId;
				}
				else
				{
					System.out.println("User not found.");
					return getUserId();
				}
			}
			else
			{
				System.out.println("Invalid user Id!!!");
				return getUserId();
			}
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return getUserId();
		}
	}

	private void addUser() throws SQLException 
	{
		System.out.print("Enter username:");
		String username = Check.getUserName(scan);
		if(Sql.getUserName(username))
		{
			System.out.println("User name already exists.");
			addUser();
		}
		System.out.print("Enter password :");
		String password = Check.getPassword(scan);
		System.out.println("1.Admin");
		System.out.println("2.User");
		int choice = Check.getChoice(scan, "[1-2]");
		boolean admin = false;
		switch(choice)
		{
		case 1:
			admin = true;
			break;
		case 2:
			admin = false;
			break;
		}
		Sql.addNewUser(username,password,admin);
	}

	private void addBook() throws SQLException 
	{
		int choice = -1;
		while(choice != 0)
		{
			System.out.println("1.Add New Book");
			System.out.println("2.Add Existing Book");
			System.out.println("0.Back");
			System.out.print("\nEnter your choice:");
			choice = Check.getChoice(scan, "[0-2]");
			switch(choice)
			{
			case 1:
				String bookName = getBookName();
				int price = getPrice();
				String genre = getGenre();
				int count = getCount();
				Sql.addNewBook(bookName,price,genre,count);
				break;
			case 2:
				int bookId = getBookId();
				count = getCount();
				Sql.addOldBook(bookId,count);
				break;
			case 0:
				System.out.println("Going to admin menu");
				return;
			default:
				System.out.println("Inavalid option.");
				break;
			}
		}
		
	}

	private int getBookId() 
	{
		int bookId;
		try
		{
			System.out.print("Enter book Id:");
			bookId = scan.nextInt();
			if(Check.isValid(Integer.toString(bookId),"[0-9]+"))
			{
				if(Sql.hasBookId(bookId))
				{
					return bookId;
				}
				else
				{
					System.out.println("Invalid book id");
					return getBookId();
				}
			}
			else
			{
				System.out.println("Invalid book id");
				return getBookId();
			}
		}
		catch (Exception e) 
		{
			System.out.println("Invalid book id");
			System.out.println(e.getMessage());
			return getBookId();
		}
		
	}

	private int getCount() 
	{
		int count;
		try 
		{
			System.out.print("Enter no of books:");
			count = scan.nextInt();
			if(Check.isValid(Integer.toString(count),"[0-9]+") && count >= 1)
			{
				return count;
			}
			else
			{
				System.out.println("Invalid count");
				return getCount();
			}
		} 
		catch (Exception e)
		{
			System.out.println("Invalid count.");
			System.out.println(e.getMessage());
			return getCount();
		}
	}

	private String getGenre()
	{
		String genre;
		try
		{
			System.out.print("Enter genre:");
			genre = scan.next();
			if(Check.isValid(genre, "[a-zA-Z]+"))
				return genre;
			else
			{
				System.out.println("Invalid genre.");
				return getGenre();
			}
		}
		catch (Exception e) 
		{
			System.out.println("Invalid genre.");
			System.out.println(e.getMessage());
			return getGenre();
		}
	}

	private int getPrice() 
	{
		int price;
		try 
		{
			System.out.print("Enter price:");
			price = scan.nextInt();
			if(Check.isValid(Integer.toString(price),"[0-9]+") && price >= 0)
			{
				return price;
			}
			else
			{
				System.out.println("Invalid price");
				return getPrice();
			}
		} 
		catch (Exception e)
		{
			System.out.println("Invalid price");
			System.out.println(e.getMessage());
			return getPrice();
		}
		
	}

	private String getBookName() 
	{
		String bookName;
		try
		{
			System.out.print("Enter book name:");
			bookName = scan.nextLine();
			if(Check.isValid(bookName, "[a-zA-Z0-9\s]+"))
				return bookName;
			else
			{
				System.out.println("Invalid book name.");
				return getBookName();
			}
		}
		catch (Exception e) 
		{
			System.out.println("Invalid book name.");
			System.out.println(e.getMessage());
			return getBookName();
		}
		
	}

	private void userMenu(String UID) throws SQLException 
	{
		int choice = -1;
		while(choice != 0)
		{
			System.out.println("-----User Menu-----");
			System.out.println("1.Avaliable books");
			System.out.println("2.My Books");
			System.out.println("0.Exit");
			choice = Check.getChoice(scan,"[0-2]+");
			switch(choice)
			{
			case 1:
				System.out.println("===========================Avaliable books===========================");
				Sql.getAvaliableBooks();
				break;
			case 2:
				System.out.println("===============================My books===============================");
				Sql.getMyBooks(UID);
				break;
			case 0:
				System.out.println("Exitting");
				choice = 0;
				break;
			default:
				System.out.println("Invalid option");
				break;	
			}
		}
		
		
		
	}
}
