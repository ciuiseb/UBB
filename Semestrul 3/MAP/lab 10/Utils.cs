using lab10.factory;
using lab10.task_runner;

namespace lab10;

public class Utils
{
    public static List<MessageTask> GetMessageTasks()
    {
        var tasks = new List<MessageTask>
        {
            new MessageTask(
                "1",
                "asdasd",
                "Salut",
                "Maria",
                "Andrei",
                DateTime.Now
            ),
            new MessageTask(
                "2",
                "a",
                "Salut",
                "Andrei",
                "Maria",
                DateTime.Now
            ),
            new MessageTask(
                "3",
                "sd",
                "Saluts",
                "ss",
                "ds",
                DateTime.Now
            ),
            new MessageTask(
                "4",
                "asdasd",
                "sssssss",
                "dddd",
                "dddddddd",
                DateTime.Now
            ),
            new MessageTask(
                "5",
                "aa",
                "a",
                "Maraaia",
                "a",
                DateTime.Now
            ),


        };

        return tasks;
    }
    public static void TestRunStrategyTaskRunner(Strategy strategy)
    {
        var messageTasks = GetMessageTasks();

        var strategyTaskRunner = new StrategyTaskRunner(strategy);

        foreach (var msg in messageTasks)
        {
            strategyTaskRunner.AddTask(msg);
        }
        strategyTaskRunner.ExecuteAll();
        Console.WriteLine();
    }

    public static void TestRunPrinter()
    {
        var messageTasks = GetMessageTasks();
        var strategy = Strategy.Fifo;
        var strategyTaskRunner = new StrategyTaskRunner(strategy);
        foreach (var msg in messageTasks)
        {
            strategyTaskRunner.AddTask(msg);
        }
        strategyTaskRunner.ExecuteAll();
        Console.WriteLine();

        var printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        foreach (var msg in messageTasks)
        {
            printerTaskRunner.AddTask(msg);
        }
        printerTaskRunner.ExecuteAll();
    }
    public static void TestRunDelayRunner()
    {
        var messageTasks = GetMessageTasks();
        var strategy = Strategy.Fifo;
        var strategyTaskRunner = new StrategyTaskRunner(strategy);
        foreach (var msg in messageTasks)
        {
            strategyTaskRunner.AddTask(msg);
        }
        strategyTaskRunner.ExecuteAll();
        Console.WriteLine();

        var delayTaskRunner = new DelayTaskRunner(strategyTaskRunner);
        foreach (var msg in messageTasks)
        {
            delayTaskRunner.AddTask(msg);
        }
        delayTaskRunner.ExecuteAll();
    }
}