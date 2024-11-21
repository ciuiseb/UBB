import socket
import struct
import sys 

def choose_exercise_client(client_socket):
    option = int(input("What exercise do you want to solve? "))
    client_socket.send(struct.pack('!H', option))
    return option

def solve_client_1(client_socket):
    size = int(input("size = "))
    if size == 0 or size > 256:
        print("Invalid length. Must be between 1 and 256.")
        return

    numbers = []
    for i in range(size):
        num = int(input(f"numbers[{i}] = "))
        numbers.append(num)

    client_socket.send(struct.pack('!H', size))
    for num in numbers:
        client_socket.send(struct.pack('!H', num))

    suma = struct.unpack('!H', client_socket.recv(2))[0]
    print(f"Suma este {suma}")

def solve_client_2(client_socket):
    buffer = input("Sir de caractere: ")
    buffer = buffer.ljust(256, '\0')
    client_socket.send(buffer.encode())

    number_of_spaces = struct.unpack('!H', client_socket.recv(2))[0]
    print(f"Sirul avea {number_of_spaces} spatii")

def solve_client_3(client_socket):
    buffer = input("Sir de caractere: ")
    buffer = buffer.ljust(256, '\0')
    client_socket.send(buffer.encode())

    result = client_socket.recv(256).decode().rstrip('\0')
    print(f"Sirul onglidit este {result}")

def solve_client_4(client_socket):
    buffer_1 = input("Primul sir: ")
    buffer_2 = input("Al doilea sir: ")

    client_socket.send(struct.pack('!H', len(buffer_1)))
    client_socket.send(buffer_1.encode())

    client_socket.send(struct.pack('!H', len(buffer_2)))
    client_socket.send(buffer_2.encode())

    result = client_socket.recv(len(buffer_1) + len(buffer_2)).decode()
    print(f"Sirurile interclasate: {result}")

def solve_client_5(client_socket):
    number = int(input("Numarul este: "))
    client_socket.send(struct.pack('!H', number))

    num_divisors = struct.unpack('!H', client_socket.recv(2))[0]

    divisors = []
    for _ in range(num_divisors):
        divisor = struct.unpack('!H', client_socket.recv(2))[0]
        divisors.append(divisor)

    print("Divizorii sunt:", end=" ")
    print(", ".join(map(str, divisors)))

def solve_client_6(client_socket):
    buffer = input("Sirul: ")
    target = input("Caracterul cautat: ")[0]

    client_socket.send(struct.pack('!H', len(buffer)))
    client_socket.send(buffer.encode())

    client_socket.send(target.encode())

    num_appearances = struct.unpack('!H', client_socket.recv(2))[0]

    positions = []
    for _ in range(num_appearances):
        pos = struct.unpack('!H', client_socket.recv(2))[0]
        positions.append(pos)

    print("Pozitiile pe care apare caracterul cautat in sir sunt:", end=" ")
    print(", ".join(map(str, positions)))

def solve_client_7(client_socket):
    buffer = input("Sirul: ")
    starting_position = int(input("Pozitia initiala: "))
    substring_length = int(input("Lungimea dorita: "))

    client_socket.send(struct.pack('!H', len(buffer)))
    client_socket.send(buffer.encode())

    client_socket.send(struct.pack('!H', starting_position))
    client_socket.send(struct.pack('!H', substring_length))

    result = client_socket.recv(substring_length).decode()
    print(f"Sirul cerut este: {result}")

def solve_client_8(client_socket):
    len1 = int(input("Introduceti lungimea primului sir: "))
    print("Introduceti elementele primului sir: ")
    arr1 = [int(input()) for _ in range(len1)]

    len2 = int(input("Introduceti lungimea celui de-al doilea sir: "))
    print("Introduceti elementele celui de-al doilea sir: ")
    arr2 = [int(input()) for _ in range(len2)]

    client_socket.send(struct.pack('!H', len1))
    for num in arr1:
        client_socket.send(struct.pack('!i', num))

    client_socket.send(struct.pack('!H', len2))
    for num in arr2:
        client_socket.send(struct.pack('!i', num))

    common_count = struct.unpack('!H', client_socket.recv(2))[0]

    if common_count > 0:
        common = []
        for _ in range(common_count):
            num = struct.unpack('!i', client_socket.recv(4))[0]
            common.append(num)
        print("Numerele comune sunt:", " ".join(map(str, common)))
    else:
        print("Nu exista numere comune.")

def solve_client_9(client_socket):
    len1 = int(input("Introduceti lungimea primului sir: "))
    print("Introduceti elementele primului sir: ")
    arr1 = [int(input()) for _ in range(len1)]

    len2 = int(input("Introduceti lungimea celui de-al doilea sir: "))
    print("Introduceti elementele celui de-al doilea sir: ")
    arr2 = [int(input()) for _ in range(len2)]

    client_socket.send(struct.pack('!H', len1))
    for num in arr1:
        client_socket.send(struct.pack('!i', num))

    client_socket.send(struct.pack('!H', len2))
    for num in arr2:
        client_socket.send(struct.pack('!i', num))

    result_count = struct.unpack('!H', client_socket.recv(2))[0]

    if result_count > 0:
        result = []
        for _ in range(result_count):
            num = struct.unpack('!i', client_socket.recv(4))[0]
            result.append(num)
        print("Numerele care sunt in primul sir dar nu in al doilea sunt:", " ".join(map(str, result)))
    else:
        print("Nu exista numere unice in primul sir.")

def solve_client_10(client_socket):
    str1 = input("Introduceti primul sir de caractere: ")
    str2 = input("Introduceti al doilea sir de caractere: ")

    client_socket.send(struct.pack('!H', len(str1)))
    client_socket.send(str1.encode())

    client_socket.send(struct.pack('!H', len(str2)))
    client_socket.send(str2.encode())

    max_char = client_socket.recv(1).decode()
    max_count = struct.unpack('!H', client_socket.recv(2))[0]

    print(f"Caracterul cel mai comun pe pozitii identice este '{max_char}' cu {max_count} aparitii.")

def solve_client(option, client_socket):
    solvers = {
        1: solve_client_1,
        2: solve_client_2,
        3: solve_client_3,
        4: solve_client_4,
        5: solve_client_5,
        6: solve_client_6,
        7: solve_client_7,
        8: solve_client_8,
        9: solve_client_9,
        10: solve_client_10
    }

    if option in solvers:
        solvers[option](client_socket)

def main():
    if len(sys.argv) != 3:
        print("{sys.argv[1]}: python client.py <ip_address> <port>")
        sys.exit(1)
    
    server_ip = sys.argv[1]
    
    try:
        octets = server_ip.split('.')
        if len(octets) != 4:
            raise ValueError("Invalid IP address")
        for octet in octets:
            num = int(octet)
            if num < 0 or num > 255:
                raise ValueError("Invalid IP address")
    except ValueError as e:
        print(f"Invalid IP address: {e}")
        sys.exit(1)
    port = int(sys.argv[2])
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        client_socket.connect((server_ip, port))
        option = choose_exercise_client(client_socket)
        solve_client(option, client_socket)
    except ConnectionRefusedError:
        print(f"Error: Could not connect to server")
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client_socket.close()

if __name__ == "__main__":
    main()