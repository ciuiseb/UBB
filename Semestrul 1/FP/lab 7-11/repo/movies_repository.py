from domain import Movie


class MovieRepository:
    def __init__(self):
        self.elements = []

    def add(self, new_movie: Movie):
        self.elements.append(new_movie)

    def get_all(self):
        return self.elements

    def get_titles(self):
        titles = []
        for movie in self.elements:
            titles.append(movie.title)
        return titles

    def __len__(self):
        return len(self.elements)


def test_movies_repo():
    movies_repo = MovieRepository()
    movies_repo.add(Movie("test", "comedy", 8))

    assert movies_repo.get_all()[0].title == "test"
    assert movies_repo.get_all()[0].genre == "comedy"
    assert movies_repo.get_all()[0].rating == 8

    assert movies_repo.get_titles()[0] == "test"

    Movie.movies_count = 0


test_movies_repo()
