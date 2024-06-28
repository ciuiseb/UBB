from domain import Customer

class RepoError(Exception):
    pass
class CustomerFileRepository():
    def __init__(self, fileN):
        self.__fileName = fileN
        self.__elements = []
        self.__loadFromFile()
    def add(self, val):
        self.__elements.append(val)
        self.__storeToFile()
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
    def clear(self):
        self.__elements = []
        self.__storeToFile()

    def __loadFromFile(self):
        try:
            with open(self.__fileName, "r") as f:
                for line in f:
                    nume, cnp, id = line.strip().split(",")
                    customer = Customer(nume, cnp)
                    customer.id = id
                    self.add(customer)
        except FileNotFoundError as nf:
            raise RepoError(nf)

    def __storeToFile(self):
        try:
            file = open(self.__fileName, "w")
            Customer.customer_count = 0
        except IOError:
            return
        for customer in self.get_all():
            cststr = customer.name + "," + customer.cnp + "," + str(customer.id) + "\n"
            file.write(cststr)
        file.close()

def test_customer_file():
    repo = CustomerFileRepository("Test_customersFile.txt")
    repo.clear()
    repo.add(Customer("Alex", "1930518523029"))
    assert len(repo) == 1

test_customer_file()