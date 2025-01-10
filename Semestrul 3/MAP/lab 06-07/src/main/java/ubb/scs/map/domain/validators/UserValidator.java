package ubb.scs.map.domain.validators;


import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.exceptions.ValidationException;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getFirstName().isEmpty() || entity.getLastName().isEmpty())
            throw new ValidationException("Utilizatorul nu este valid");
    }
}
