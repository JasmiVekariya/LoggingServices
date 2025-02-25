
'''
FILE          : LogServer.py
PROJECT       : Assignment - 03
PROGRAMMER    : Jasmi Vekariya
                Prachi Patel
FIRST VERSION : February 22, 2025
DESCRIPTION   : The following program contains the code for the service which listen for logging messages.
'''

import socket  
import threading  
import json 
import pytz  
import time
from datetime import datetime, timezone as dt_timezone 

CONFIG_FILE = "config.json" 
"""path to the configuration file. """

def load_config():
    """Load server configuration from a JSON file."""
    with open(CONFIG_FILE, "r") as file:
        return json.load(file)

config = load_config()
HOST = config["host"]  
PORT = config["port"]  
LOG_FILE = f"{config['log_file']}.log"  """Log file name with .log extension. """

def write_log(message, addr, level="INFO"):
    """Writing  a log message to the log file."""

    valid_levels = [
        "Emergency", "Alert", "Critical", "Error", "Warning", 
        "Notification", "Informational", "Debugging", "INFO"
    ]

    if level not in valid_levels:
        level = "INFO"

    utc_now = datetime.now(dt_timezone.utc)

    canada_timezone = pytz.timezone("America/Toronto") 

    local_time = utc_now.astimezone(canada_timezone)  

    timestamp = local_time.strftime("%b %d %Y %H:%M:%S")  

    """Building  log message string including client IP, log level, and message """
    log_message = f"{timestamp} {addr[0]} {level} {message}\n"



    with open(LOG_FILE, "a") as log:
        log.write(log_message)

def handle_client(conn, addr):
    """Handles the communication with a client."""
    print(f"Connection from {addr[0]} established.")  

    try:
        while True:
           
            """Receive data from the client and decode it """
            data = conn.recv(1024).decode().strip()
            
            if not data:
                break

            log_entry = f"{data}"  
            
            print(f"{addr[0]} {log_entry}") 
            write_log(log_entry, addr)  

            conn.send("Log entry received.\n".encode())

    except ConnectionResetError:
        
        print(f"Connection with {addr[0]} lost.")

    finally:
        
        conn.close() 

def start_server():
    """Starting the server connection."""
    
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    server.bind((HOST, PORT))
    
    server.listen(5)
    print(f"Logging server running on {HOST}:{PORT}")

    while True:
        
        conn, addr = server.accept()
      
        threading.Thread(target=handle_client, args=(conn, addr), daemon=True).start()

if __name__ == "__main__":
    start_server() 
