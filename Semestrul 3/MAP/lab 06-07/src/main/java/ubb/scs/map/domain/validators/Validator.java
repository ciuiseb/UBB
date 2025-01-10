package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}