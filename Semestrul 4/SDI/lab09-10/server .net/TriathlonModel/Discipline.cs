using System;

namespace Triathlon.Model
{
    public enum Discipline
    {
        RUNNING = 1,
        SWIMMING = 2,
        CYCLING = 3
    }

    public static class DisciplineExtensions
    {
        public static long GetId(this Discipline discipline)
        {
            return (long)discipline;
        }

        public static Discipline GetById(long id)
        {
            foreach (Discipline discipline in Enum.GetValues(typeof(Discipline)))
            {
                if ((long)discipline == id)
                {
                    return discipline;
                }
            }
            throw new ArgumentException($"No Discipline with id {id}");
        }
    }
}