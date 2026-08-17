import re
import sys
from typing import List, Dict

from binary_search_tree import BinarySearchTree
from automat_finit import AutomatFinit


class AnalizatorLexical:
    def __init__(self, coding_table_path: str):
        self.tabela_coduri: Dict[str, int] = {}
        self.ids: BinarySearchTree = BinarySearchTree()
        self.consts: BinarySearchTree = BinarySearchTree()
        self.fip: List[str] = []
        self.source_program: List[str] = []

        self.automat_nr_intregi = AutomatFinit.from_file("af_nr_intregi.txt")
        self.automat_nr_reale = AutomatFinit.from_file("af_nr_reale.txt")
        self.automat_ids = AutomatFinit.from_file("af_ids.txt")

        self.tokenizer_pattern = re.compile(r'(?=;)|[ \t\n\r]+')

        self._read_symbols_table(coding_table_path)

    def get_fip_and_ts(self, source_file: str):
        try:
            self._clear_tables()
            self._read_source_file(source_file)
            self._identifica_ids()
            self._identifica_consts()
            self._generate_fip()

            self._print_results()

        except (RuntimeError, IOError, FileNotFoundError) as e:
            print(f"Eroare fatala: {e}", file=sys.stderr)

    def _clear_tables(self):
        self.ids = BinarySearchTree()
        self.consts = BinarySearchTree()
        self.fip.clear()
        self.source_program.clear()

    def _read_source_file(self, source_file: str):
        try:
            with open(source_file, 'r', encoding='utf-8') as f:
                self.source_program = f.read().splitlines()
        except (IOError, FileNotFoundError) as e:
            raise RuntimeError(f"Fisierul nu a fost deschis/gasit: {source_file}") from e

    def _identifica_ids(self):
        types = {"Numerus", "Ratio", "Struct"}

        for line in self.source_program:
            trimmed_line = line.strip()
            if not trimmed_line:
                continue

            simboluri = [s for s in self.tokenizer_pattern.split(trimmed_line) if s]
            if not simboluri:
                continue

            if simboluri[0] in types:
                try:
                    ids_in_line = self._process_ids_line(simboluri)
                    for an_id in ids_in_line:
                        self.ids.insert(an_id)
                except RuntimeError as e:
                    raise RuntimeError(f"Eroare in declaratie: '{line}'. Detaliu: {e}")

    def _identifica_consts(self):
        types = {"Numerus", "Ratio", "Struct"}
        for line in self.source_program:
            trimmed_line = line.strip()
            if not trimmed_line:
                continue

            test_simboluri = [s for s in self.tokenizer_pattern.split(trimmed_line) if s]
            if not test_simboluri:
                continue
            if test_simboluri[0] in types:
                continue

            simboluri = self._tokenize_line(trimmed_line)
            for s in simboluri:
                if self.automat_nr_intregi.check_string(s) or self.automat_nr_reale.check_string(s):
                    self.consts.insert(s)

    def _generate_fip(self):
        types = {"Numerus", "Ratio", "Struct"}
        in_program_body = False

        for line_number, line in enumerate(self.source_program, 1):
            trimmed_line = line.strip()
            if not trimmed_line:
                continue

            test_simboluri = [s for s in self.tokenizer_pattern.split(trimmed_line) if s]
            if not test_simboluri:
                continue

            if not in_program_body:
                if test_simboluri[0] in types:
                    continue
                else:
                    in_program_body = True

            simboluri = self._tokenize_line(trimmed_line)

            for s in simboluri:
                if not s:
                    continue
                try:
                    pif_entry = self._map_simbol(s)
                    self.fip.append(pif_entry)
                except RuntimeError as e:
                    error_msg = f"Eroare Lexicala la linia {line_number}: {e}"
                    raise RuntimeError(error_msg) from e

    def _process_ids_line(self, line_tokens: List[str]) -> List[str]:
        result = []
        i = 1

        while i < len(line_tokens):
            simbol = line_tokens[i]
            if simbol == ";":
                if i == 1:
                    raise RuntimeError("Declaratie goala.")
                break

            if self.automat_ids.check_string(simbol):
                if self.ids.contains(simbol) or simbol in result:
                    raise RuntimeError(f"Redeclararea ID: {simbol}")
                result.append(simbol)
            else:
                raise RuntimeError(f"ID ilegal: {simbol}")

            i += 1
            if i >= len(line_tokens):
                break

            if line_tokens[i] == ";":
                break

            if line_tokens[i] != "et":
                raise RuntimeError("Lipseste 'et' in declaratie")

            i += 1

        return result

    def _read_symbols_table(self, coding_table_path: str):
        try:
            with open(coding_table_path, 'r', encoding='utf-8') as f:
                for line in f:
                    line = line.strip()
                    if not line:
                        continue
                    parts = line.split(',')
                    if len(parts) == 2:
                        token = parts[0].strip()
                        cod = int(parts[1].strip())
                        self.tabela_coduri[token] = cod
        except (IOError, FileNotFoundError) as e:
            raise RuntimeError(f"Fisierul de coduri nu a fost deschis/gasit: {coding_table_path}") from e
        except ValueError as e:
            raise RuntimeError(f"Format invalid in fisierul de coduri: {line}") from e

    def _print_results(self):
        print("--- Forma Interna a Programului (FIP) ---")
        for entry in self.fip:
            print(entry)

        print("\n--- Tabela Simboluri (Identificatori) ---")
        self.ids.print_tree()

        print("\n--- Tabela Simboluri (Constante) ---")
        self.consts.print_tree()


    def _tokenize_line(self, line: str) -> List[str]:
        line = line.strip()
        return [s for s in self.tokenizer_pattern.split(line) if s]

    def _map_simbol(self, simbol: str) -> str:
        if self.consts.contains(simbol):
            return f"(const, {self.consts.get(simbol)})"

        if self.ids.contains(simbol):
            return f"(ID, {self.ids.get(simbol)})"

        cod = self.tabela_coduri.get(simbol)
        if cod is not None:
            return str(cod)

        raise RuntimeError(f"Simbol invalid: {simbol}")