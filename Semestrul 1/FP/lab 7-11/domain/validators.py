from domain.customers import Customer
from domain.movies import Movie
from domain.rental import Rental
from repo import RentalRepository
from repo import CustomersRepository
from repo import MovieRepository


class MovieValidator:
    def __init__(self):
        self.__accepted_genre = ["horror", "comedy", "western", "love", "action"]
        self.__repo = MovieRepository()

    def validate(self, movie: Movie):
        errors = []
        if movie.genre not in self.__accepted_genre:
            errors.append("Genul nu este valid")
        if movie.title in self.__repo.get_titles():
            errors.append("Filmul exista deja")
        if not isinstance(movie.rating, int) and not isinstance(movie.rating, float):
            errors.append("Ratingul filmului trebuie sa fie numar")
        if int(movie.rating) > 10:
            errors.append("Ratingul trebuie sa fie mai mic decat 10")
        if errors:
            print(errors)
            raise errors
        return True

    def get_accepted_genres(self):
        return self.__accepted_genre


class CustomerValidator:
    def __init__(self):
        self.__repo = CustomersRepository()

    def validate(self, customer: Customer):
        errors = []
        if len(customer.cnp) != 13:
            errors.append("CNP-ul introdus nu este valid")
        if customer.cnp in self.__repo.get_cnps():
            errors.append("Doi clienti nu pot sa aiba acelasi cnp!")
        if errors:
            print(errors)
            raise errors
        return True


class RentalValidator:
    def __init__(self):
        self.__repo = RentalRepository

    def validate(self, rental: Rental):
        errors = []

        return True
