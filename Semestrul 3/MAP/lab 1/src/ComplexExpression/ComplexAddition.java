package ComplexExpression;

import ComplexNumber.ComplexNumber;

import java.util.LinkedList;

public class ComplexAddition extends ComplexExpression {

    public ComplexAddition(LinkedList<ComplexNumber> nr) {
        super(nr);
    }

    @Override
    protected ComplexNumber execute(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.getRealPart() + b.getRealPart(),
                a.getImaginaryPart() + b.getImaginaryPart());
    }
}
