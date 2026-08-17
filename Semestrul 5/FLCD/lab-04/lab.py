import sys

from automat_finit import AutomatFinit


def menu():
    print("0. Exit ")
    print("1. Afiseaza stari")
    print("2. Afiseaza alfabet")
    print("3. Afiseaza tranzitiile")
    print("4. Afiseaza starile finale")
    print("5. Verifica secventa")
    print("6. Determina prefixul maxim acceptat pentru secventa")


def get_manual_automat():
    def parse_list_input(prompt):
        s = input(prompt)
        return set(item.strip() for item in s.split(',') if item.strip())

    import re
    state_pattern = re.compile(r"^[A-Z]+[0-9]*$")

    q = set()
    while not q:
        q_input = parse_list_input("Introdu starile: ")
        valid_states = True
        for state in q_input:
            if not state_pattern.match(state):
                print(f"  Eroare: Statul '{state}' nu respecta formatul <litere_mari><cifre>", file=sys.stderr)
                valid_states = False
                break
        if valid_states:
            q = q_input
        else:
            print("Introdu din nou starile.")

    if not q:
        print("Eroare: Este necesara cel putin o stare.", file=sys.stderr)
        return None

    symbol_pattern = re.compile(r"^[a-z]$")
    sigma = set()
    while not sigma:
        sigma_input = parse_list_input("Introdu alfabetul: ")
        valid_symbols = True
        for sym in sigma_input:
            if not symbol_pattern.match(sym):
                print(f"  Eroare: Simbolul '{sym}' nu este o litera mica", file=sys.stderr)
                valid_symbols = False
                break
        if valid_symbols:
            sigma = sigma_input
        else:
            print("Introdu din nou alfabetul.")

    if not sigma:
        print("Eroare: Este necesar cel putin un simbol.", file=sys.stderr)
        return None

    q0 = ""
    while q0 not in q:
        q0 = input(f"Introdu starea initiala: ").strip()
        if q0 not in q:
            print(f"    Eroare: '{q0}' nu este in setul de stari Q. Incearca din nou.")

    f = set()
    while True:
        f_input = parse_list_input(f"Introdu starile finale: ")
        if not f_input:
            f = f_input
            break
        if f_input.issubset(q):
            f = f_input
            break
        else:
            print(f"    Eroare: Starile {f_input - q} nu sunt in setul de stari Q. Incearca din nou.")

    print(f"Vei defini o tranzitie pentru fiecare pereche (stare, simbol).")
    delta = {}
    for state in sorted(list(q)):
        for symbol in sorted(list(sigma)):
            while True:
                next_state = input(f"  δ({state}, {symbol}) = ").strip()
                if next_state in q:
                    delta[(state, symbol)] = next_state
                    break
                else:
                    print(f"    Eroare: Statul '{next_state}' nu este in Q {q}. Te rog introdu un stat valid.")

    try:
        return AutomatFinit(sigma, q, delta, q0, f)
    except Exception as e:
        print(f"Eroare: {e}", file=sys.stderr)
        return None


def main():
    choice = input("Vrei sa introduci manual elementele automatului? (da/nu): ").strip().lower()

    if choice == 'da':
        my_automaton = get_manual_automat()

    else:
        print("OK. Loading from file.")
        # my_automaton = AutomatFinit.from_file("automat_finit.txt")
        my_automaton = AutomatFinit.from_file("automat_finit_in_class.txt")

    while True:
        menu()
        optiune = input("Alege o optiune: ").strip()

        if optiune == '0':
            print("Stop")
            break

        elif optiune == '1':
            print(f"Q: {my_automaton.q}")

        elif optiune == '2':
            print(f"Σ: {my_automaton.sigma}")

        elif optiune == '3':
            print("Tranzitii:")
            if not my_automaton.delta:
                print("  (Nicio tranzitie nu exista)")
            for (stare, simbol), stare_urm in my_automaton.delta.items():
                print(f"  δ({stare}, {simbol}) = {stare_urm}")

        elif optiune == '4':
            print(f"F: {my_automaton.f}")

        elif optiune == '5':
            secventa = input("Introdu secventa: ").strip()
            print(my_automaton.check_string(secventa))
        elif optiune == '6':
            secventa = input("Introdu secventa: ").strip()
            print(my_automaton.get_max_prefix(secventa))
        else:
            print("Optiune invalida.")
        print("\n")


if __name__ == "__main__":
    main()
