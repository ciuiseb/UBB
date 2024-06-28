def creareNumar(lista):
    x=0
    for i in range(9,-1,-1):
        while lista[i]:
            x*=10
            x+=i
            lista[i]-=1;
    print(x)

def creareLista(n, lista):
    while n != 0:
        lista[int(n%10)] += 1
        n = int(n / 10)

n = int(input())
lista = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

creareLista(n,lista)
creareNumar(lista)
