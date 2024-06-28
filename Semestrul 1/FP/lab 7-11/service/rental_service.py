from domain import Rental, Customer, Movie
from domain import RentalValidator
from functions import sort_by_rent
from repo.rental_file_repository import RentalFileRepository
from service import CustomerService
from service import MovieService


class RentalService:
    def __init__(self, path1, path2, path3):
        self.__repo = RentalFileRepository(path1)
        self.__validator = RentalValidator()
        self.movies_service = MovieService(path2)
        self.customers_service = CustomerService(path3)

    def add_rental(self, movie_id, customer_id):
        rental = Rental(self.movies_service.search_movie_by_id(movie_id),
                        self.customers_service.search_customer_by_id(customer_id))
        if self.__validator.validate(rental):
            self.__repo.add(rental)

    def del_rental(self, title, customer_name):
        for element in self.get_all():
            if element.movie.title == title:
                if element.customer.name == customer_name:
                    self.get_all().remove(element)
                    return 1
        return 0

    def del_rental_from_all_by_movie(self, title):
        movie = self.movies_service.search_movie_by_title(title)
        while movie in self.__repo.get_all_movies():
            for element in self.__repo.get_all():
                if element.movie == movie:
                    self.__repo.get_all().remove(element)

    def del_rental_from_all_by_customer(self, cnp):
        customer = self.customers_service.search_customer_by_cnp(cnp)
        while customer in self.__repo.get_all_customers():
            for element in self.__repo.get_all():
                if element.customer.cnp == cnp:
                    self.__repo.get_all().remove(element)

    def sort_by_most_rents(self):
        rents = []
        length = len(self.customers_service.get_all()) - 1
        iterations = self.customers_service.get_all()[length].id
        for i in range(0, iterations + 1):
            rents.append(0)
        for element in self.get_all():
            rents[element.customer.id] += 1
        for i in range(length, iterations):
            rents.pop()

        return rents

    def sort_by_most_rented(self):
        rents = []
        movies_id = []
        length = len(self.movies_service.get_all()) - 1
        iterations = self.movies_service.get_all()[length].id
        for i in range(0, iterations + 1):
            movies_id.append(i)
            rents.append(0)
        for element in self.get_all():
            rents[element.movie.id] += 1
        sort_by_rent(rents, movies_id)
        for i in range(length, iterations):
            rents.pop()
            movies_id.pop()

        return movies_id, rents

    def get_all(self):
        return self.__repo.get_all()


def test_add():
    rental_service = RentalService("", "", "")
    rental_service.movies_service.add_movie("m1", "horror", 10)
    rental_service.movies_service.add_movie("m2", "horror", 10)

    rental_service.customers_service.add_customer("c1", "2951005332651")
    rental_service.customers_service.add_customer("c2", "1951005332651")

    rental_service.add_rental(1, 1)
    rental_service.add_rental(2, 1)
    rental_service.add_rental(2, 2)

    assert rental_service.get_all()[0].movie.title == "m1"
    assert rental_service.get_all()[0].customer.name == "c1"

    Customer.customer_count = 0
    Movie.movies_count = 0


def test_del():
    rental_service = RentalService("", "", "")
    rental_service.movies_service.add_movie("m1", "horror", 10)
    rental_service.movies_service.add_movie("m2", "horror", 10)

    rental_service.customers_service.add_customer("c1", "2951005332651")
    rental_service.customers_service.add_customer("c2", "1951005332651")

    rental_service.add_rental(1, 1)
    rental_service.add_rental(2, 1)
    rental_service.add_rental(2, 2)

    rental_service.del_rental("m1", "c1")

    assert rental_service.get_all()[0].movie.title == "m2"
    assert rental_service.get_all()[0].customer.name == "c1"

    Customer.customer_count = 0
    Movie.movies_count = 0


def test_del_from_all_by_customer():
    rental_service = RentalService("", "", "")
    rental_service.movies_service.add_movie("m1", "horror", 10)
    rental_service.movies_service.add_movie("m2", "horror", 10)

    rental_service.customers_service.add_customer("c1", "2951005332651")
    rental_service.customers_service.add_customer("c2", "1951005332651")

    rental_service.add_rental(1, 1)
    rental_service.add_rental(2, 1)
    rental_service.add_rental(2, 2)
    rental_service.add_rental(1, 1)

    rental_service.del_rental_from_all_by_customer("2951005332651")

    assert rental_service.get_all()[0].movie.title == "m2"
    assert rental_service.get_all()[0].customer.name == "c2"

    Customer.customer_count = 0
    Movie.movies_count = 0


def test_sort_most_rents():
    rental_service = RentalService("", "", "")
    rental_service.movies_service.add_movie("m1", "horror", 10)
    rental_service.movies_service.add_movie("m2", "horror", 10)

    rental_service.customers_service.add_customer("c1", "2951005332651")
    rental_service.customers_service.add_customer("c2", "1951005332651")

    rental_service.add_rental(1, 1)
    rental_service.add_rental(2, 1)
    rental_service.add_rental(2, 2)

    l1, l2 = rental_service.sort_by_most_rents()

    assert l1 == [1, 2]
    assert l2 == [2, 1]

    Customer.customer_count = 0
    Movie.movies_count = 0


def test_sort_by_most_rented():
    rental_service = RentalService("", "", "")
    rental_service.movies_service.add_movie("m1", "horror", 10)
    rental_service.movies_service.add_movie("m2", "horror", 10)

    rental_service.customers_service.add_customer("c1", "2951005332651")
    rental_service.customers_service.add_customer("c2", "1951005332651")

    rental_service.add_rental(1, 1)
    rental_service.add_rental(2, 1)
    rental_service.add_rental(2, 2)

    movies, trash = rental_service.sort_by_most_rented()
    assert movies == [2, 1]

    Customer.customer_count = 0
    Movie.movies_count = 0


def test_inversa():
    rental_service = RentalService("", "", "")
    rental_service.movies_service.add_movie("m1", "horror", 10)
    rental_service.movies_service.add_movie("m2", "horror", 10)

    rental_service.customers_service.add_customer("c1", "2951005332651")
    rental_service.customers_service.add_customer("c2", "1951005332651")

    rental_service.add_rental(1, 1)
    rental_service.add_rental(2, 1)
    rental_service.add_rental(2, 2)

    l1 = l2 = []
    l1, l2 = rental_service.sort_by_most_rented()

    assert l1 == [2, 1]  # id-uri filme
    assert l2 == [2, 1]  # nr de inchirieri / film

    Customer.customer_count = 0
    Movie.movies_count = 0


def test_rental_service():
    test_add()
    test_del()
    test_del_from_all_by_customer()
    test_sort_most_rents()
    test_sort_by_most_rented()
    test_inversa()


# test_rental_service()
