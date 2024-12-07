using lab10.factory;

namespace lab10;

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("------------------------");
        Utils.TestRunStrategyTaskRunner(Strategy.Fifo);
        Console.WriteLine("------------------------");
        Utils.TestRunPrinter();
        Console.WriteLine("------------------------");
        Utils.TestRunDelayRunner();
    }

}
