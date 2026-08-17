import re
import sys

class AutomatFinit:
    def __init__(self, sigma, q, delta, q0, f):
        self.sigma = sigma
        self.q = q
        self.delta = delta
        self.q0 = q0
        self.f = f

        if self.q0 not in self.q:
            print(f"Error: Start state '{self.q0}' is not in the set of states Q.", file=sys.stderr)

        for final_state in self.f:
            if final_state not in self.q:
                print(f"Error: Final state '{final_state}' is not in the set of states Q.", file=sys.stderr)

    def check_string(self, string):
        current_state = self.q0
        for char in string:
            if char not in self.sigma:
                return False
            transition_key = (current_state, char)
            if transition_key in self.delta:
                current_state = self.delta[transition_key]
            else:
                return False
        return current_state in self.f

    def get_max_prefix(self, string):
        result = None
        current_state = self.q0
        current_string = ""

        for char in string:
            transition_key = (current_state, char)

            if char not in self.sigma or transition_key not in self.delta:
                break

            current_state = self.delta[transition_key]
            current_string += char

            if current_state in self.f:
                result = current_string

        return result

    @classmethod
    def from_file(cls, filename):
        try:
            with open(filename, 'r', encoding='utf-8') as file:
                content = file.read()
        except FileNotFoundError:
            print(f"Error: File '{filename}' not found.", file=sys.stderr)
            return None
        except Exception as e:
            print(f"Error reading file '{filename}': {e}", file=sys.stderr)
            return None

        state_regex = r"([A-Z]+[0-9]+)"
        state_list_regex = r"([A-Z]+[0-9]+(?:\s*,\s*[A-Z]+[0-9]+)*)"

        symbol_char_class = r"[a-zA-Z0-9\.+\-]"
        symbol_list_regex = r"({0}(?:\s*,\s*{0})*)".format(symbol_char_class)


        file_pattern_str = (
            r"^\s*Q:\s*(?P<states>{})\s*;\s*"
            r"Σ\s*:\s*(?P<alphabet>{})\s*;\s*"
            r"q0\s*:\s*(?P<start>{})\s*;\s*"
            r"F\s*:\s*(?P<finals>{})\s*;\s*"
            r"Tranzitii\s*:\s*(?P<transitions_block>.*?)\s*;\s*$"
        ).format(state_list_regex, symbol_list_regex, state_regex, state_list_regex)

        file_pattern = re.compile(file_pattern_str, re.DOTALL)

        match = file_pattern.match(content)

        if not match:
            print(f"Error: File '{filename}' does not match the expected format.", file=sys.stderr)
            print("Expected format:")
            print("Q: Q0,Q1,...;")
            print("Σ: a,b,...;")
            print("q0: Q0;")
            print("F: Q1,Q2,...;")
            print("Tranzitii:")
            print("Q0: (a,Q1),(b,Q0);")
            print("Q1: (a,Q2);")
            print("...;")
            return None

        try:
            def parse_list(s):
                return set(item.strip() for item in s.split(','))

            q = parse_list(match.group('states'))
            sigma = parse_list(match.group('alphabet'))
            q0 = match.group('start').strip()
            f = parse_list(match.group('finals'))
            delta = {}
            transitions_block = match.group('transitions_block')

            state_transition_pattern = re.compile(
                r"([A-Z]+[0-9]+)\s*:\s*(.*?);", re.DOTALL
            )

            pair_pattern = re.compile(r"\(\s*({0})\s*,\s*([A-Z]+[0-9]+)\s*\)".format(symbol_char_class))


            for state_match in state_transition_pattern.finditer(transitions_block):
                current_state = state_match.group(1)
                pairs_str = state_match.group(2)

                if current_state not in q:
                    print(f"Warning: Transition defined for state '{current_state}' which is not in Q.", file=sys.stderr)

                for pair_match in pair_pattern.finditer(pairs_str):
                    symbol = pair_match.group(1)
                    next_state = pair_match.group(2)

                    if symbol not in sigma:
                        print(f"Warning: Transition symbol '{symbol}' from '{current_state}' not in Sigma.", file=sys.stderr)
                    if next_state not in q:
                        print(f"Warning: Next state '{next_state}' from '{current_state}' not in Q.", file=sys.stderr)

                    transition_key = (current_state, symbol)

                    if transition_key in delta:
                        print(f"Error: Non-deterministic transition for {transition_key}. Aborting.", file=sys.stderr)
                        return None

                    delta[transition_key] = next_state

            return cls(sigma, q, delta, q0, f)

        except Exception as e:
            print(f"Error parsing components: {e}", file=sys.stderr)
            return None