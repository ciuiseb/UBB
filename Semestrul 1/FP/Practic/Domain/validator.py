from Domain.show_class import Show


class ValidatorError(Exception):
    pass


class Validator:
    def validate(self, element: Show):
        """
        Valideaza integritatea lui element. Altfel eroare
        :param element:
        :return: True, daca e corect definit
        """
        errors = []
        errors += self.validate_title(element.title)
        errors += self.validate_artist(element.artist)
        errors += self.validate_genre(element.genre)
        errors += self.validate_duration(element.duration)
        if errors:
            raise ValidatorError(", ".join(errors))
        return True

    def validate_title(self, title):
        """
        Valideaza titlul
        :param title: Titlul unui element tip spectacol
        :return: Lista de erori. Daca e goala nu sunt erori
        """
        errors = []
        if title == "":
            errors.append("Titlul nu poate sa fie vid!")
        return errors

    def validate_artist(self, artist):
        """
            Valideaza numele artistului
            :param artist: Numele artistului unui element tip spectacol
            :return: Lista de erori. Daca e goala nu sunt erori
        """
        errors = []
        if artist == "":
            errors.append("Numele artistului nu poate sa fie vid!")
        return errors

    def validate_genre(self, genre):
        """
        Valideaza genul
        :param genre: Numele genului unui element tip spectacol
        :return: Lista de erori. Daca e goala nu sunt erori
        """
        errors = []
        accepted_genres = ["Comedie", "Concert", "Balet", "Altele"]
        if genre not in accepted_genres:
            errors.append(f"Genul '{genre}' nu este acceptat!")
        return errors

    def validate_duration(self, duration):
        """
        Valideaza durata
        :param duration: Durata unui element de tip spectacol
        :return: Lista de erori. Daca e goala nu sunt erori
        """
        errors = []
        try:
            int(duration)
        except ValueError:
            errors.append("Durata trebuie sa fie un numar!")
        if not errors:
            duration = str(duration)
            if "." in duration or "-" in duration:
                errors.append("Durata trebuie sa fie un intreg pozitiv!")
        return errors
