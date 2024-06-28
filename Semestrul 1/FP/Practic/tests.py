import os
import unittest

from Service.service import *


class ShowTest(unittest.TestCase):
    def setUp(self):
        self.show = Show("aaa", "bbb", "Concert", 3600)

    def test_title_getter(self):
        self.assertEqual("aaa", self.show.title)

    def test_artist_getter(self):
        self.assertEqual("bbb", self.show.artist)

    def test_tgenre_getter(self):
        self.assertEqual("Concert", self.show.genre)

    def test_duratoin_getter(self):
        self.assertEqual(3600, self.show.duration)

    def test_genre_setter(self):
        self.show.genre = "Altele"
        self.assertEqual("Altele", self.show.genre)

    def test_duratoin_setter(self):
        self.show.duration = 1
        self.assertEqual(1, self.show.duration)


class RepositoryTest(unittest.TestCase):
    def setUp(self):
        self.repo = ShowRepository("test_file.txt")
        self.repo.add(Show("titlu1", "artist1", "Comedie", 123))
        self.repo.add(Show("titlu2", "artist2", "Balet", 321))

    def tearDown(self):
        self.repo.clear()

    def test_get_all(self):
        self.assertEqual("titlu1", self.repo.get_all()[0].title)

    def test_add(self):
        self.assertEqual(2, len(self.repo))

    def test_write_read(self):
        self.repo.write_to_file()
        self.repo.read_from_file()
        # daca citirea si scrierea merg, se vor adauga in file elementele initale, iar dupa recitire lungimea va fi dubla celei intiale
        self.assertEqual(4, len(self.repo))


class ValidatorTest(unittest.TestCase):
    def setUp(self):
        self.validator = Validator()

    def test_validate(self):
        invalid_inputs = [Show("", "artist", "Comedie", 1000),
                          Show("title", "", "Comedie", 1000),
                          Show("title", "artist", "atl gen", 1000),
                          Show("title", "artist", "Comedie", -1),
                          Show("title", "artist", "Comedie", 1.3),
                          Show("title", "artist", "Comedie", "aa")]

        for input in invalid_inputs:
            with self.assertRaises(ValidatorError):
                self.validator.validate(input)


class ServiceTest(unittest.TestCase):
    def setUp(self):
        repo = ShowRepository("test_file.txt")
        self.service = ShowService(repo)
        self.service.add(Show("titlu1", "artist1", "Comedie", 123))
        self.service.add(Show("titlu2", "artist2", "Balet", 321))

    def tearDown(self):
        self.service.repo.clear()

    def test_get_all(self):
        self.assertEqual("titlu1", self.service.get_all()[0].title)

    def test_add(self):
        self.assertEqual(2, len(self.service.repo))

    def test_search_by_title_and_artist(self):
        self.assertFalse(self.service.search_by_title_and_artist("titlu", "artist"))
        self.assertEqual("Comedie", self.service.search_by_title_and_artist("titlu1", "artist1").genre)

    def test_modify_genre_and_duration(self):
        show = Show("titlu1", "artist1", "Comedie", 123)
        self.service.modify_genre_and_duration(show, "Altele", 12)
        self.assertEqual("Altele", self.service.get_all()[0].genre)
        self.assertEqual(12, self.service.get_all()[0].duration)

    def test_get_random_string(self):
        random.seed(1)
        string = self.service.get_random_string()
        self.assertEqual("to zomolax", string)

    def test_get_random_genre(self):
        random.seed(2)
        genre = self.service.get_random_genre()
        self.assertEqual("Comedie", genre)

    def test_get_random_duration(self):
        random.seed(3)
        duration = self.service.get_random_duration()
        self.assertEqual(3898, duration)

    def test_get_random_shows(self):
        specs = self.service.get_random_shows(100)
        self.assertEqual(102, len(self.service.repo))
        self.assertEqual(100, len(specs))

    def test_sort_by_artist_then_title(self):
        self.service.add(Show("titlu0", "artist1", "Comedie", 123))
        sorted_list = self.service.sort_by_artist_then_title()
        self.assertEqual("titlu0", sorted_list[0].title)

    def test_write_to_file_list(self):
        self.service.write_to_file_list("test.txt",
                                        [Show("titlu0", "artist1", "Comedie", 123)])
        try:
            repo = ShowRepository("test.txt")
            self.assertEqual(1, len(repo))
            os.remove("test.txt")
        except RepoError:
            assert False
