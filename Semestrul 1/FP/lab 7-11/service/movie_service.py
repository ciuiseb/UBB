from domain import Movie
from domain import MovieValidator
from functions.auxiliary_functions import *
from repo import MovieFileRepository


class MovieService:
    def __init__(self, path):
        self.repo = MovieFileRepository(path)
        # self.repo = MovieRepository()
        self.__validator = MovieValidator()

    def add_movie(self, title, genre, rating):
        """

        :param title:
        :param genre:
        :param rating:
        :return:
        """
        movie = Movie(title, genre, float(rating))
        if self.__validator.validate(movie):
            self.repo.add(movie)

    def del_movie(self, titlu):
        self.repo.get_all().remove(self.search_movie_by_title(titlu))

    def modify_movie(self, title, modification, new_atribute):
        movie = self.search_movie_by_title(title)
        if modification == "titlul":
            movie.title = new_atribute
        elif modification == "genul":
            movie.genre = new_atribute
        elif modification == "ratingul":
            movie.rating = new_atribute

        for element in self.repo.get_all():
            if element.id == movie.id:
                element.title = movie.title
                element.genre = movie.genre
                element.raintg = movie.rating

    def search_movie_by_title(self, title):
        """
        Returneaza un obiect tip Movie pe baza titlului introdus
        :param title:
        :return:
        """
        """
        Best case: titlul cautat este pe prima poizitie => T(n) = 1 ∈ O(1)
        Worst case: titlul cautat este pe ultima poizite => T(n) = n ∈ O(n)
        Caz mediu: for-ul executa (n+1)/2 pasi => T(n) = n ∈ O(n)
        """
        if title in self.repo.get_titles():
            for element in self.repo.get_all():
                if element.title == title:
                    return element
        return 0

    def search_movie_by_id(self, id):
        if id <= len(self.repo.get_all()):
            for element in self.get_all():
                if element.id == id:
                    return element
        return 0

    def get_all(self):
        return self.repo.get_all()

    def get_accepted_genres(self):
        return self.__validator.get_accepted_genres()

    def add_random_movie(self):
        title = random_title()
        genre = random_genre(self.get_accepted_genres())
        rating = random_rating()
        self.add_movie(title, genre, rating)


def test_add():
    movie_service = MovieService("")
    movie_service.add_movie("test", "horror", 1)

    assert movie_service.repo.get_all()[0].title == "test"
    assert movie_service.repo.get_all()[0].genre == "horror"
    assert movie_service.repo.get_all()[0].rating == 1
    Movie.movies_count = 0


def test_add_random_movie():
    movie_service = MovieService("")

    random.seed(1)
    movie_service.add_random_movie()

    assert movie_service.get_all()[0] == Movie("lmneo", "horror", 7.760962444912575)

    Movie.movies_count = 0


def test_del():
    movie_service = MovieService("")
    movie_service.add_movie("test", "horror", 1)
    movie_service.add_movie("test1", "horror", 10)
    movie_service.del_movie("test")

    assert movie_service.repo.get_all()[0].title == "test1"
    assert movie_service.repo.get_all()[0].genre == "horror"
    assert movie_service.repo.get_all()[0].rating == 10

    Movie.movies_count = 0


def test_modify():
    movie_service = MovieService("")
    movie_service.add_movie("test1", "horror", 10)

    movie_service.modify_movie("test1", "genul", "comedy")
    movie_service.modify_movie("test1", "ratingul", 8)
    movie_service.modify_movie("test1", "titlul", "test")

    assert movie_service.repo.get_all()[0].title == "test"
    assert movie_service.repo.get_all()[0].genre == "comedy"
    assert movie_service.repo.get_all()[0].rating == 8
    Movie.movies_count = 0


def test_search_by_title():
    movie_service = MovieService("")
    movie_service.add_movie("test", "horror", 1)

    assert movie_service.search_movie_by_title("test") == Movie("test", "horror", 1)
    Movie.movies_count = 0


def test_search_by_id():
    movie_service = MovieService("")
    movie_service.add_movie("test", "horror", 1)

    assert movie_service.search_movie_by_id(1) == Movie("test", "horror", 1)
    Movie.movies_count = 0


def test_movie_service():
    test_add()
    test_add_random_movie()
    test_del()
    test_modify()
    test_search_by_title()
    test_search_by_id()


test_movie_service()
