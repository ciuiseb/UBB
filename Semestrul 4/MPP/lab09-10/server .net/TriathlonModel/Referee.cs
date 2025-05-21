namespace Triathlon.Model
{
    public class Referee : TEntity<long>
    {
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

        public Referee()
        {
            this.username = "";
            this.password = "";
            this.email = "";
            this.firstName = "";
            this.lastName = "";
        }

        public string Username
        {
            get { return username; }
            set { username = value; }
        }

        public string Password
        {
            get { return password; }
            set { password = value; }
        }

        public string Email
        {
            get { return email; }
            set { email = value; }
        }

        public string FirstName
        {
            get { return firstName; }
            set { firstName = value; }
        }

        public string LastName
        {
            get { return lastName; }
            set { lastName = value; }
        }

        public string Name
        {
            get { return firstName + " " + lastName; }
        }
    }
}