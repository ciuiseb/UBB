using lab_11.domain;
using lab_11.service;

namespace lab_11.tests;

public class PlayerServiceTests
{
    private readonly MockPlayerDatabaseRepository _playerDatabaseRepo;
    private readonly MockActivePlayerDatabaseRepository _activePlayerDatabaseRepo;
    private readonly PlayerService _service;

    public PlayerServiceTests()
    {
        _playerDatabaseRepo = new MockPlayerDatabaseRepository();
        _activePlayerDatabaseRepo = new MockActivePlayerDatabaseRepository();
        _service = new PlayerService(_playerDatabaseRepo, _activePlayerDatabaseRepo, null);
    }



    private void TestGetTeamActivePlayersInMatch()
    {
        var testPlayers = new List<Player>
        {
            new() { Id = 1, TeamId = 1, Name = "Player 1" },
            new() { Id = 2, TeamId = 1, Name = "Player 2" },
            new() { Id = 3, TeamId = 2, Name = "Player 3" }
        };

        var testActivePlayers = new List<ActivePlayer>
        {
            new() { Id = 1, PlayerId = 1, MatchId = 1 },
            new() { Id = 2, PlayerId = 2, MatchId = 1 },
            new() { Id = 3, PlayerId = 3, MatchId = 1 }
        };

        _playerDatabaseRepo.AddTestData(testPlayers);
        _activePlayerDatabaseRepo.AddTestData(testActivePlayers);

        var result = _service.GetTeamActivePlayersInMatch(1, 1);

        if (result.Count != 2 || result.Any(p => p.MatchId != 1))
            throw new Exception("TestGetTeamActivePlayersInMatch failed");
    }

    public void RunTests()
    {
        TestGetTeamActivePlayersInMatch();
    }
}