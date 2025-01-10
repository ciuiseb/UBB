package Factory;

import java.util.LinkedList;

import ComplexExpression.ComplexExpression;
import ComplexExpression.ComplexAddition;
import ComplexExpression.ComplexSubstraction;
import ComplexExpression.ComplexMultiplication;
import ComplexExpression.ComplexDivision;
import ComplexNumber.ComplexNumber;
import ComplexNumber.Operation;

public class ExpressionFactory implements Factory {


    @Override
    public ComplexExpression createExpression(Operation op, LinkedList<ComplexNumber> numbers) {
        ComplexExpression expr = null;
        switch (op){
            case ADDITION -> expr = new ComplexAddition(numbers);
            case SUBTRACTION -> expr = new ComplexSubstraction(numbers);
            case DIVISION -> expr = new ComplexDivision(numbers);
            case MULTIPLICATION -> expr = new ComplexMultiplication(numbers);
        }
        return expr;
    }
}
