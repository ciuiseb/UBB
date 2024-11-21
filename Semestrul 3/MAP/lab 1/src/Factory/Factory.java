package Factory;

import ComplexExpression.ComplexExpression;
import ComplexNumber.ComplexNumber;
import ComplexNumber.Operation;

import java.awt.*;
import java.util.LinkedList;

public interface Factory {
    public ComplexExpression createExpression(Operation op, LinkedList<ComplexNumber>numbers);
}
