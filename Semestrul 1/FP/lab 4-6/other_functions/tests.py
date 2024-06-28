from classes.complexNr import Complex
import other_functions.functions
def test_prim():
    assert other_functions.functions.prim(-1) == False
    assert other_functions.functions.prim(2) == True
    assert other_functions.functions.prim(4) == False
    assert other_functions.functions.prim(25) == False
    assert other_functions.functions.prim(29) == True
def test_lists_compare():
    assert other_functions.functions.lists_compare([Complex(1,2), Complex(1, 10), Complex(5,6)] , [Complex(1,2), Complex(1, 10), Complex(5,6)]) == True
    assert other_functions.functions.lists_compare([Complex(1,2), Complex(1, 10), Complex(5,6), Complex(1,1)], [Complex(1,2), Complex(1, 10), Complex(5,6)]) == False
