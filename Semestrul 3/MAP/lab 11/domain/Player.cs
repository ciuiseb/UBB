namespace lab_11.domain;

public class Player : Student
{
    public int TeamId { get; set; }
}

public enum PlayerType
{
    Reserve,
    Starter
}

public class ActivePlayer : Entity<int>
{
    public int PlayerId { get; set; }
    public int MatchId { get; set; }
    public int PointsScored { get; set; }
    public PlayerType Type { get; set; }
}