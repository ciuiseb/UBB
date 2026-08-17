from analizator import AnalizatorLexical

def main():
    analizator = AnalizatorLexical("tabela_coduri.txt")
    analizator.get_fip_and_ts("program.txt")

if __name__ == '__main__':
    main()