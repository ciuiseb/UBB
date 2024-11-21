package Tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MessageTask extends Task{
    public String message;
    public String from;
    public String to;

    public LocalDate date;

    public MessageTask(String taskID, String description) {
        super(taskID, description);
    }

    public MessageTask(String taskID, String description, String message, String from, String to) {
        super(taskID, description);
        this.message = message;
        this.from = from;
        this.to = to;
        this.date = LocalDateTime.now().toLocalDate();
    }

    @Override
    public void execute(){
        System.out.println("From: " + from + ", at " + date);
        System.out.println("To: " + to);
        System.out.println(message);
    }
}
