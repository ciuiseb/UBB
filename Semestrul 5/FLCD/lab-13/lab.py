import sys

class LL1Parser:
    def __init__(self, file_path):
        self.grammar = {}
        self.start_symbol = None
        self.terminals = set()
        self.non_terminals = set()
        self.first = {}
        self.follow = {}
        self.parsing_table = {}
        self.load_grammar(file_path)

    def load_grammar(self, file_path):
        try:
            with open(file_path, 'r') as f:
                lines = f.readlines()
        except FileNotFoundError:
            print(f"Eroare: Fișierul {file_path} nu a fost găsit.")
            sys.exit(1)

        for line in lines:
            line = line.strip()
            if not line: continue

            if '->' not in line:
                continue

            head, body = [x.strip() for x in line.split('->', 1)]

            if self.start_symbol is None:
                self.start_symbol = head

            self.non_terminals.add(head)

            rule = body.split()

            if head not in self.grammar:
                self.grammar[head] = []

            self.grammar[head].append(rule)

            for symbol in rule:
                if not symbol.isupper() and symbol != 'epsilon':
                    self.terminals.add(symbol)

        for nt in self.non_terminals:
            self.first[nt] = set()
            self.follow[nt] = set()

    def compute_first(self):
        changed = True
        while changed:
            changed = False
            for head, productions in self.grammar.items():
                for body in productions:
                    body_first = self.get_first_of_sequence(body)
                    original_size = len(self.first[head])
                    self.first[head].update(body_first)
                    if len(self.first[head]) > original_size:
                        changed = True

    def get_first_of_sequence(self, sequence):
        result = set()
        if not sequence: return result

        if sequence[0] == 'epsilon':
            result.add('epsilon')
            return result

        if sequence[0] in self.terminals:
            result.add(sequence[0])
            return result

        if sequence[0] in self.non_terminals:
            result.update(self.first[sequence[0]] - {'epsilon'})
            if 'epsilon' in self.first[sequence[0]]:
                if len(sequence) > 1:
                    result.update(self.get_first_of_sequence(sequence[1:]))
                else:
                    result.add('epsilon')
        return result

    def compute_follow(self):
        self.follow[self.start_symbol].add('$')
        changed = True
        while changed:
            changed = False
            for head, productions in self.grammar.items():
                for body in productions:
                    for i, symbol in enumerate(body):
                        if symbol in self.non_terminals:
                            rest_of_body = body[i+1:]
                            trailer_first = self.get_first_of_sequence(rest_of_body)

                            original_size = len(self.follow[symbol])
                            self.follow[symbol].update(trailer_first - {'epsilon'})

                            if not rest_of_body or 'epsilon' in trailer_first:
                                self.follow[symbol].update(self.follow[head])

                            if len(self.follow[symbol]) > original_size:
                                changed = True

    def build_parsing_table(self):
        is_ll1 = True
        all_columns = list(self.terminals) + ['$']

        for nt in self.non_terminals:
            self.parsing_table[nt] = {t: None for t in all_columns}

        for head, productions in self.grammar.items():
            for body in productions:
                first_set = self.get_first_of_sequence(body)

                for term in first_set:
                    if term != 'epsilon':
                        if self.parsing_table[head][term] is not None:
                            is_ll1 = False
                        self.parsing_table[head][term] = body

                if 'epsilon' in first_set:
                    for term in self.follow[head]:
                        if self.parsing_table[head][term] is not None:
                            if self.parsing_table[head][term] != ['epsilon'] and body != ['epsilon']:
                                is_ll1 = False
                        self.parsing_table[head][term] = body
        return is_ll1

    def parse(self, input_string):
        if ' ' in input_string:
            input_tokens = input_string.split()
        else:
            input_tokens = []
            i = 0
            while i < len(input_string):
                if input_string[i].strip() == '':
                    i += 1
                    continue
                input_tokens.append(input_string[i])
                i += 1

        input_tokens.append('$')
        stack = ['$', self.start_symbol]
        result_productions = []

        print(f"\n{'Input':<20} | {'Ramas':<20} | {'Productie'}")
        print("-" * 65)

        ptr = 0
        while len(stack) > 0:
            top = stack[-1]
            current_input = input_tokens[ptr]

            stack_str = "".join(stack)
            input_str = "".join(input_tokens[ptr:])

            if top == current_input:
                print(f"{stack_str:<20} | {input_str:<20} | Se reduce '{top}'")
                stack.pop()
                ptr += 1
                if top == '$':
                    return True, result_productions

            elif top in self.terminals:
                return False, f"Eroare: Se aștepta '{top}', s-a găsit '{current_input}'"

            elif top == 'epsilon':
                stack.pop()

            elif top in self.non_terminals:
                rule = self.parsing_table[top].get(current_input)
                if rule is None:
                    return False, f"Eroare: Nicio regulă pentru [{top}, {current_input}]"

                print(f"{stack_str:<20} | {input_str:<20} | {top} -> {' '.join(rule)}")
                result_productions.append(f"{top} -> {' '.join(rule)}")
                stack.pop()

                if rule != ['epsilon']:
                    for symbol in reversed(rule):
                        stack.append(symbol)
            else:
                return False, f"Simbol necunoscut în stivă: {top}"

        return False, "Stiva s-a golit prematur."

if __name__ == "__main__":
    parser = LL1Parser("gramatica.txt")

    parser.compute_first()
    parser.compute_follow()

    is_valid = parser.build_parsing_table()

    if is_valid:
        print("\nGramatica este LL(1).")
        seq = input("\nIntroduceți secvența (ex: a + a * a): ")
        success, res = parser.parse(seq)

        if success:
            print("\nAccepta\nSirul productiilor:")
            for r in res: print("  " + r)
        else:
            print("\nRespins:", res)
    else:
        print("\nGramatica nu este LL(1)")