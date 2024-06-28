from domain import Customer


class CustomersRepository:
    def __init__(self):
        self.__elements = []

    def add(self, customer: Customer):
        self.__elements.append(customer)

    def get_all(self):
        return self.__elements

    def get_names(self):
        names = []
        for customer in self.get_all():
            names.append(customer.name)
        return names

    def get_cnps(self):
        cnps = []
        for customer in self.get_all():
            cnps.append(customer.cnp)
        return cnps

    def __len__(self):
        return len(self.__elements)


def test_customers_repo():
    customer_repo = CustomersRepository()
    customer_repo.add(Customer("adi", "1951005330651"))

    assert customer_repo.get_all()[0].name == "adi"
    assert customer_repo.get_all()[0].cnp == "1951005330651"

    assert customer_repo.get_names()[0] == "adi"

    assert customer_repo.get_cnps()[0] == "1951005330651"

    Customer.customer_count = 0


test_customers_repo()
