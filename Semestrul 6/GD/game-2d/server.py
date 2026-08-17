import socket
import threading
import json
import random
import string
import logging

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

HOST = '127.0.0.1'
PORT = 5555

rooms = {}

def generate_room_code():
    return ''.join(random.choices(string.ascii_uppercase, k=4))

def handle_client(conn, addr):
    current_room = None

    while True:
        try:
            data = conn.recv(1024)
            if not data:
                break

            message = json.loads(data.decode('utf-8'))
            action = message.get("action")

            if action == "CREATE":
                room_code = generate_room_code()
                rooms[room_code] = [conn]
                current_room = room_code
                conn.sendall(json.dumps({"action": "ROOM_CREATED", "code": room_code}).encode('utf-8'))
                logger.info(f"Room {room_code} created by {addr}")

            elif action == "JOIN":
                room_code = message.get("code")
                if room_code in rooms and len(rooms[room_code]) < 2:
                    rooms[room_code].append(conn)
                    current_room = room_code
                    conn.sendall(json.dumps({"action": "JOINED", "code": room_code}).encode('utf-8'))
                    for client in rooms[room_code]:
                        client.sendall(json.dumps({"action": "START_GAME"}).encode('utf-8'))
                    logger.info(f"{addr} joined room {room_code}")
                else:
                    conn.sendall(json.dumps({"action": "ERROR", "msg": "Room full or not found"}).encode('utf-8'))

            # THIS IS THE FIXED LINE:
            elif action not in ["CREATE", "JOIN"]:
                if current_room and current_room in rooms:
                    for client in rooms[current_room]:
                        if client != conn:
                            try:
                                client.sendall(data)
                            except:
                                pass

        except (ConnectionResetError, json.JSONDecodeError):
            break

    if current_room and current_room in rooms:
        if conn in rooms[current_room]:
            rooms[current_room].remove(conn)
        if not rooms[current_room]:
            del rooms[current_room]

    conn.close()

def start_server():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind((HOST, PORT))
    server.listen()

    logger.info(f"Server started...")
    while True:
        conn, addr = server.accept()
        thread = threading.Thread(target=handle_client, args=(conn, addr))
        thread.start()

if __name__ == "__main__":
    start_server()