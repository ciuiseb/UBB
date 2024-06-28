class Customer:
    customer_count = 0

    def __init__(self, name, cnp):
        Customer.customer_count += 1
        self.__id = Customer.customer_count
        self.__name = name  # search by name then search by cnp
        self.__cnp = cnp

    @property
    def id(self):
        return self.__id

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, new_name):
        self.__name = new_name

    @property
    def cnp(self):
        return self.__cnp

    @cnp.setter
    def cnp(self, new_cnp):
        self.__cnp = new_cnp

    def __eq__(self, other):
        if not isinstance(other, Customer):
            return False
        return (self.name == other.name and self.cnp == other.cnp)


    def __ne__(self, other):
        return not (self.name == other.name and self.cnp == other.cnp)

    def __str__(self):
        return f"[Nume:{self.name}, CNP:{self.cnp}, ID:{self.id}]"

    @id.setter
    def id(self, value):
        self.__id = value


def test_customer():
    customer = Customer("test", "2950802129071")
    assert customer.name == "test"
    assert customer.cnp == "2950802129071"

    customer.name = "test1"
    customer.cnp = "2950802129072"

    assert customer.name == "test1"
    assert customer.cnp == "2950802129072"


test_customer()
Customer.customer_count = 0
