
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
from datetime import datetime  

CONFIG_FILE = "config.json"

def load_config():
    """Load server configuration from a JSON file."""
    with open(CONFIG_FILE, "r") as file:
        return json.load(file)

config = load_config()
HOST = config["host"]  
PORT = config["port"]  
LOG_FILE = f"{config['log_file']}.log" 

def write_log(message, addr):
    """Write log message to the log file in a specific format."""

    utc_now = datetime.utcnow().replace(tzinfo=pytz.utc)

    timezone = pytz.timezone("UTC")

    local_time = utc_now.astimezone(timezone)

    timestamp = local_time.strftime("%b %d %Y %H:%M:%S")  

    device_id = "pix-f"

    facility_code = "%PIX"

    severity_level = 5  

    message_number = "111008"

    log_message = f"{timestamp} {device_id}: {facility_code}-{severity_level}-{message_number}: {message}\n"

    with open(LOG_FILE, "a") as log:
        log.write(log_message)

def handle_client(conn, addr):
    """Handle communication with a connected client."""
    print(f"Connection from {addr} established.")  

    try:
        while True:
           
            data = conn.recv(1024).decode().strip()
            
            if not data:
                break

            log_entry = f"'{addr}' {data}"
            
            write_log(log_entry, addr)

            conn.send("Log entry received.\n".encode())

    except ConnectionResetError:
        
        print(f"Connection with {addr} lost.")

    finally:
        
        conn.close()

def start_server():
    """Start the logging server to accept client connections."""
    
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    server.bind((HOST, PORT))
    
    server.listen(5)
    print(f"Logging server running on {HOST}:{PORT}")

    while True:
        
        conn, addr = server.accept()
      
        threading.Thread(target=handle_client, args=(conn, addr), daemon=True).start()

if _name_ == "_main_":
    start_server()