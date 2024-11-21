import socket
import struct
import threading
from typing import Tuple


def solve_server_1(data: bytes, client_address: Tuple[str, int], server_socket: socket.socket):
    size = struct.unpack('!H', data[:2])[0]
    total = 0
    offset = 2
    for _ in range(size):
        num = struct.unpack('!H', data[offset:offset + 2])[0]
        total += num
        offset += 2
    server_socket.sendto(struct.pack('!H', total), client_address)


def solve_server_2(data: bytes, client_address: Tuple[str, int], server_socket: socket.socket):
    space_count = data.count(b' ')
    server_socket.sendto(struct.pack('!H', space_count), client_address)


def solve_server_3(data: bytes, client_address: Tuple[str, int], server_socket: socket.socket):
    length = 0
    while length < len(data) and data[length] != 0:
        length += 1
    reversed_bytes = data[:length][::-1]
    server_socket.sendto(reversed_bytes, client_address)


def solve_server(option: int, data: bytes, client_address: Tuple[str, int], server_socket: socket.socket):
    if option < 1 or option > 10:
        return

    print(f"Solving problem number {option}")

    solvers = {
        1: solve_server_1,
        2: solve_server_2,
        3: solve_server_3
    }

    if option in solvers:
        solvers[option](data, client_address, server_socket)


def main():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_socket.bind(('0.0.0.0', 1234))
    print("Server is listening on port 1234...")

    while True:
        try:
            # Get option
            option_data, client_address = server_socket.recvfrom(2)
            print(f"Client connected from {client_address[0]}:{client_address[1]}")
            option = struct.unpack('!H', option_data)[0]

            # Get data
            data, _ = server_socket.recvfrom(1024)

            # Process request
            solve_server(option, data, client_address, server_socket)

        except Exception as e:
            print(f"Error handling client request: {e}")


if __name__ == "__main__":
    main()