"""
	X 1 - 1.1 adauga nr complex la finalul listei - lab 3
	X 2 - 1.2 adauga nr complex la pe pozitia ceruta - lab 4

	X 3 - 2.1 sterge elementul de pe pozitia - lab 4
	X 4 - 2.2 sterge elementele din intervalul - lab 5
	X 5 - 2.3 inlocuieste toate aparitiile unui numar din lista cu altul - lab 5

	X 6 - 3.1 tipareste partea imaginara pentru numerele din interval - lab 3
	X 7 - 3.2 tipareste nr complexe cu modul < 10 - lab 3
	X 8 - 3.3 tipareste nr complexe cu modul = 10 - lab 3

	X 9 - 4.1 suma nr dintr-o subsecventa data - lab 3
	X 10 - 4.2 produsul nr dintr-o subsecventa data - lab 3
	X 11 - 4.3 tipareste lista sortata descrescator dupa partea imaginara - lab 5

	X 12 - 5.1 filtrare partea reala prim - lab 3
	X 13 - 5.2 filtrare modul (<,=, > decat un nr dat) - lab 5

	14 - 6 reface ultima operatie - lab 5  // lista de lista / lista cu operatiile trecute + functii complementare de undo la fiecare functie / lista cu ultimele modificari aduse (adaugat/eliminat nr poz)

	0 -  exit

teste functii - lab 4
teste functii ulterioare - lab 5
!! adaugate teste pentru validarea datelor: functie de citire capete, validare, functie de prelucrare - lab 4 X
"""

from classes.complexNr import Complex

import main_functions.functions
import main_functions.tests

import other_functions.functions
import other_functions.tests

def functions_tests():
    other_functions.tests.test_prim()
    other_functions.tests.test_lists_compare()

    # main_functions.tests.test_add_stack()
    main_functions.tests.test_add_on_position()
    main_functions.tests.test_del_pos()
    main_functions.tests.test_del_interval()
    main_functions.tests.test_nr_replace()
    main_functions.tests.test_print_imag_in_interval()
    main_functions.tests.test_print_nr_mod_equal_10()
    main_functions.tests.test_print_nr_mod_lower_10()
    main_functions.tests.test_subseq_sum()
    main_functions.tests.test_sort_by_imag()
    main_functions.tests.test_subseq_prod()
    main_functions.tests.test_elim_prime_real()


def run():
    functions_tests()

    modificari = []
    list_complex_nr = []
    # lista initiala temporara
    list_complex_nr.append(Complex(1,2))
    list_complex_nr.append(Complex(2,3))
    list_complex_nr.append(Complex(3,4))
    list_complex_nr.append(Complex(4,5))
    list_complex_nr.append(Complex(5,6))
    list_complex_nr.append(Complex(6,7))
    #
    ok = 1
    switch = 1
    if switch == 1:
        while ok:
            main_functions.functions.print_menu()
            p = int(input())
            if p == 1:
                z = Complex(int(input("Partea reala este:")), int(input("Partea imaginara este:")))
                # main_functions.functions.add_stack(list_complex_nr, z, list_of_last_modifications)
                main_functions.functions.add_stack(list_complex_nr, z, modificari)
            elif p == 2:
                z = Complex(int(input("Partea reala este:")), int(input("Partea imaginara este:")))
                p = int(input("Numarul va fi plasat pe pozitia:"))
                if other_functions.functions.pos_check_valid(list_complex_nr, p):
                    list_complex_nr = main_functions.functions.add_on_position(list_complex_nr, z, p, modificari)
            elif p == 3:
                p = int(input("Stergeti numarul de pe pozitia:"))
                if other_functions.functions.pos_check_valid(list_complex_nr, p):
                    list_complex_nr = main_functions.functions.del_pos(list_complex_nr, p, modificari)
            elif p ==4:
                inceput = int(input("Inceputul intervalului este:"))
                final = int(input("Final intervalului este:"))
                if other_functions.functions.ends_check_valid(list_complex_nr, inceput, final) == True:
                    list_complex_nr = main_functions.functions.del_interval(list_complex_nr, inceput, final, modificari)
            elif p == 5:
                z = Complex(int(input("Partea reala a inlocuitului este:")), int(input("Partea imaginara a inlocuitului este:")))
                if other_functions.functions.check_existance(list_complex_nr,z):
                    r = Complex(int(input("Partea reala a inlocuitorului este:")), int(input("Partea imaginara a inlocuitorului este:")))
                    list_complex_nr = main_functions.functions.nr_replace(list_complex_nr, z, r, modificari)
            elif p == 6:
                inceput = int(input("Inceputul intervalului este:"))
                final = int(input("Final intervalului este:"))
                if other_functions.functions.ends_check_valid(list_complex_nr, inceput, final) == True:
                    res = main_functions.functions.print_imag_in_interval(list_complex_nr, inceput, final)
                    print(res)
            elif p == 7:
                res = main_functions.functions.print_nr_mod_lower_10(list_complex_nr)
                other_functions.functions.list_print(res)
            elif p == 8:
                res = main_functions.functions.print_nr_mod_equal_10(list_complex_nr)
                other_functions.functions.list_print(res)
            elif p == 9:
                inceput = int(input("Inceputul intervalului este:"))
                final = int(input("Final intervalului este:"))
                if other_functions.functions.ends_check_valid(list_complex_nr, inceput, final) == True:
                    res = main_functions.functions.subseq_sum(list_complex_nr, inceput, final)
                    res.print_complex()
            elif p == 10:
                inceput = int(input("Inceputul intervalului este:"))
                final = int(input("Final intervalului este:"))
                if other_functions.functions.ends_check_valid(list_complex_nr, inceput, final) == True:
                    res = main_functions.functions.subseq_prod(list_complex_nr, inceput, final)
                    res.print_complex()
            elif p == 11:
                other_functions.functions.list_print(main_functions.functions.sort_by_imag(list_complex_nr))
            elif p == 12:
                list_complex_nr = main_functions.functions.elim_prime_real(list_complex_nr, modificari)
            elif p == 0:
                print("Aplicatie incheiata.")
                ok = 0
            elif p == 13:
                z = int(input("Numarul ales este: "))
                sign = input("Alegeti un semn( <, =, >): ")
                if other_functions.functions.sign_check(sign):
                    list_complex_nr = main_functions.functions.mod_filter(list_complex_nr, sign, z, modificari)
            elif p == 14:
                if len(modificari):
                    list_complex_nr = main_functions.functions.undo(list_complex_nr, modificari, len(modificari) - 1)
                else:
                    print("Nu se pot modifica refaceri inca")
            elif p == 15:
                other_functions.functions.list_print(list_complex_nr)
            elif p == 20:
                for i in range(0, len(modificari)):
                    for j in range(0, len(modificari[i])):
                        print(modificari[i][j])
            else:
                print("Optiune invalida")
    elif switch == 2:
        while(ok):
            comanda = input("Introduceti comanda: ").split()
            if comanda[0] == "adauga":
                if comanda[4] =="pe" and comanda[5] == "pozitia": # adauga X + Yj pe pozitia Z
                    z = main_functions.functions.get_number_command(comanda)
                    if comanda[2] == '-':
                        z.imag *= -1
                    list_complex_nr = main_functions.functions.add_on_position(list_complex_nr, z ,main_functions.functions.get_position_command(comanda), modificari)
                elif comanda[4] =="la" and comanda[5] =="finalul" and comanda[6] == "listei": # adauga X + Yj la finalul listei
                    z = main_functions.functions.get_number_command(comanda)
                    if comanda[2] == "-":
                        z.imag *= -1
                    list_complex_nr = main_functions.functions.add_stack(list_complex_nr, z, modificari)
            elif comanda[0] == "sterge":
                if comanda[1] =="pozitia":
                    list_complex_nr = main_functions.functions.del_pos(list_complex_nr, main_functions.functions.get_position_command(comanda), modificari)
                elif comanda[1] == "intervalul": #sterge intervalul cuprins intre pozitia A si B 4 6
                    list_complex_nr = main_functions.functions.del_interval(list_complex_nr, main_functions.functions.get_inceput_command(comanda), main_functions.functions.get_final_command(comanda), modificari)
            elif comanda[0] == "printeaza" and comanda[1] == "lista":
                other_functions.functions.list_print(list_complex_nr)
            elif comanda[0] == "stop":
                ok = 0
            else:
                print("Comanda invalida")

if __name__ == '__main__':
    run()