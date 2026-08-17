import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GradeNode {
    int studentId;
    int grade;
    Boolean plagiat;
    GradeNode next;
    GradeNode prev;

    public ReentrantLock lock = new ReentrantLock();

    public GradeNode(int studentId, int grade) {
        this.studentId = studentId;
        this.grade = grade;
        this.plagiat = false;
        this.next = null;
        this.prev = null;
    }
}