namespace lab_11.tests;

public static class TestRunner
{
    public static void RunTests()
    {
        var playerTests = new PlayerServiceTests();
        var matchsTests = new MatchServiceTests();

        playerTests.RunTests();
        matchsTests.RunTests();

        Console.WriteLine("Tests completed.");
    }
}