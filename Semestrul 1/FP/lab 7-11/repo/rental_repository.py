from domain import Rental, Movie, Customer


class RentalRepository:
    def __init__(self):
        self.elements = []

    def add(self, rented_movie: Rental):
        self.elements.append(rented_movie)

    def get_all(self):
        return self.elements

    def get_all_movies(self):
        movies = []
        for element in self.elements:
            movies.append(element.movie)
        return movies

    def get_all_customers(self):
        customers = []
        for element in self.elements:
            if element.customer not in customers:
                customers.append(element.customer)
        return customers

    def __len__(self):
        return len(self.elements)


def test_rental_repo():
    rental_repo = RentalRepository()
    rental_repo.add(Rental(Movie("test", "comedy", 8), Customer("adi", "1951005330651")))

    assert rental_repo.get_all()[0].movie.title == "test"
    assert rental_repo.get_all()[0].movie.genre == "comedy"
    assert rental_repo.get_all()[0].movie.rating == 8

    assert rental_repo.get_all()[0].customer.name == "adi"
    assert rental_repo.get_all()[0].customer.cnp == "1951005330651"

    assert rental_repo.get_all_customers()[0].name == "adi"

    assert rental_repo.get_all_movies()[0].title == "test"


test_rental_repo()
Customer.customer_count = 0
Movie.movies_count = 0
