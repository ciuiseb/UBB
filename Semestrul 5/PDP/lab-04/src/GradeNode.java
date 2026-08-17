public class GradeNode {
    int studentId;
    int grade;
    GradeNode next;
    GradeNode prev;

    GradeNode(int studentId, int grade) {
        this.studentId = studentId;
        this.grade = grade;
        this.next = null;
        this.prev = null;
    }
}