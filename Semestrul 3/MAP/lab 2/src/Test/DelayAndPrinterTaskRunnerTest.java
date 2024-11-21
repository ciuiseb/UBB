package Test;

import Factory.Strategy;
import TaskRunner.DelayTaskRunner;
import TaskRunner.PrinterTaskRunner;
import TaskRunner.StrategyTaskRunner;
import Tasks.MessageTask;

import java.util.Scanner;

public class DelayAndPrinterTaskRunnerTest {
    private static Scanner scanner = new Scanner(System.in);
    private static MessageTask[] messageTasks;
    private static StrategyTaskRunner strategyTaskRunner;

    public static void main(String[] args) {
        messageTasks = getTasks();
        Strategy strategy = Strategy.FIFO;
//        if(args[0].equals("FIFO")){
//            strategy = Strategy.FIFO;
//        } else if(args[0].equals("LIFO")){
//            strategy = Strategy.LIFO;
//        }
        strategyTaskRunner = new StrategyTaskRunner(strategy);
        for(var msg: messageTasks){
            strategyTaskRunner.addTask(msg);
        }
        strategyTaskRunner.executeAll();
        System.out.println();

        DelayTaskRunner delayTaskRunner = new DelayTaskRunner(strategyTaskRunner);
        for(var msg: messageTasks){
            delayTaskRunner.addTask(msg);
        }
        delayTaskRunner.executeAll();
        System.out.println();

        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        for(var msg: messageTasks){
            printerTaskRunner.addTask(msg);
        }
        printerTaskRunner.executeAll();

    }
    public static MessageTask[] getTasks(){
        MessageTask[] tasks = {
                new MessageTask("1", "feedback lab 2", "Te-ai descurcat bine", "teacher", "student"),
                new MessageTask("2", "feedback lab 3", "Te-ai descurcat bine", "teacher", "student"),
                new MessageTask("3", "feedback lab 4", "Te-ai descurcat bine", "teacher", "student"),
                new MessageTask("4", "feedback lab 5", "Te-ai descurcat bine", "teacher", "student"),
                new MessageTask("5", "feedback lab 6", "Te-ai descurcat bine", "teacher", "student")
        };
        return tasks;
    }

}
