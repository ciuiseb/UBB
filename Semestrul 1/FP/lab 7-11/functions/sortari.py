def insertion_sort(lista, key=lambda x:x, reverse=False, cmp=lambda x,y: x < y ):
    def cmp2(a,b):
        return (cmp(a,b) if not reverse else cmp(b,a))
    for i in range(1, len(lista)):
        elem = lista[i]
        j = i - 1
        while j >= 0 and cmp2(key(elem), key(lista[j])):
            lista[j + 1] = lista[j]
            j -= 1
        lista[j + 1] = elem
    return lista


def get_next_gap(gap):
    gap = int((gap * 10) / 13)
    if gap < 1:
        return 1
    return gap


def comb_sort(lista, key=None, reverse=False):
    ln = len(lista)
    gap = ln
    swapped = 1

    while gap != 1 or swapped == 1:

        gap = get_next_gap(gap)
        swapped = False

        for i in range(0, ln - gap):
            if (key(lista[i]) if key else lista[i]) * (-1 if reverse else 1) > (
                    key(lista[i + gap]) if key else lista[i + gap]) * (-1 if reverse else 1):
                lista[i], lista[i + gap] = lista[i + gap], lista[i]
                swapped = True
    return lista


def test_insertion_sort():
    lista = [[1, 2], [3, 1], [2, 5], [0, 1]]
    lista_sortata = insertion_sort(lista, key=lambda x: x[0])
    assert lista_sortata[0] == [0, 1]

    lista_sortata = insertion_sort(lista, key=lambda x: x[0], reverse=True)
    assert lista_sortata[0] == [3, 1]

    lista_sortata = insertion_sort(lista, cmp=lambda x, y: x[0] < y[0])
    assert lista_sortata[0] == [0, 1]


def test_comb_sort():
    lista = [[1, 2], [3, 1], [2, 5], [0, 1]]
    lista_sortata = comb_sort(lista, key=lambda x: x[0])
    assert lista_sortata[0] == [0, 1]

    lista_sortata = comb_sort(lista, key=lambda x: x[0], reverse=True)
    assert lista_sortata[0] == [3, 1]


test_insertion_sort()
test_comb_sort()
