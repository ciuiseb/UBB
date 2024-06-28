class Movie:
    movies_count = 0

    def __init__(self, title, genre, rating):
        Movie.movies_count += 1
        self.__id = Movie.movies_count
        self.__title = title
        self.__genre = genre
        self.__rating = rating

    @property
    def id(self):
        return self.__id

    @id.setter
    def id(self, value):
        self.__id = value

    @property
    def title(self):
        return self.__title

    @title.setter
    def title(self, new_title):
        self.__title = new_title

    @property
    def genre(self):
        return self.__genre

    @genre.setter
    def genre(self, new_genre):
        self.__genre = new_genre

    @property
    def rating(self):
        return self.__rating

    @rating.setter
    def rating(self, new_rating):
        self.__rating = int(new_rating)

    def __eq__(self, other):
        return self.title == other.title and self.genre == other.genre and self.rating == other.rating

    def __ne__(self, other):
        return not (self.title == other.title and self.genre == other.genre and self.rating == other.rating)

    def __str__(self):
        return f"[Titlu: {self.title}, Gen: {self.genre}, Rating: {self.rating}, ID: {self.id}]"


def test_movie():
    movie = Movie("test", "horror", 9)
    assert movie.title == "test"
    assert movie.genre == "horror"
    assert movie.rating == 9

    movie.title = "test1"
    movie.genre = "comedy"
    movie.rating = 8

    assert movie.title == "test1"
    assert movie.genre == "comedy"
    assert movie.rating == 8


test_movie()
Movie.movies_count = 0
