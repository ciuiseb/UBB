import random

from Repository.repository import *
from Domain.validator import *


class ShowService:
    def __init__(self, repo: ShowRepository):
        self.repo = repo
        self.validator = Validator()

    def get_all(self):
        """
        :return: Returneaza elementele din repository
        """
        return self.repo.get_all()

    def add(self, element):
        """
        Valideaza un element nou, iar daca este valid il adauga in repository si updateaza fileul
        :param element: obiect tip Spectacol
        :return: n/a
        """
        if self.validator.validate(element):
            element.duration = int(element.duration)
            self.repo.add(element)
            self.repo.write_to_file()

    def search_by_title_and_artist(self, title, artist):
        """
        Cauta daca exista un element cu titlu si artist dat
        :param title:
        :param artist:
        :return: Elementul, sau False daca nu exista
        """
        for element in self.get_all():
            if element.title == title and element.artist == artist:
                return element
        return False

    def modify_genre_and_duration(self, show, genre, duration):
        """
        Modifica genul si durata unui element dat din repository, dupa care modifica in fisier
        :param show: Elementul din repo
        :param genre: Noul gen
        :param duration: Noua durata
        :return: n/a
        """
        for element in self.get_all():
            if element == show:
                element.genre = genre
                element.duration = duration
                break
        self.repo.write_to_file()

    def get_random_string(self):
        """
        Functie care genereaza un string aleatoriu in care consoanele si vocalele sunt alternate.
        :return: string
        """
        vocale = "aeiou"
        consoane = "qwrtyplkjhgfdszxcvbnm"

        string = ""

        length = random.randint(9, 12)
        chars_before_space = random.randint(1, length - 1)

        alternate = random.choice([0, 1])

        for i in range(chars_before_space):
            if alternate == 0:  # vocale
                string += random.choice(vocale)
            else:  # consoane
                string += random.choice(consoane)
            alternate = 1 - alternate
        string += " "
        for i in range(length - chars_before_space - 1):
            if alternate == 0:  # vocale
                string += random.choice(vocale)
            else:  # consoane
                string += random.choice(consoane)
            alternate = 1 - alternate
        return string

    def get_random_genre(self):
        """
        Alege unul dintre genele acceptate
        :return: genul ales
        """
        accepted_genres = ["Comedie", "Concert", "Balet", "Altele"]
        return random.choice(accepted_genres)

    def get_random_duration(self):
        """
        Alege o durata aleeatorie din intervalul [0, 10 000]
        :return: durata aleasa
        """
        return random.randint(0, 10000)

    def get_random_shows(self, count):
        """
        Returneaza 'count' specatcole generate aleatoriu cu functiile de mai sus
        :param count: numar introdus de utilizator
        :return:lista cu spectacolele generatae
        """
        shows = []
        for i in range(count):
            title = self.get_random_string()
            artist = self.get_random_string()
            genre = self.get_random_genre()
            duration = self.get_random_duration()
            random_specacle = Show(title, artist, genre, duration)

            shows.append(random_specacle)
            self.add(random_specacle)
        return shows

    def sort_by_artist_then_title(self):
        """
        Sorteaza elementele din repository in functie de artist, departajandu-le in functie de titlu. Daca nu exista fisierul il creeaza
        :return: lista soratata
        """
        list = self.get_all().copy()
        return sorted(list, key=lambda x: (x.artist, x.title))

    def write_to_file_list(self, file_name, list):
        """
        Scrie o lista data intr-un fisier dat
        :param file_name: numele fisierului
        :param list: lista
        :return: n/a
        """
        with open(file_name, "w") as f:
            for element in list:
                show_str = f"{element.title},{element.artist},{element.genre},{int(element.duration)}"
                f.write(show_str + "\n")
