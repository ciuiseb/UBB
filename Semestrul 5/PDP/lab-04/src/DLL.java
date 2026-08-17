import java.util.ArrayList;
import java.util.Comparator;

public class DLL {
    

    private GradeNode head;
    private GradeNode tail;

    public DLL() {
        this.head = null;
        this.tail = null;
    }

    public GradeNode getHead() {
        return head;
    }

    public void addLast(int studentID, int grade) {
        GradeNode newGradeNode = new GradeNode(studentID, grade);
        if (head == null) {
            head = newGradeNode;
            tail = newGradeNode;
        } else {
            tail.next = newGradeNode;
            newGradeNode.prev = tail;
            tail = newGradeNode;
        }
    }

    public void updateGrade(int studentID, int newGrade) {
        GradeNode current = head;

        while (current != null) {
            if (current.studentId == studentID) {
                current.grade += newGrade;
                return;
            }
            current = current.next;
        }

        addLast(studentID, newGrade);
    }

    public void printForward() {
        GradeNode current = head;
        while (current != null) {
            System.out.println(current.studentId + ": " + current.grade);
            current = current.next;
        }
    }

    public void printSorted() {
        var records = getSorted();

        records.printForward();
    }

    public DLL getSorted() {
        DLL records = new DLL();
        GradeNode current = head;
        while (current != null) {
            records.addLast(current.studentId, current.grade);
            current = current.next;
        }

        if (records.head == null || records.head.next == null) {
            return records;
        }

        boolean swapped;
        do {
            swapped = false;
            GradeNode ptr1 = records.head;

            while (ptr1.next != null) {
                if (ptr1.studentId > ptr1.next.studentId) {

                    int tempId = ptr1.studentId;
                    int tempGrade = ptr1.grade;

                    ptr1.studentId = ptr1.next.studentId;
                    ptr1.grade = ptr1.next.grade;

                    ptr1.next.studentId = tempId;
                    ptr1.next.grade = tempGrade;

                    swapped = true;
                }
                ptr1 = ptr1.next;
            }
        } while (swapped);

        return records;
    }
}