from analizator import Analizator


def main():
    analizator = Analizator("tabela_coduri.txt")
    analizator.run_compilation("program.txt")
    print("--------------------")
    analizator.run_compilation("program_cu_erori.txt")

if __name__ == '__main__':
    main()
