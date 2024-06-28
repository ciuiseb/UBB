from classes.complexNr import Complex
import main_functions.functions
import other_functions.functions


# test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6)]
def test_add_stack():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6)]
    z = Complex(11, 2)

    main_functions.functions.add_stack(test_list, z, [])
    # list_print(test_list)
    assert other_functions.functions.lists_compare(test_list,
                                                   [Complex(1, 2), Complex(1, 10), Complex(5, 6), Complex(11, 2)])


def test_add_on_position():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6)]
    assert other_functions.functions.lists_compare(
        main_functions.functions.add_on_position( test_list, Complex(1, 3), 2, []),
        [Complex(1, 2), Complex(1, 10), Complex(1, 3), Complex(5, 6)]) == True


def test_del_pos():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6)]
    assert other_functions.functions.lists_compare(main_functions.functions.del_pos(test_list, 1, []),
                                                   [Complex(1, 2), Complex(5, 6)]) == True


def test_del_interval():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6), Complex(1, 2), Complex(1, 10), Complex(5, 6)]
    assert other_functions.functions.lists_compare(main_functions.functions.del_interval(test_list, 0, 2, []),
                                                   [Complex(1, 2), Complex(1, 10), Complex(5, 6)]) == True


def test_print_imag_in_interval():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6)]
    assert other_functions.functions.lists_compare(main_functions.functions.print_imag_in_interval(test_list, 0, 2),
                                                   [2, 10, 6]) == True
    assert other_functions.functions.lists_compare(main_functions.functions.print_imag_in_interval(test_list, 0, 2),
                                                   [2, 10, 7]) == False


def test_print_nr_mod_lower_10():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6)]
    assert other_functions.functions.lists_compare(main_functions.functions.print_nr_mod_lower_10(test_list),
                                                   [Complex(1, 2), Complex(5, 6)]) == True
    assert other_functions.functions.lists_compare(main_functions.functions.print_nr_mod_lower_10(test_list),
                                                   [Complex(1, 2), Complex(1, 10), Complex(5, 6)]) == False


def test_nr_replace():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6)]
    assert other_functions.functions.lists_compare(
        main_functions.functions.nr_replace(test_list, Complex(1, 2), Complex(1, 3), []),
        [Complex(1, 3), Complex(1, 10), Complex(5, 6)]) == True


def test_print_nr_mod_equal_10():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6), Complex(0, 10), Complex(6, 8)]
    assert (other_functions.functions.lists_compare(
        main_functions.functions.print_nr_mod_equal_10(test_list),
    [Complex(0, 10), Complex(6, 8)])
        == True)
    assert other_functions.functions.lists_compare(main_functions.functions.print_nr_mod_equal_10(test_list),
                                                   [Complex(1, 9), Complex(0, 10), Complex(6, 8)]) == False


def test_subseq_sum():
    assert main_functions.functions.subseq_sum([Complex(1, 2), Complex(1, 10), Complex(5, 6)], 0, 2) == Complex(7, 18)
    assert main_functions.functions.subseq_sum([Complex(1, 2), Complex(1, 10), Complex(5, 6)], 0, 2) != Complex(99, 18)


def test_subseq_prod():
    assert main_functions.functions.subseq_prod([Complex(1, 2), Complex(1, 10), Complex(5, 6)], 0, 2) == Complex(5, 120)
    assert main_functions.functions.subseq_prod([Complex(1, 2), Complex(1, 10), Complex(5, 6)], 0, 2) != Complex(6, 120)


def test_sort_by_imag():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6)]
    assert other_functions.functions.lists_compare(main_functions.functions.sort_by_imag(test_list),
                                                   [Complex(1, 10), Complex(5, 6), Complex(1, 2)]) == True
    assert other_functions.functions.lists_compare(main_functions.functions.sort_by_imag(test_list),
                                                   [Complex(5, 6), Complex(1, 10), Complex(1, 2)]) == False


def test_elim_prime_real():
    test_list = [Complex(1, 2), Complex(1, 10), Complex(5, 6), Complex(2, 100), Complex(12, 3)]
    assert other_functions.functions.lists_compare(main_functions.functions.elim_prime_real(test_list, []),
                                                   [Complex(1, 2), Complex(1, 10), Complex(12, 3)]) == True


def test_mod_filter():
    test_list = [Complex(1, 2), Complex(0, 10), Complex(5, 6)]
    assert other_functions.functions.lists_compare(main_functions.functions.mod_filter(test_list, '=', 10, []),
                                                   [Complex(1, 2), Complex(5, 6)]) == True
