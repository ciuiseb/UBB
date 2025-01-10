using lab_11.domain;
using lab_11.repo;

namespace lab_11.tests;

public class MockPlayerDatabaseRepository() : PlayerDatabaseRepository("ignored")
{
    private List<Player> _players = [];

    public void AddTestData(List<Player> players)
    {
        _players = players;
    }

    public override List<Player> FindAll()
    {
        return _players;
    }
}

public class MockActivePlayerDatabaseRepository() : ActivePlayerDatabaseRepository("ignored")
{
    private List<ActivePlayer> _activePlayers = [];

    public void AddTestData(List<ActivePlayer> activePlayers)
    {
        _activePlayers = activePlayers;
    }

    public override List<ActivePlayer> FindAll()
    {
        return _activePlayers;
    }
}

public class MockMatchDatabaseRepository() : MatchDatabaseRepository("ignored")
{
    private List<Match> _matches = [];

    public void AddTestData(List<Match> matches)
    {
        _matches = matches;
    }

    public override List<Match> FindAll()
    {
        return _matches;
    }

    public override Match FindOne(int id)
    {
        return _matches.FirstOrDefault(m => m.Id == id);
    }
}
