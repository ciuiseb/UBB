from Service.service import *


class UIError(Exception):
    pass


class UI:
    def __init__(self, service: ShowService):
        self.service = service

    def menu(self):
        print("""
        1. Adauga un spectacol
        2. Modifica un spectacol
        3. Genereza spectacole aleatorii
        4. Export
        0. Exit
        """)

    def get_title(self):
        return input("Introduceti titlul: ")

    def get_artist(self):
        return input("Introduceti artistul: ")

    def get_genre(self):
        return input("Introduceti genul:")

    def get_duration(self):
        return input("Introduceti durata: ")

    def get_new_genre(self):
        return input("Introduceti un nou gen:")

    def get_new_duration(self):
        return input("Introduceti durata: ")

    def get_count(self):
        count = input("Introduceti numarul de spectacole: ")
        try:
            int(count)
        except ValueError:
            raise UIError("Contorul trebuie sa fie un numar! ")
        return int(count)

    def get_file_name(self):
        file = input("Introduceti numele fisierului: ")
        if ".txt" not in file:
            file += ".txt"
        return file

    def print_list(self, list):
        for element in list:
            print(element)

    def run(self):
        try:
            while True:
                self.menu()
                p = input("Introduceti o opiune: ")
                if p == "1":
                    title = self.get_title()
                    artist = self.get_artist()
                    genre = self.get_genre()
                    duration = self.get_duration()
                    new_show = Show(title, artist, genre, duration)
                    self.service.add(new_show)
                elif p == "2":
                    title = self.get_title()
                    artist = self.get_artist()
                    search = self.service.search_by_title_and_artist(title, artist)
                    if search:
                        new_genre = self.get_new_genre()
                        new_duration = self.get_new_duration()
                        errors = []
                        errors += self.service.validator.validate_genre(new_genre)
                        errors += self.service.validator.validate_duration(new_duration)
                        if not errors:
                            self.service.modify_genre_and_duration(search, new_genre, new_duration)
                        else:
                            raise UIError(",".join(errors))
                    else:
                        raise UIError("Nu exista spectacolul cautat")
                elif p == "3":
                    count = self.get_count()
                    if count > 0:
                        print("Spectacole generate: ")
                        generated_shows = self.service.get_random_shows(count)
                        self.print_list(generated_shows)
                    else:
                        print("Nu a fost generat niciun spectacol")

                elif p == "4":
                    file_name = self.get_file_name()
                    sorted_list = self.service.sort_by_artist_then_title()
                    self.service.write_to_file_list(file_name, sorted_list)
                elif p == "0":
                    break
                else:
                    raise UIError("Optiune invalida!")
        except ValidatorError as e:
            print("Eroare de validare: ", e)
            self.run()
        except UIError as e:
            print("Eroare: ", e)
            self.run()
