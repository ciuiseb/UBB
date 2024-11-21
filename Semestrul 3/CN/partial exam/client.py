import random
import socket
import sys


def validate_input():
    while True:
        try:
            n = int(input("Enter array ength: "))
            if n <= 0:
                print("Length must be greater than 0")
                continue
            return n
        except ValueError:
            print("Enter a number!")


def validate_number(i):
    while True:
        try:
            num = int(input(f"array[{i}]= "))
            return num
        except ValueError:
            print("Enter a number!")

def client_type_tcp(ip):
    port = random.choice([1551, 1771])

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        client.connect((ip, port))
    except Exception:
        print("Can't connect to server!")
        return

    try:
        n = validate_input()

        client.send(str(n).encode())
        response = client.recv(1024).decode()

        if response != "OK":
            print("Error sending the length!")
            return

        for i in range(n):
            num = validate_number(i)
            client.send(str(num).encode())
            response = client.recv(1024).decode()

            if response != "OK":
                print(f"Error seding number of index {i}!")
                return

        result = client.recv(1024).decode()
        print(f"The minim is: {result}")

    except Exception as e:
        print(f"Error: {e}")
    finally:
        client.close()


def client_type_udp(ip):
    client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        client.sendto("ready".encode(), (ip, 1661))

        data, _ = client.recvfrom(1024)
        minimum = int(data.decode())
        print(f"The global minimum is: {minimum}")

    except Exception as e:
        print(f"Error: {e}")
    finally:
        client.close()

def main():
    if len(sys.argv) != 3:
        print("Usage: python client.py <ip> <flag>")
        print("flag must be 1 or 2")
        return

    ip = sys.argv[1]
    try:
        flag = int(sys.argv[2])
        if flag not in [1, 2]:
            raise ValueError
    except ValueError:
        print("Flag must be 1 or 2")
        return
    if flag == 1:
        client_type_tcp(ip)
    elif flag == 2:
        client_type_udp(ip)


if __name__ == "__main__":
    main()