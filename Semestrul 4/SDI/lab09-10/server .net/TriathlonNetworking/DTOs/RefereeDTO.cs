using System;

namespace Triathlon.Network.Dto
{
    [Serializable]
    public class RefereeDTO
    {
        private long id;
        private string username;
        private string password;

        public RefereeDTO() { }

        public RefereeDTO(long id, string username, string password)
        {
            this.id = id;
            this.username = username;
            this.password = password;
        }

        public long Id { get { return id; } set { id = value; } }
        public string Username { get { return username; } set { username = value; } }
        public string Password { get { return password; } set { password = value; } }
    }
}