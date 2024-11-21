import threading
import socket

global_minimum = 999999
minimum_lock = threading.Lock()


def handle_client_type_tcp(client_socket, addr):
    global global_minimum
    try:
        length = int(client_socket.recv(1024).decode())
        client_socket.send("OK".encode())

        numbers = []
        for _ in range(length):
            num = int(client_socket.recv(1024).decode())
            numbers.append(num)
            client_socket.send("OK".encode())

        if numbers:
            minim = min(numbers)
            if minim < global_minimum:
                global_minimum = minim
            print(f"Sending result to client with address {addr}")
            client_socket.send(str(minim).encode())
            with minimum_lock:
                global_minimum = min(global_minimum, minim)

    except Exception as e:
        print(f"Error serving TCP client with address {addr}: {e}")
    finally:
        client_socket.close()


def handle_client_type_udp(udp_socket):
    data, addr = udp_socket.recvfrom(1024)
    with minimum_lock:
        udp_socket.sendto(str(global_minimum).encode(), addr)


def start_tcp_server(port):
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind(('192.168.40.147', port))
    server.listen(5)
    print(f"TCP Server listening on port {port}")

    while True:
        client_socket, addr = server.accept()
        print(f"TCP Client connected from address {addr} on port {port}")
        client_thread = threading.Thread(target=handle_client_type_tcp, args=(client_socket, addr))
        client_thread.start()

def start_udp_server(port):
    udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    udp_socket.bind(('192.168.40.147', port))
    print(f"UDP Server started on port {port}")

    while True:
        client_thread = threading.Thread(target=handle_client_type_udp, args=(udp_socket,))
        client_thread.start()

def main():
    try:
        tcp_server1 = threading.Thread(target=start_tcp_server, args=(1551,))
        tcp_server2 = threading.Thread(target=start_tcp_server, args=(1771,))

        udp_server1 = threading.Thread(target=start_udp_server, args=(1661,))

        tcp_server1.start()
        tcp_server2.start()
        udp_server1.start()

        tcp_server1.join()
        tcp_server2.join()
        udp_server1.join()

    except Exception as e:
        print(f"Error starting server: {e}")


if __name__ == "__main__":
    main()