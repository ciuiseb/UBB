import math
class Complex:
    def __init__(self, real, imag):
        self._real = real
        self._imag = imag
    def __add__(self, other):
        self._real += other._real
        self._imag += other._imag
        return self
    def __mul__(self, other):
        self._real *= other._real
        self._imag *= other._imag
        return self
    def __eq__(self, other):
        return self._real == other._real and self._imag == other._imag
    def __ne__(self, other):
        return not (self._real == other._real and self._imag == other._imag)
    def get_real(self):
        return self._real
    def _set_real(self, val):
        self._real = val
    def _del_real(self):
        del self._real

    real = property(
        fget = get_real,
        fset = _set_real,
        fdel = _del_real,
        doc = ""
    )
    def get_imag(self):
        return self._imag
    def set_imag(self, val):
        self._imag = val
    imag = property(
        fget= get_imag,
        fset = set_imag
    )
    def modul(self):
        return math.sqrt(self._real * self._real + self._imag * self._imag)

    def print_complex(self):
        print("[", self._real, " + ", self._imag, "i ]")
