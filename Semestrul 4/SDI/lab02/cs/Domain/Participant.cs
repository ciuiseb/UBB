namespace cs.Domain;

public class Participant : Entity<long>
{
    private string firstName;
    private string lastName;
    private int age;
    private string gender;

    public Participant(string firstName, string lastName, int age, string gender)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
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

    public int GetAge()
    {
        return age;
    }

    public void SetAge(int age)
    {
        this.age = age;
    }

    public string GetGender()
    {
        return gender;
    }

    public void SetGender(string gender)
    {
        this.gender = gender;
    }

    public string GetName()
    {
        return firstName + " " + lastName;
    }
}