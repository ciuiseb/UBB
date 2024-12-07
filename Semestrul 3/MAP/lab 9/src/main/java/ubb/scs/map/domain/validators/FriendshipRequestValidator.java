package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.entities.FriendshipRequest;
import ubb.scs.map.domain.exceptions.ValidationException;

public class FriendshipRequestValidator implements Validator<FriendshipRequest>{
    @Override
    public void validate(FriendshipRequest entity) throws ValidationException {
        if(entity.getFirst() == null){
            throw(new ValidationException("First id cannot be null"));
        }
        if(entity.getSecond() == null){
            throw(new ValidationException("Second id cannot be null"));
        }
        if(entity.getSentDate() == null){
            throw(new ValidationException("Date cannot be null"));
        }
    }
}
