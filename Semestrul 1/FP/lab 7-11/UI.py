from service import RentalService
from repo.customers_file_repository import RepoError
from functions.sortari import (insertion_sort,
                               comb_sort)


class UI:
    def __init__(self):
        self.rental_service = RentalService("service/rentalsFile.txt",
                                            "service/moviesFile.txt",
                                            "service/customersFile.txt")

    def meniu(self):
        print("""
        1. Adauga film
        2. Sterge film
        3. Modifica film 
        4. Adauga client
        5. Sterge client  
        6. Modifica client           
        7. Cautati un film in functie de nume
        8. Cautati un film in functie de id
        9. Cauta un client in functie de nume
        10. Cauta un client in functie de id       
        11. Adauga un film cu nume, gen si rating random
        12. Adauga o noua inchiriere
        13. Sterge o inchiriere        
        14. Sortare in functie de numele clientilor
        15. Sortare in functie de numarul de filme inchiriare
        16. Top3 cele mai inchiriate filme
        17. Primii 30% clienti cu cele mai multe filme inchiriate
        17. Primele 30% filme cu cele mai multe inchirieri
        21. Print list of movies
        22. Print list of customers
        23. Print rentals
        0. Stop
        """)

    def print_customers(self, n):
        if n == 0:
            print(self.rental_service.customers_service.get_all()[0])
            return
        print(self.rental_service.customers_service.get_all()[n])
        self.print_customers(n - 1)

    def print_movies(self, m, n):
        if m == n:
            return
        print(self.rental_service.movies_service.get_all()[m])
        self.print_movies(m + 1, n)

    def print_rentals(self):
        for rental in self.rental_service.get_all():
            print(rental)

    def run(self):
        try:
            ok = 1
            while ok:
                self.meniu()
                option = input("Alegeti o optiune: ")

                if option == '1':
                    titlu = input("Introduceti un titlu: ")
                    genre = input("introduceti un gen: ")
                    rating = input("Introduceti un rating: ")
                    self.rental_service.movies_service.add_movie(titlu, genre, rating)

                elif option == '2':
                    movie_title = input("Introduceti numele filmului pentru a-l sterge: ")
                    if self.rental_service.movies_service.search_movie_by_title(movie_title):
                        self.rental_service.movies_service.del_movie(movie_title)
                    else:
                        print("Nu exista filmul ales!")

                elif option == '3':
                    movie_title = input("Introduceti numele filmului pentru a-l modifica: ")
                    if self.rental_service.movies_service.search_movie_by_title(movie_title):
                        modification = input("Ce doriti sa modificati?")
                        if (modification == "titlul" or modification == "genul" or modification == "ratingul"):
                            new_atribute = input("Introduceti noua valoare: ")
                            self.rental_service.movies_service.modify_movie(movie_title, modification, new_atribute)
                        else:
                            print("Nu exista atributul ales")
                    else:
                        print("Nu exista filmul ales!")

                elif option == '4':
                    name = input("Introduceti numele: ")
                    cnp = input("Introduceti cnp-ul: ")
                    self.rental_service.customers_service.add_customer(name, cnp)

                elif option == '5':
                    customer_id = int(input("introduceti id-ul clientului"))
                    self.rental_service.customers_service.del_customer(customer_id)


                elif option == '6':
                    customer_name = input("Ce client doriti sa modificati? ")
                    customer = self.rental_service.customers_service.search_customer_by_name(customer_name)
                    if customer == 0:
                        print("Nu exista niciun client cu acest nume!")
                    elif customer == 1:
                        print("Sunt mai multi clienti cu acelasi nume! Cautati in functi de cnp")
                        cnp = input("Introduceti cnpul: ")
                        if cnp:
                            modification = input("Ce doriti sa modificati? ")
                            new_atribute = input("Introduceti noua valoare")
                            self.rental_service.customers_service.modify_customer(cnp, modification, new_atribute)
                    else:
                        modification = input("Ce doriti sa modificati? ")
                        new_atribute = input("Introduceti noua valoare")
                        self.rental_service.customers_service.modify_customer(customer.cnp, modification, new_atribute)

                elif option == '7':
                    movie_title = input("Ce film cautati? Introduceti numele: ")
                    movie = self.rental_service.movies_service.search_movie_by_title(movie_title)
                    if movie:
                        movie.print_movie()
                    else:
                        print("Filmul cautat nu exista!")

                elif option == '8':
                    movie_id = int(input("Ce film cautati? Introduceti id-ul: "))
                    movie = self.rental_service.movies_service.search_movie_by_id(movie_id)
                    if movie:
                        print(movie)
                    else:
                        print("Filmul cautat nu exista!")

                elif option == '9':
                    name = input("Introduceti numele cautat: ")
                    customer = self.rental_service.customers_service.search_customer_by_name(name)
                    if customer == 0:
                        print("Clientul cautat nu exista!")
                    elif customer == 1:
                        print("Sunt mai multi clienti cu acelasi nume!")
                        cnp = input("Introduceti cnpul!")
                        customer = self.rental_service.customers_service.search_customer_by_cnp(cnp)
                        if customer:
                            print(customer)
                        else:
                            print("Nu exista niciun client cu acest nume!")
                    else:
                        print(customer)

                elif option == '10':
                    id = int(input("Introduceti idul"))
                    customer = self.rental_service.customers_service.search_customer_by_id(id)
                    if customer:
                        print(customer)
                    else:
                        print("Nu exista un client cu idul cerut")

                elif option == '11':
                    self.rental_service.movies_service.add_random_movie()

                elif option == "12":
                    movie_title = input("Ce film doriti sa fie inchiriat?")
                    if self.rental_service.movies_service.search_movie_by_title(movie_title):
                        customer_name = input("De catre cine?")
                        customer = self.rental_service.customers_service.search_customer_by_name(customer_name)
                        if customer == 0:
                            print("Clientul cautat nu exista!")
                        elif customer == 1:
                            print("Sunt mai multi clienti cu acelasi nume!")
                            cnp = input("Introduceti cnpul!")
                            customer = self.rental_service.customers_service.search_customer_by_cnp(cnp)
                            if customer:
                                self.rental_service.add_rental(movie_title, cnp)
                            else:
                                print("Nu exista niciun client cu acest nume!")
                        else:
                            self.rental_service.add_rental(movie_title, customer.cnp)
                    else:
                        print("Filmul nu exista!")

                elif option == '13':
                    movie_title = input("Introduceti numele filmului: ")
                    customer_name = input("Introduceti numele clientului: ")
                    aux = self.rental_service.del_rental(movie_title, customer_name)

                    if aux == 0:
                        print("Nu exista inchirierea cautata!")

                elif option == '14':
                    sorted_list = insertion_sort(self.rental_service.get_all(), key=lambda x: x.customer.name)
                    for element in sorted_list:
                        print(element)
                elif option == "15":
                    res, temp = self.rental_service.sort_by_most_rents()
                    for element in res:
                        print(self.rental_service.customers_service.search_customer_by_id(element))
                elif option == "16":
                    movies, trash = self.rental_service.sort_by_most_rented()
                    for i in range(0, 3):
                        print(self.rental_service.movies_service.search_movie_by_id(movies[i]))
                elif option == "17":
                    length = int(len(self.rental_service.customers_service.get_all()) * 0.3)
                    customers, rents = self.rental_service.sort_by_most_rents()
                    for i in range(0, length):
                        print(self.rental_service.customers_service.search_customer_by_id(customers[i]), rents[i])
                elif option == "18":
                    length = int(len(self.rental_service.movies_service.get_all()) * 0.3)
                    movies, rents = self.rental_service.sort_by_most_rented()
                    for i in range(0, length):
                        print(
                            f"{self.rental_service.movies_service.search_movie_by_id(movies[i])}, fiind inchiriat de {rents[i]} ori")
                elif option == "21":
                    self.print_movies(0, len(self.rental_service.movies_service.get_all()))
                elif option == "22":
                    self.print_customers(len(self.rental_service.customers_service.get_all()) - 1)
                elif option == "23":
                    self.print_rentals()
                elif option == '0':
                    ok = 0
                else:
                    print("Optiune invalida")
        except RepoError as e:
            print("File not found", e)
