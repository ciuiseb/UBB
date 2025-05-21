namespace cs.Domain;

public class Referee : IEntity<long>
{
    //username, password, email, first_name, last_name
    private string username;
    private string password;
    private string email;
    private string firstName;
    private string lastName;

    public Referee(string username, string password, string email, string firstName, string lastName)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public string GetUsername()
    {
        return username;
    }

    public void SetUsername(string username)
    {
        this.username = username;
    }

    public string GetPassword()
    {
        return password;
    }

    public void SetPassword(string password)
    {
        this.password = password;
    }

    public string GetEmail()
    {
        return email;
    }

    public void SetEmail(string email)
    {
        this.email = email;
    }

    public string GetFirstName()
    {
        return firstName;
    }

    public void SetFirstName(string firstName)
    {
        this.firstName = firstName;
    }

    public string GetLastName()
    {
        return lastName;
    }

    public void SetLastName(string lastName)
    {
        this.lastName = lastName;
    }

    public string GetName()
    {
        return firstName + " " + lastName;
    }
}