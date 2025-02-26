/*
FILE          : LogClient.java
PROJECT       : Assignment - 03
PROGRAMMER    : Jasmi Vekariya
                Prachi Patel
FIRST VERSION : February 22, 2025
DESCRIPTION   : The following program contains the code for the client which is used to test the service. 
*/

import java.io.*;               
import java.net.*;              
import java.util.Random;        
import java.util.Scanner;       

public class LogClient
 {
    public static void main(String[] args) 
    {
        // Ensure that exactly two command-line arguments are provided
        if (args.length != 2) 
        {
            System.out.println("java LogClient <server_ip> <server_port>");
            return; 
        }

        // Retrieve the server host and port from command-line arguments
        String SERVER_HOST = args[0];
        int SERVER_PORT = Integer.parseInt(args[1]);
        
        // Making  connection and initialize I/O streams using try-with-resources
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); 
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
             Scanner scanner = new Scanner(System.in))
              { 
                System.out.println("Connected to the logging server.");
              
              // Start an infinite loop to display the mode selection menu continuously
              while (true)
             {
                System.out.println("\nSelect the mode:");        
                System.out.println("1.   Manual");                
                System.out.println("2.  Automatic");             
                System.out.println("3. Spam");                   
                System.out.println("4.  Exit");                   
                System.out.print("Enter your choice: ");
                
                int choice;
                try 
                {
                    // Read user input and convert it to an integer choice
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } 
                catch (NumberFormatException e) 
                {
                    // Handle invalid input that is not a number
                    System.out.println("Invalid input! Please enter a number.");
                    continue; 
                }

                // Choose the mode based on the user's input
                switch (choice)
                 {
                    case 1:                       // Case for manual mode
                        manualMode(out, in, scanner);
                        break;

                    case 2:                      // Case for automatic mode
                        automaticMode(out, in); 
                        break;

                    case 3:                     // Case for spam mode
                        spamMode(out, in); 
                        break;

                    case 4:                      // Case for exit
                        System.out.println("Exiting...");
                        return;
                        
                    default:
                        // Handle any invalid choices not matching the cases
                        System.out.println("Invalid choice! Please enter valid choice.");
                }
            }
        } 
        catch (IOException e) 
        {
            // Print the stack trace if an I/O error occurs
            e.printStackTrace(); 
        }
 
    }

    // Method to handle manual mode where the user inputs a single message
     private static void manualMode(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException 
    {
        System.out.print("Enter the message: ");     
        String message = scanner.nextLine();            
        out.println(message);                          
        System.out.println("Server's response: " + in.readLine());  
    }

    // Method to handle automatic mode which sends a predefined message multiple times
    private static void automaticMode(PrintWriter out, BufferedReader in) throws IOException
     {
        String autoMessage = "This is an automatic message.";  
        System.out.println("Automatic mode: Sending messages to the server....");

        // Loop to send the automatic message 15 times with a counter
        for (int i = 1; i <= 15; i++) 
        {  
            out.println(autoMessage + " [" + i + "]");   // Send message
            System.out.println("Server's response: " + in.readLine());  // Print server's response

            try 
            {
                Thread.sleep(500);      
            } 
            catch (InterruptedException e)
            {
                
                System.out.println("Sleep interrupted: " + e.getMessage());
            }
        }
     }

    // Method to handle spam mode which sends a large number of messages in quick succession
 private static void spamMode(PrintWriter out, BufferedReader in) throws IOException 
 {
    String spamMessage = "Spam message from client";  
    Random random = new Random();                    
    int spamCount = random.nextInt(51) + 50;           

    System.out.println("Spam mode: Sending " + spamCount + " messages to the server....");

    // Loop to send the spam message spamCount times
    for (int i = 0; i < spamCount; i++) 
    {
        String fullMessage = spamMessage + " [" + (i + 1) + "]";  // Append message count to the spam message
        out.println(fullMessage);               // Send the spam message to the server

        System.out.println("Server response: " + in.readLine());  // Print the server's response

        try 
        {
            Thread.sleep(50);       
        } 
        catch (InterruptedException e) 
        {
            // Handle interruption during sleep
            System.out.println("Sleep interrupted: " + e.getMessage());
        }
    }

    System.out.println("Spam mode complete. Server processed all of the messages.");  // Completion of spam mode
    }
}
