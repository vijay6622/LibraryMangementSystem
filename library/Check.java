package com.application.library;

import java.util.Scanner;

public class Check
{
	public static boolean isValid(String input,String pattern)
	{
		
		boolean result = input.matches(pattern);
			
		return result;
	}

	public static String getUserName(Scanner scan) 
	{
		try
		{
			String uName = scan.next();
			if(isValid(uName,"[a-zA-Z0-9]*"))
			{
				return uName;
			}
			else
			{
				System.out.println("User name should contains only a-z,A-Z or 0-9");
				System.out.print("Enter user name:");
				return getUserName(scan);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Invalid user name!!!");
			System.out.println(e.getMessage());
			System.out.print("Enter user name:");
			return getUserName(scan);
		}
	}

	public static String getPassword(Scanner scan) 
	{
		try
		{
			//[A-Za-z\\d@$!%*?&]
			String password  = scan.next();
			if(isValid(password, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])(?=\\S+$).{8,}$"))
			{
				return password;
			}
			else
			{
				System.out.println("Password should contain atleast one lowercase, uppercase, number and a special character and more than 8 character.");
				System.out.print("Enter password:");
				return getPassword(scan);
			}
		}
		catch(Exception e)
		{
			System.out.println("Invalid password!!!");
			System.out.println(e.getMessage());
			System.out.print("Enter password:");
			return getPassword(scan);
		}
	}

	public static int getChoice(Scanner scan, String pattern) 
	{
		try
		{
			System.out.print("Enter choice:");
			int choice = scan.nextInt();
			if(isValid(Integer.toString(choice), pattern))
			{
				return choice;
			}
			else
			{
				System.out.println("Choice should be in " + pattern);
				return getChoice(scan, pattern);
			}
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			return getChoice(scan,pattern);
		}
	}
}
