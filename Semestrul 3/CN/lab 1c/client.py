import socket
import struct
import sys


def choose_exercise_client(client_socket, server_address):
    option = int(input("What exercise do you want to solve? "))
    client_socket.sendto(struct.pack('!H', option), server_address)
    return option


def solve_client_1(client_socket, server_address):
    size = int(input("size = "))
    if size == 0 or size > 256:
        print("Invalid length. Must be between 1 and 256.")
        return

    numbers = []
    for i in range(size):
        num = int(input(f"numbers[{i}] = "))
        numbers.append(num)

    message = struct.pack('!H', size)
    for num in numbers:
        message += struct.pack('!H', num)
    client_socket.sendto(message, server_address)

    data, _ = client_socket.recvfrom(2)
    suma = struct.unpack('!H', data)[0]
    print(f"Suma este {suma}")


def solve_client_2(client_socket, server_address):
    buffer = input("Sir de caractere: ")
    buffer = buffer.ljust(256, '\0')
    client_socket.sendto(buffer.encode(), server_address)

    data, _ = client_socket.recvfrom(2)
    number_of_spaces = struct.unpack('!H', data)[0]
    print(f"Sirul avea {number_of_spaces} spatii")


def solve_client_3(client_socket, server_address):
    buffer = input("Sir de caractere: ")
    buffer = buffer.ljust(256, '\0')
    client_socket.sendto(buffer.encode(), server_address)

    data, _ = client_socket.recvfrom(256)
    result = data.decode().rstrip('\0')
    print(f"Sirul onglidit este {result}")


def solve_client(option, client_socket, server_address):
    solvers = {
        1: solve_client_1,
        2: solve_client_2,
        3: solve_client_3
    }

    if option in solvers:
        solvers[option](client_socket, server_address)


def main():
    if len(sys.argv) != 3:
        print(f"Usage: python {sys.argv[0]} <ip_address> <port>")
        sys.exit(1)

    server_ip = sys.argv[1]
    port = int(sys.argv[2])
    server_address = (server_ip, port)

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    try:
        option = choose_exercise_client(client_socket, server_address)
        solve_client(option, client_socket, server_address)
    except socket.timeout:
        print("Error: Server did not respond in time")
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client_socket.close()


if __name__ == "__main__":
    main()