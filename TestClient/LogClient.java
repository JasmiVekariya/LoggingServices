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
              }
    }
 }