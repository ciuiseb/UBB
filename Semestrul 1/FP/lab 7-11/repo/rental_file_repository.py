from domain import Rental, Movie, Customer
from repo import RentalRepository


class RentalFileRepository(RentalRepository):
    def __init__(self, fileN):
        RentalRepository.__init__(self)

        self.__fileName = fileN
        self.__loadFromFile()

    def __loadFromFile(self):
        try:
            file = open(self.__fileName, "r")
        except IOError:
            return
        line = file.readline().strip()
        while line != "":
            attributes = line.split(",")
            rental = Rental(Movie(attributes[0], attributes[1], float(attributes[2])),
                            Customer(attributes[4], attributes[5]))
            rental.movie.id = int(attributes[3])
            rental.customer.id = int(attributes[6])
            RentalRepository.add(self, rental)
            line = file.readline()
        file.close()
        self.__storeToFile()
    def clear(self):
        self.elements = []
        self.__storeToFile()
    def add(self, val):
        self.elements.append(val)
        self.__storeToFile()
    def __storeToFile(self):
        try:
            file = open(self.__fileName, "w")
        except IOError:
            return
        for rental in RentalRepository.get_all(self):
            rntl = rental.movie.title + "," + rental.movie.genre + "," + str(rental.movie.rating) + "," + str(
                rental.movie.id) + "," + rental.customer.name + "," + rental.customer.cnp + "," + str(
                rental.customer.id) + "\n"
            file.write(rntl)
        file.close()
def test_rental_file():
    repo = RentalFileRepository("Test_rentalsFile.txt")
    repo.clear()

    repo.add(Rental(Movie("m1", "horror", 10), Customer("c1", "2951005332651")))
    assert len(repo) == 1
test_rental_file()