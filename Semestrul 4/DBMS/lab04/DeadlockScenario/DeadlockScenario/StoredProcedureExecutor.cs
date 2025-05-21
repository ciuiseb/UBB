using System.Data;
using Microsoft.Data.SqlClient;

namespace DeadlockScenario
{
    public class StoredProcedureExecutor(int maxRetries)
    {
        public async Task ExecuteWithRetryAsync(string procedureName, string threadName)
        {
            int attempts = 0;
            bool success = false;

            while (!success && attempts < maxRetries)
            {
                attempts++;
                Console.WriteLine($"{threadName}: Attempt {attempts} to execute {procedureName}");

                try
                {
                    await using (var connection = DatabaseConfig.GetConnection())
                    {
                        await connection.OpenAsync();
                        await using (var command = new SqlCommand(procedureName, connection))
                        {
                            command.CommandType = CommandType.StoredProcedure;
                            await command.ExecuteNonQueryAsync();
                        }
                    }

                    success = true;
                    Console.WriteLine($"{threadName}: Successfully executed {procedureName} on attempt {attempts}");
                }
                catch (SqlException ex)
                {
                    if (ex.Number == 1205)
                    {
                        Console.WriteLine($"{threadName}: Deadlock detected on attempt {attempts} for {procedureName}. Error: {ex.Message}");

                        if (attempts >= maxRetries)
                        {
                            Console.WriteLine($"{threadName}: Maximum retries ({maxRetries}) reached for {procedureName}. Aborting.");
                        }
                        else
                        {
                            Console.WriteLine($"{threadName}: Waiting before retry...");
                            await Task.Delay(500 * attempts);
                        }
                    }
                    else
                    {
                        Console.WriteLine($"{threadName}: SQL Error executing {procedureName}: {ex.Message}");
                        throw;
                    }
                }
            }
        }
    }
}