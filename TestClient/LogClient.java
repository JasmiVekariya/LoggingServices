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
        if (args.length != 2) 
        {
            System.out.println("java LogClient <server_ip> <server_port>");
            return; 
        }

        String SERVER_HOST = args[0];
        int SERVER_PORT = Integer.parseInt(args[1]);
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); 
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
             Scanner scanner = new Scanner(System.in))
              { 
                System.out.println("Connected to the logging server.");
              
              while (true)
             {
                System.out.println("\nSelect the mode:");
                System.out.println("i.   Manual");
                System.out.println("ii.  Automatic");
                System.out.println("iii. Spam");
                System.out.println("iv.  Exit");
                System.out.print("Enter your choice: ");
                
                int choice;
                try 
                {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } 

                catch (NumberFormatException e) 
                {
                    System.out.println("Invalid input! Please enter a number.");
                    continue; 
                }

                switch (choice)
                 {
                    case i:
                        manualMode(out, in, scanner);
                        break;

                    case ii:
                        automaticMode(out, in); 
                        break;

                    case iii:
                        spamMode(out, in); 
                        break;

                    case iv:
                        System.out.println("Exiting...");
                        return;
                        
                    default:
                        System.out.println("Invalid choice! Please enter valid choice.");
                }
            }
        } 
        
        catch (IOException e) 
        {
            e.printStackTrace(); 
        }
 
    }

     private static void manualMode(PrintWriter out, BufferedReader in, Scanner scanner) throws IOException 
    {
        System.out.print("Enter the message: ");
        String message = scanner.nextLine(); 
        out.println(message); 
        System.out.println("Server's response: " + in.readLine());
    }

    private static void automaticMode(PrintWriter out, BufferedReader in) throws IOException
     {
        String autoMessage = "This is an automatic message."; 
        System.out.println("Automatic mode: Sending messages to the server....");

        for (int i = 1; i <= 15; i++) 
        {  
            out.println(autoMessage + " [" + i + "]");  
            System.out.println("Server's response: " + in.readLine()); 

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

 private static void spamMode(PrintWriter out, BufferedReader in) throws IOException 
 {
    String spamMessage = "Spam message from client"; 
    Random random = new Random();
    int spamCount = random.nextInt(51) + 50; 

    System.out.println("Spam mode: Sending " + spamCount + " messages to the server....");

    for (int i = 0; i < spamCount; i++) 
    {
        String fullMessage = spamMessage + " [" + (i + 1) + "]"; 
        out.println(fullMessage);  

        System.out.println("Server response: " + in.readLine()); 

        try 
        {
            Thread.sleep(50); 
        } 
        
        catch (InterruptedException e) 
        {
            System.out.println("Sleep interrupted: " + e.getMessage());
        }
    }

    System.out.println("Spam mode complete. Server processed all of the messages.");
    }
}
     


