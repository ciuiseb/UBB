import socket
import threading
import json

class Network:
    def __init__(self):
        self.client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.host = "127.0.0.1"
        self.port = 5555
        self.addr = (self.host, self.port)
        self.connected = False
        self.messages = []
        self.connect()

    def connect(self):
        try:
            self.client.connect(self.addr)
            self.connected = True
            threading.Thread(target=self.receive, daemon=True).start()
        except:
            pass

    def send(self, data):
        try:
            self.client.send(json.dumps(data).encode('utf-8'))
        except socket.error:
            pass

    def receive(self):
        while self.connected:
            try:
                data = self.client.recv(1024)
                if not data:
                    break
                message = json.loads(data.decode('utf-8'))
                self.messages.append(message)
            except:
                break