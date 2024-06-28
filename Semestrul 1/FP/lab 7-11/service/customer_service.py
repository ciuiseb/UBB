from domain import Customer
from domain import CustomerValidator
from repo import CustomerFileRepository, CustomersRepository


class CustomerService:
    def __init__(self, path):
        self.repo = CustomerFileRepository(path)
        # self.repo = CustomersRepository()
        self.__validator = CustomerValidator()

    def add_customer(self, name, cnp):
        customer = Customer(name, cnp)
        if self.__validator.validate(customer):
            self.repo.add(customer)

    def del_customer(self, id):
        self.repo.get_all().remove(self.search_customer_by_id(id))

    def modify_customer(self, cnp, modification, new_atribute):
        customer = self.search_customer_by_cnp(cnp)
        if modification == "numele":
            customer.name = new_atribute
        elif modification == "cnpul":
            customer.cnp = new_atribute

        for element in self.repo.get_all():
            if element.id == customer.id:
                element.name = customer.name
                element.cnp = customer.cnp

    def search_customer_by_name(self, customer_name):
        aparitions = 0
        errors = []
        res = 0
        for element in self.repo.get_all():
            if element.name == customer_name:
                res = element
                aparitions += 1
        if aparitions == 0:
            return 0
        if aparitions > 1:
            return 1
        return res

    def search_customer_by_cnp(self, cnp):
        ok = 0
        errors = []
        res = 0
        if cnp in self.repo.get_cnps():
            for element in self.repo.get_all():
                if element.cnp == cnp:
                    res = element
                    ok = 1
        if ok == 0:
            return 0
        return res

    def search_customer_by_id(self, id_client):
        if id_client <= len(self.repo):
            for element in self.get_all():
                if element.id == id_client:
                    return element
        else:
            raise Exception

    def get_all(self):
        return self.repo.get_all()


def test_get_all():
    # and add
    customer_service = CustomerService("Test_customersFile.txt")
    customer_service.repo.clear()
    customer_service.add_customer("adi", "1951005330651")

    assert customer_service.get_all()[0].name == "adi"
    assert customer_service.get_all()[0].cnp == "1951005330651"

    Customer.customer_count = 0


def test_del():
    customer_service = CustomerService("Test_customersFile.txt")
    customer_service.repo.clear()
    customer_service.add_customer("adi", "1951005330651")
    customer_service.add_customer("vasile", "1951005332651")
    customer_service.del_customer(1)

    assert customer_service.get_all()[0].name == "vasile"
    assert customer_service.get_all()[0].cnp == "1951005332651"
    Customer.customer_count = 0


def test_modify():
    customer_service = CustomerService("Test_customersFile.txt")
    customer_service.repo.clear()
    customer_service.add_customer("vasile", "1951005332651")

    customer_service.modify_customer("1951005332651", "numele", "alex")
    customer_service.modify_customer("1951005332651", "cnpul", "2951005332651")

    assert customer_service.get_all()[0].name == "alex"
    assert customer_service.get_all()[0].cnp == "2951005332651"
    Customer.customer_count = 0


def test_search_by_name():
    customer_service = CustomerService("Test_customersFile.txt")
    customer_service.repo.clear()
    customer_service.add_customer("alex", "2951005332651")
    assert customer_service.search_customer_by_name("alex") == Customer("alex", "2951005332651")
    Customer.customer_count = 0


def test_search_by_cnp():
    customer_service = CustomerService("Test_customersFile.txt")
    customer_service.repo.clear()
    customer_service.add_customer("alex", "2951005332651")

    assert customer_service.search_customer_by_cnp("2951005332651") == Customer("alex", "2951005332651")
    Customer.customer_count = 0


def test_search_by_id():
    customer_service = CustomerService("Test_customersFile.txt")
    customer_service.repo.clear()

    customer_service.add_customer("alex", "2951005332651")

    assert customer_service.search_customer_by_id(1) == Customer("alex", "2951005332651")
    Customer.customer_count = 0


def test_customer_service():
    test_get_all()
    test_del()
    test_search_by_name()
    test_search_by_cnp()
    test_search_by_id()
    test_modify()


test_customer_service()
