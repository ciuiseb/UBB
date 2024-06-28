"""
    1. citire lista
    2. lungimea secventei maxima de numere crescatoare
    3. l.s.m de numere cu proprietatea ca 
    4. exit
"""

def print_menu(l):
    if len(l) != 0 :
         print("Lista curenta este:", l)

    print("""Alegeti o optiune:
          1. Cititi o lista
          2. Afisati secventa maxima de numere crescatoare din lista
          3. Afisati secventa maxima de numere cu proprietatea ca x[j+1]-x[j] are semn contrar fata de x[j]-x[j-1]
          4. exit
          """)
def read_list():
    user_input = input("Intorduceti o lista")
    return [int(x) for x in user_input.split()]
def secv_1(l):
    ok = 0          # siguranta in caz ca nu exista cazuri favorabile
    l_max = 0
    l_curent = 0
    for i in range(1,len(l)):
        if l[i] > l [i - 1]:
            l_curent += 1
            ok = 1
        else:
            if l_curent > l_max:
                l_max = l_curent
            l_curent = 0
    if l_curent > l_max:
                l_max = l_curent
    return l_max + ok
def test_secv_1():
    assert secv_1([1, 2, 3, 4, 5]) == 5
    assert secv_1([1, 2, 3, 2 ,7, 1, 3, 5, 9]) == 4
    assert secv_1([-1, 0 , -2]) == 2
    assert secv_1([3, 2, 1]) == 0
def sign(a,b):
    if(a >= b):
        return 0
    else:
        return 1
def secv_2(l):
    """p = 1 -       p = 0 """
    l_max = 0
    l_curent = 0
    p = sign(l[1], l[0])
    q = -1
    ok = 0 # siguranta in caz ca nu exista cazuri favorabile
    for i in range(1, len(l)):
        q = sign( l[i], l[i - 1])
        if p != q:
            l_curent += 1
            ok = 1
        else:
            if l_curent > l_max:
                l_max = l_curent
            l_curent = 0
        
        p = q
    if l_curent > l_max:
                l_max = l_curent
    return l_max + ok
def test_secv_2():
    assert secv_2([1, 2, 3, 4]) == 0
    assert secv_2([1, 2, 0, 3, 4, 2, 1]) == 3
    assert secv_2([1, 0, 2, -1, 5, 3, 2, 4, 1, 7, 2, 8]) == 6
    assert secv_2([1, 1, 2, 2, 3]) == 0
    """
1, 2, 0, 3, 4, 2, 1
+ - + + - - -> 3

1, 0, 2, -1, 5, 3, 2, 4, 1, 7, 2, 8
- + - + - - + - + - + -> 6

1, 1, 2, 2, 3
+ + + + -> 0"""
def main():
    ok = 1
    l = []
    while ok:
        print_menu(l)
        i = int(input())
        if i == 1:
            l.clear()
            l = read_list()
        elif i == 2:
            if len(l) == 0:
                print("Trebuie citita o lista")
            else:
                print(secv_1(l))
        elif i == 3:
            if len(l) == 0:
                print("Trebuie citita o lista")
            else:
                print(secv_2(l))
        elif i == 4: 
            print("Proces incheiat")
            ok = 0
        else:
            print("Optiune invalida")


test_secv_1()
test_secv_2()


main()