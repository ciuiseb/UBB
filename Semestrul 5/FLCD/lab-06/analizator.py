import sys
import subprocess
from typing import List, Dict

from binary_search_tree import BinarySearchTree


class AnalizatorLexical:
    def __init__(self, coding_table_path: str):
        self.tabela_coduri: Dict[str, int] = {}
        self.ids: BinarySearchTree = BinarySearchTree()
        self.consts: BinarySearchTree = BinarySearchTree()
        self.fip: List[str] = []

        self._read_symbols_table(coding_table_path)

    def _run_flex_scanner(self, source_file: str) -> List[tuple[str, str]]:
        token_list = []
        try:
            process = subprocess.run(
                ['wsl', './scanner', source_file],
                capture_output=True,
                text=True,
                check=True,
                encoding='utf-8'
            )
            if process.stderr:
                print(f"Eroare Lexicala (from scanner):\n{process.stderr.strip()}", file=sys.stderr)
            output = process.stdout.strip()

            for line in output.splitlines():
                if not line:
                    continue
                parts = line.split(',', 1)
                if len(parts) == 2:
                    token_list.append((parts[0], parts[1]))
            return token_list
        except (FileNotFoundError, subprocess.CalledProcessError) as e:
            print(f"Eroare {e}")

    def get_fip_and_ts(self, source_file: str):
        try:
            self._clear_tables()
            all_tokens = self._run_flex_scanner(source_file)

            for token_name, token_value in all_tokens:
                if token_name == "ID":
                    self.ids.insert(token_value)
                    pos = self.ids.get(token_value)
                    self.fip.append(f"(ID, {pos})")
                elif token_name == "INTEGER" or token_name == "REAL":
                    self.consts.insert(token_value)
                    pos = self.consts.get(token_value)
                    self.fip.append(f"(const, {pos})")
                else:
                    cod = self.tabela_coduri.get(token_value)
                    self.fip.append(str(cod))
            self._print_results()

        except (RuntimeError, IOError, FileNotFoundError) as e:
            print(f"Eroare {e}")

    def _clear_tables(self):
        self.ids = BinarySearchTree()
        self.consts = BinarySearchTree()
        self.fip.clear()


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