from domain import Movie
from .movies_repository import MovieRepository


class MovieFileRepository(MovieRepository):
    def __init__(self, fileN):
        MovieRepository.__init__(self)
        self.__fileName = fileN
        self.__loadFromFile()
    def clear(self):
        self.elements = []
        self.__storeToFile()
    def add(self, val):
        self.elements.append(val)
        self.__storeToFile()
    def __loadFromFile(self):
        try:
            file = open(self.__fileName, "r")
        except IOError:
            return
        line = file.readline().strip()
        while line != "":
            attributes = line.split(",")
            movie = Movie(attributes[0], attributes[1], float(attributes[2]))
            movie.id = int(attributes[3])
            MovieRepository.add(self, movie)
            line = file.readline().strip()
        file.close()
        self.__storeToFile()

    def __storeToFile(self):
        try:
            file = open(self.__fileName, "w")
        except IOError:
            return
        for movie in self.get_all():
            mvstr = movie.title + "," + movie.genre + "," + str(movie.rating) + "," + str(movie.id) + "\n"
            file.write(mvstr)
        file.close()

    def get_all(self):
        return MovieRepository.get_all(self)
def test_repo():
    repo = MovieFileRepository("Test_moviesFile.txt")
    repo.clear()

    repo.add(Movie("m1", "horror", 9.2))
    assert len(repo) == 1
test_repo()