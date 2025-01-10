package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.entities.Friendship;
import ubb.scs.map.domain.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship>{
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getFirst() == null)
            throw new ValidationException("Prmimul utilizator nu este valid");
        if(entity.getSecond() == null)
            throw new ValidationException("Al doilea utilizator nu este valid");
        if(entity.getFirst().equals(entity.getSecond()))
            throw new ValidationException("Prietenia nu este valida");
    }
}
