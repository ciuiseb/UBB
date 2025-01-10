namespace lab_11.app;

/// <summary>
/// Main application class for the Basketball Management System
/// </summary>
public class App
{
    private readonly IController _controller;

    public delegate void MatchScoreDisplayed(int matchId, string team1Name, int team1Score, string team2Name,
        int team2Score);

    public event MatchScoreDisplayed? OnMatchScoreDisplayed;

    /// <summary>
    /// Initializes a new instance of the App
    /// </summary>
    /// <param name="controller">The controller handling business logic operations</param>
    public App(IController controller)
    {
        _controller = controller;
    }

    /// <summary>
    /// Displays the main menu options to the user
    /// </summary>
    private static void Menu()
    {
        Console.WriteLine("\nBasketball Management System");
        Console.WriteLine("1. View Team Players");
        Console.WriteLine("2. View Active Players in Match");
        Console.WriteLine("3. View Matches in Date Range");
        Console.WriteLine("4. View Match Score");
        Console.WriteLine("0. Exit");
        Console.Write("Enter your choice: ");
    }

    /// <summary>
    /// Starts the application main loop
    /// </summary>
    public void Start()
    {
        while (true)
        {
            Menu();
            var choice = Console.ReadLine();

            try
            {
                switch (choice)
                {
                    case "1":
                        ViewTeamPlayers();
                        break;
                    case "2":
                        ViewActivePlayersInMatch();
                        break;
                    case "3":
                        ViewMatchesInDateRange();
                        break;
                    case "4":
                        ViewMatchScore();
                        break;
                    case "0":
                        return;
                    default:
                        Console.WriteLine("Invalid choice.");
                        break;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }

            Console.WriteLine("\nPress Enter to continue... ");
            Console.ReadKey();
            Console.Clear();
        }
    }

    /// <summary>
    /// Displays all players from a specified team
    /// </summary>
    private void ViewTeamPlayers()
    {
        Console.Write("Enter team name: ");
        var teamName = Console.ReadLine();
        if (string.IsNullOrWhiteSpace(teamName))
            return;

        var players = _controller.GetTeamPlayers(teamName);
        Console.WriteLine("\nTeam Players:");
        foreach (var player in players)
        {
            Console.WriteLine($"ID: {player.Id}, Name: {player.Name}");
        }
    }

    /// <summary>
    /// Displays active players and their scores for a specific team in a match
    /// </summary>
    private void ViewActivePlayersInMatch()
    {
        Console.Write("Enter team name: ");
        var teamName = Console.ReadLine();
        if (string.IsNullOrWhiteSpace(teamName))
            return;

        Console.Write("Enter date (yyyy-MM-dd): ");
        if (!DateTime.TryParse(Console.ReadLine(), out DateTime date))
            return;

        var matchesOnDate = _controller.GetMatchesOnDate(date);

        var match = matchesOnDate.FirstOrDefault(m =>
            _controller.GetTeamName(m.Team1Id) == teamName ||
            _controller.GetTeamName(m.Team2Id) == teamName);

        if (match == null)
        {
            Console.WriteLine($"No match found for team {teamName} on {date:yyyy-MM-dd}");
            return;
        }

        int teamId = _controller.GetTeamName(match.Team1Id) == teamName ? match.Team1Id : match.Team2Id;

        var activePlayers = _controller.GetTeamActivePlayersInMatch(teamId ,match.Id);

        Console.WriteLine("\nActive Players:");
        foreach (var player in activePlayers)
        {
            Console.WriteLine($"ID: {player.Id}, Player: {_controller.GetPlayerName(player.PlayerId)}, Points: {player.PointsScored}");
        }
    }

    /// <summary>
    /// Displays all matches within a specified date range
    /// </summary>
    private void ViewMatchesInDateRange()
    {
        Console.Write("Enter start date (yyyy-MM-dd): ");
        if (!DateTime.TryParse(Console.ReadLine(), out DateTime startDate))
            return;

        Console.Write("Enter end date (yyyy-MM-dd): ");
        if (!DateTime.TryParse(Console.ReadLine(), out DateTime endDate))
            return;

        var matches = _controller.GetMatchesInDateRange(startDate, endDate);
        Console.WriteLine("\nMatches in range:");
        foreach (var match in matches)
        {
            Console.WriteLine(
                $"ID: {match.Id}, Teams: {_controller.GetTeamName(match.Team1Id)} vs {_controller.GetTeamName(match.Team2Id)}, Date: {match.Date}"
            );
        }
    }

    /// <summary>
    /// Displays the score of a selected match from a specific date
    /// </summary>
    private void ViewMatchScore()
    {
        Console.Write("Enter date (yyyy-MM-dd): ");
        if (!DateTime.TryParse(Console.ReadLine(), out DateTime date))
            return;

        var matchesOnDate = _controller.GetMatchesOnDate(date);
        if (matchesOnDate.Count == 0)
        {
            Console.WriteLine($"No matches found on {date:yyyy-MM-dd}");
            return;
        }

        Console.WriteLine("\nMatches on this date:");
        foreach (var match in matchesOnDate)
        {
            var team1Name = _controller.GetTeamName(match.Team1Id);
            var team2Name = _controller.GetTeamName(match.Team2Id);
            Console.WriteLine($"{match.Id}. {team1Name} vs {team2Name}");
        }

        Console.Write("\nEnter match ID to see score: ");
        if (!int.TryParse(Console.ReadLine(), out int matchId))
            return;

        var (team1Score, team2Score, team1Id, team2Id) = _controller.GetMatchScoreAndTeams(matchId);

        Console.WriteLine(
            $"\nScore: {_controller.GetTeamName(team1Id)}: {team1Score}, {_controller.GetTeamName(team2Id)}: {team2Score}");
    }
}