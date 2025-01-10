package ComplexExpression;

import ComplexNumber.ComplexNumber;

import java.util.LinkedList;

public abstract class ComplexExpression {
    protected LinkedList<ComplexNumber> numbers;
    public ComplexExpression(LinkedList<ComplexNumber>nr) {
        this.numbers = nr;
    }
    public ComplexNumber executeExpression(){
        while(numbers.size() != 1){
            var result = execute(numbers.get(0), numbers.get(1));
            numbers.removeFirst();
            numbers.removeFirst();
            numbers.addFirst(result);
        }
        return numbers.getFirst();
    }
    protected abstract ComplexNumber execute(ComplexNumber a, ComplexNumber b);
}
