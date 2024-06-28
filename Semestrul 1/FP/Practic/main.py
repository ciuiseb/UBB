from ui import *

if __name__ == '__main__':
    try:
        repo = ShowRepository("Domain/input.txt")
        service = ShowService(repo)
        ui = UI(service)
        ui.run()
    except RepoError as e:
        print("File not found ", e)
