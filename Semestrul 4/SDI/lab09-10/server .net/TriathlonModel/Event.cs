namespace Triathlon.Model
{
    public class Event : TEntity<long>
    {
        private string name;
        private string date;
        private string location;
        private string description;
        public Event() : base()
        {

        }
        public Event(long id, string name, string date, string location, string description) : base(id)
        {
            this.name = name;
            this.date = date;
            this.location = location;
            this.description = description;
        }

        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        public string Date
        {
            get { return date; }
            set { date = value; }
        }

        public string Location
        {
            get { return location; }
            set { location = value; }
        }

        public string Description
        {
            get { return description; }
            set { description = value; }
        }
    }
}