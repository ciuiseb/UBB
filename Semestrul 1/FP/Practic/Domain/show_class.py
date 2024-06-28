class Show:
    def __init__(self, title, artist, genre, duration):
        self.__title = title
        self.__artist = artist
        self.__genre = genre
        self.__duration = duration

    def __str__(self):
        """

        :return: string formatat format din atributele obiectului
        """
        return f"Titlu: {self.title}, Artist:{self.artist}, Gen: {self.genre}, Durata: {self.duration}"

    def __eq__(self, other):
        """
        Compara 2 elemente
        :param other:
        :return: True daca sunt egale, False atlfel
        """
        if not isinstance(other, Show):
            return False
        return (
                    self.title == other.title and self.artist == other.artist and self.genre == other.genre and self.duration == other.duration)

    @property
    def title(self):
        return self.__title

    @property
    def artist(self):
        return self.__artist

    @property
    def genre(self):
        return self.__genre

    @genre.setter
    def genre(self, new):
        self.__genre = new

    @property
    def duration(self):
        return self.__duration

    @duration.setter
    def duration(self, new):
        self.__duration = new
