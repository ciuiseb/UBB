from domain import Customer
from domain import Movie


class Rental:
    def __init__(self, movie: Movie, customer: Customer):
        self.__movie = movie
        self.__customer = customer

    @property
    def movie(self):
        return self.__movie

    @movie.setter
    def movie(self, new_movie):
        self.__movie = new_movie

    @property
    def customer(self):
        return self.__customer

    @customer.setter
    def customer(self, new_customer):
        self.__customer = new_customer

    def __str__(self):
        return f"[Titlu: {self.__movie.title} - Client: {self.__customer.name}]"


def test_rental():
    rental = Rental(Movie("test", "horror", 9), Customer("test", "2950802129071"))

    assert rental.customer.name == "test"
    assert rental.customer.cnp == "2950802129071"

    assert rental.movie.title == "test"
    assert rental.movie.genre == "horror"
    assert rental.movie.rating == 9


test_rental()
Customer.customer_count = 0
Movie.movies_count = 0
