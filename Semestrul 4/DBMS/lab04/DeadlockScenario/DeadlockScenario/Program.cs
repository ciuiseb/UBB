namespace DeadlockScenario;
class Program
{
    static async Task Main(string[] args)
    {
        StoredProcedureExecutor executor = new StoredProcedureExecutor( 3);

        Task task1 = executor.ExecuteWithRetryAsync("ProcedureOne", "Thread-1");
        Task task2 = executor.ExecuteWithRetryAsync("ProcedureTwo", "Thread-2");

        await Task.WhenAll(task1, task2);
    }

}