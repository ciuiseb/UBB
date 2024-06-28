import random


def random_title():
    """
    Creeaza un cuvant format doar din litere cu lungimea <=20
    :return: titlu random
    """
    letters = "qwertyuiopasdfghjklzxcvbnm "
    iterations = random.randint(1, 20)
    title = ""
    while iterations:
        title += random.choice(letters)
        iterations -= 1
    return title


def random_genre(genres_list):
    """
    Alege dintr-o lista data de genuri de filme, anterior aleasa, un element
    :param genres_list: Lista de genuri acceptate din repo
    :return: unul dintre genuri
    """
    return random.choice(genres_list)


def random_rating():
    """
    Creeaza un numar <=10
    :return: Numarul generat
    """
    rating = random.randint(0, 10)
    rating += random.random()
    while rating > 10:
        rating -= random.random()
    return rating


def sort_by_rent(rents, id):
    """
    Sorteaza lista de clienti cu cele mai multe filme descrescator
    :param rents: Filmele fiecarui client
    :param id: Id-urile corespunatoare
    :return: Cele 2 lista, sortate
    """
    for i in range(0, len(rents)):
        for j in range(i, len(rents)):
            if rents[i] < rents[j]:
                rents[i], rents[j] = rents[j], rents[i]
                id[i], id[j] = id[j], id[i]


def test_sort_by_rent():
    list1 = [2, 5, 3]
    list2 = [1, 2, 3]

    sort_by_rent(list1, list2)
    assert list1 == [5, 3, 2]
    assert list2 == [2, 3, 1]


test_sort_by_rent()
