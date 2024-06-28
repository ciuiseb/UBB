from classes.complexNr import Complex
import other_functions.functions


def print_menu():
    print("""
    Alegeti o optiune:
    1.  Adauga nr complex la finalul listei
    2.  Adauga nr complex pe o pozitie data
    3.  Sterge nr complex pe o pozitie data
    4.  Sterge toate nr complexe din intervalul [] dat
    5.  Inlocuieste toate apartiile unui numar cu unul nou
    6.  Tipareste partea imaginara pentru numerele din interval
    7.  Tipareste nr complexe cu modul < 10
    8.  Tipareste nr complexe cu modul = 10
    9.  Afiseaza suma nr dintr-o subsecventa data
    10. Afiseaza produsul nr dintr-o subsecventa data
    11. Printeaza lista sortata descrescator in functie de partea imaginara
    12. Elimina din lista numerele cu partea reala numar prim
    13. Elimina din lista numerele complexe la care modulul este <,= sau > decât un număr dat 
    14. Anuleaza ultima operatie
    0. Exit
    """)


def add_stack(list, z, modificari):
    rez = list
    rez.append(z)

    modificari.append(["add_stack", len(list) - 1])
    return rez


def add_on_position(list, z, p, modificari):
    res = []
    modificari.append(["add_on_position", p])
    for i in range(0, p):
        res.append(list[i])
    res.append(z)

    for i in range(p, len(list)):
        res.append(list[i])


    return res


def del_pos(list, p, modificari):
    res = []
    modificari.append(["del_pos", list[p],p])
    for i in range(0, p):
        res.append((list[i]))

    for i in range(p + 1, len(list)):
        res.append(list[i])



    return res


def del_interval(list, inceput, final, modificari):
    res = []
    aux = ["del_interval", inceput, final]
    for i in range(0, inceput):
        res.append((list[i]))

    for i in range(final + 1, len(list)):
        res.append(list[i])

    for i in range(inceput, final + 1):
        aux.append(list[i])
    modificari.append(aux)
    return res


def nr_replace(list, z, r, modificari):
    res = []
    iteratie =["nr_replace", z]
    lista_indici = []
    for i in range(0, len(list)):
        if list[i] == z:
            res.append(r)
            lista_indici.append(i)
        else:
            res.append(list[i])

    iteratie.append(lista_indici)
    modificari.append(iteratie)
    return res
def undo_nr_replace(lista_initiala, z, lista_indici):
    for j in range(0, len(lista_indici)):
        lista_initiala[lista_indici[j]] = z
    return lista_initiala

def print_imag_in_interval(list, inceput, final):
    res = []
    for i in range(inceput, final + 1):
        res.append(list[i]._imag)

    return res


def print_nr_mod_lower_10(list):
    res = []
    for i in range(0, len(list)):
        if list[i].modul() < 10:
            res.append(list[i])

    return res


def print_nr_mod_equal_10(list):
    res = []

    for i in range(0, len(list)):
        if list[i].modul() == 10:
            res.append(list[i])
    return res


def subseq_sum(list, inceput, final):
    s = Complex(0, 0)

    for i in range(inceput, final + 1):  # !!
        s = s + list[i]
    return s


def subseq_prod(list, inceput, final):
    p = Complex(1, 1)

    for i in range(inceput, final + 1):
        p *= list[i]
    return p


def sort_by_imag(list):
    res = list
    for i in range(0, len(res)):
        for j in range(i, len(res)):
            if res[i]._imag < res[j]._imag:
                res[i], res[j] = res[j], res[i]
    return res


def elim_prime_real(list, modificari):
    res = []
    aux = ["elim_prime_real"]
    for i in range(0, len(list)):
        if other_functions.functions.prim(list[i].real) == False:
            res.append(list[i])
        else:
            aux.append(list[i])
            aux.append(i)
    modificari.append(aux)
    return res


def mod_filter(list, sign, z, modificari):
    res = []
    aux = ["mod_filter"]
    for i in range(0, len(list)):
        removed = 0
        if sign == '>':
            if list[i].modul() <= z:
                res.append(list[i])
            else:
                removed = 1
                aux.append(list[i])
                aux.append(i)
        elif sign == '<':
            if list[i].modul() >= z:
                res.append(list[i])
            else:
                removed = 1
                aux.append(list[i])
                aux.append(i)
        elif sign == '=':
            if list[i].modul() != z:
                res.append(list[i])
            else:
                removed = 1
                aux.append(list[i])
                aux.append(i)
        if removed == 1:
            pass
    modificari.append(aux)
    return res
def undo(list, modificari, i):
    rez = list

    if modificari[i][0] == "add_stack":
        rez = del_pos(rez, modificari[i][1], [])
    elif modificari[i][0] == "add_on_position":
        rez = del_pos(rez, modificari[i][1], [])
    elif modificari[i][0] == "del_pos":
        rez = add_on_position(rez, modificari[i][1], modificari[i][2], [])
    elif modificari[i][0] == "del_interval":
        l = 3
        for j in range (modificari[i][1], modificari[i][2] + 1):
            rez = add_on_position(rez, modificari[i][l], j, [])
            l += 1
    elif modificari[i][0] == "nr_replace":
        rez = undo_nr_replace(rez, modificari[i][1], modificari[i][2])

    elif modificari[i][0] == "elim_prime_real" or modificari[i][0] == "mod_filter":
        for j in range(1, len(modificari[i]), 2):
            rez = add_on_position(rez, modificari[i][j], modificari[i][j + 1], [])

    modificari.pop()

    return rez
def get_operation(iteratie):
    return iteratie[0]
def get_inceput(iteratie):
    if iteratie[0] == "del_interval":
        return iteratie[1]
def get_final(iteratie):
    if iteratie[0] == "del_interval":
        return iteratie[2]
def get_complex_nr(iteratie):
    if iteratie[0] == "del_pos":
        return iteratie[1]
def get_complex_nr_interval(iteratie, pos):
    if iteratie[0] == "del_interval":
        return iteratie[pos]

def get_number_command(comanda):
    nr = Complex(0,0)
    nr.real = int(comanda[1])
    nr.imag = int(comanda[3][0])
    if comanda[2] == '-':
        nr.imag *= -1
    return nr
def get_position_command(comanda):
    if comanda[0] == "adauga":
        if comanda[4] == "pe" and comanda[5] == "pozitia":
            return int(comanda[6])
    elif comanda[0] == "sterge":
        if comanda[1] == "pozitia":
            return int(comanda[2])
def get_inceput_command(comanda):
    if comanda[0] == "sterge" and comanda[1] =="intervalul":
        return int(comanda[5])
def get_final_command(comanda):
    if comanda[0] == "sterge" and comanda[1] =="intervalul":
        return int(comanda[7])