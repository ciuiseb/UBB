from typing import List

from Domain.show_class import Show


class RepoError(Exception):
    pass


class ShowRepository:
    def __init__(self, file_path):
        self.__elements = []
        self.__file_path = file_path
        self.read_from_file()

    def __len__(self):
        """

        :return: numarul de elemente din repository
        """
        return len(self.__elements)

    def get_all(self) -> List[Show]:
        """
        Functie care returneaza elementele din repository
        :return: elements
        """
        return self.__elements

    def add(self, new):
        """
        Adauga un nou element in repository
        :param new: Element care urmeaza sa fie adaugat in repository
        :return: n/a
        """
        self.__elements.append(new)

    def read_from_file(self):
        """
        Adauga in repository elementele din fisierul transmis ca parametru repository-ului. Eroare, daca nu exista fisierul
        :return: n/a
        """
        try:
            with open(self.__file_path, "r") as f:
                for line in f:
                    title, artist, genre, duration = line.strip().split(",")
                    duration = int(duration)
                    new_show = Show(title, artist, genre, duration)
                    self.add(new_show)
        except FileNotFoundError as nf:
            raise RepoError(nf)

    def write_to_file(self):
        """
        Inlocuieste fostul text din fisier cu elemente din repository. Eroare, daca nu exista fisierul
        :return: n/a
        """
        try:
            with open(self.__file_path, "w") as f:
                for element in self.get_all():
                    show_str = f"{element.title},{element.artist},{element.genre},{int(element.duration)}"
                    f.write(show_str + "\n")
        except FileNotFoundError as nf:
            raise RepoError(nf)

    def clear(self):
        """
        Sterge toate elementele din lista si goleste fisierul.
        :return: n/a
        """
        self.__elements = []
        self.write_to_file()
