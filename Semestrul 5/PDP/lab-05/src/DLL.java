public class DLL {
    private final GradeNode head;
    private final GradeNode tail;

    public DLL() {
        head = new GradeNode(Integer.MIN_VALUE, Integer.MIN_VALUE);
        tail = new GradeNode(Integer.MAX_VALUE, Integer.MAX_VALUE);

        head.next = tail;
        tail.prev = head;
    }

    public void updateGrade(int studentID, int gradeToAdd) {
        GradeNode pred = null;
        GradeNode curr = null;

        head.lock.lock();
        try {
            pred = head;
            curr = head.next;
            curr.lock.lock();
            try {
                while (curr != tail) {
                    if (curr.studentId == studentID) {
                        if (gradeToAdd == -1) {
                            curr.plagiat = true;
                        } else {
                            curr.grade += gradeToAdd;
                        }
                        return;
                    }

                    GradeNode nextNode = curr.next;
                    nextNode.lock.lock();

                    pred.lock.unlock();
                    pred = curr;
                    curr = nextNode;
                }

                GradeNode newNode = new GradeNode(studentID, gradeToAdd);
                if (gradeToAdd == -1) {
                    newNode.plagiat = true;
                }
                newNode.prev = pred;
                newNode.next = curr;
                pred.next = newNode;
                curr.prev = newNode;


            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }


    public void insertSorted(int studentId, int grade) {
        GradeNode newNode = new GradeNode(studentId, grade);

        head.lock.lock();
        GradeNode pred = head;

        try {
            GradeNode curr = pred.next;
            curr.lock.lock();
            try {
                while (curr != tail &&
                        (curr.grade < grade || (curr.grade == grade && curr.studentId < studentId))) {

                    GradeNode nextNode = curr.next;
                    nextNode.lock.lock();

                    pred.lock.unlock();
                    pred = curr;
                    curr = nextNode;
                }

                newNode.next = curr;
                newNode.prev = pred;
                pred.next = newNode;
                curr.prev = newNode;

            } finally {
                if (curr.lock.isHeldByCurrentThread()) curr.lock.unlock();
            }
        } finally {
            if (pred.lock.isHeldByCurrentThread()) pred.lock.unlock();
        }
    }

    public GradeNode poll() {
        head.lock.lock();
        try {
            GradeNode firstReal = head.next;
            firstReal.lock.lock();
            try {
                if (firstReal == tail) {
                    return null;
                }

                GradeNode secondReal = firstReal.next;
                secondReal.lock.lock();
                try {
                    head.next = secondReal;
                    secondReal.prev = head;

                    firstReal.next = null;
                    firstReal.prev = null;
                    return firstReal;
                } finally {
                    secondReal.lock.unlock();
                }
            } finally {
                firstReal.lock.unlock();
            }
        } finally {
            head.lock.unlock();
        }
    }

    public void printForward() {
        GradeNode curr = head.next;
        while (curr != tail) {
            System.out.println(curr.studentId + ": " + curr.grade);
            curr = curr.next;
        }
    }

    public GradeNode getHead() {
        return head;
    }

    public GradeNode getTail() {
        return tail;
    }
}