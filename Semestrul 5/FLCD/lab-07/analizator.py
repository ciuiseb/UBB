import subprocess
import sys
from typing import List, Dict

from binary_search_tree import BinarySearchTree


class Analizator:
    def __init__(self, coding_table_path: str):
        self.tabela_coduri: Dict[str, int] = {}
        self.ids: BinarySearchTree = BinarySearchTree()
        self.consts: BinarySearchTree = BinarySearchTree()
        self.fip: List[str] = []
        self._read_symbols_table(coding_table_path)

    def run_compilation(self, source_file: str, parser_path: str = "./parser"):
        try:
            process = subprocess.run(
                ['wsl', parser_path, source_file],
                capture_output=True,
                text=True,
                encoding='utf-8'
            )
            output_lines = process.stdout.strip().splitlines()

            for line in output_lines:
                if not line or line.startswith(">>"): continue
                parts = line.split(',', 1)
                if len(parts) == 2:
                    self._process_token(parts[0].strip(), parts[1].strip())

            if process.returncode != 0:
                if process.stderr:
                    print(f"{process.stderr.strip()}", file=sys.stderr)
            else:
                self._print_results()

        except FileNotFoundError:
            print(f"Error: Executable '{parser_path}' not found.")
        except Exception as e:
            print(f"System Error: {e}")

    def _process_token(self, token_type, token_value):
        if token_type == "ID":
            self.ids.insert(token_value)
            pos = self.ids.get(token_value)
            self.fip.append(f"(ID, {pos})")
        elif token_type == "INTEGER" or token_type == "REAL":
            self.consts.insert(token_value)
            pos = self.consts.get(token_value)
            self.fip.append(f"(const, {pos})")
        else:
            cod = self.tabela_coduri.get(token_type)
            if cod is not None:
                self.fip.append(str(cod))

    def _read_symbols_table(self, coding_table_path: str):
        try:
            with open(coding_table_path, 'r', encoding='utf-8') as f:
                for line in f:
                    parts = line.strip().split(',')
                    if len(parts) == 2:
                        self.tabela_coduri[parts[0].strip()] = int(parts[1].strip())
        except Exception as e:
            print(f"Error loading codes: {e}")

    def _clear_tables(self):
        self.ids = BinarySearchTree()
        self.consts = BinarySearchTree()
        self.fip.clear()

    def _print_results(self):
        print("\n--- FIP ---")
        for entry in self.fip:
            print(entry)
        print("\n--- Symbol Table (IDs) ---")
        self.ids.print_tree()
        print("\n--- Symbol Table (Consts) ---")
        self.consts.print_tree()
