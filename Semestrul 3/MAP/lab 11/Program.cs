using lab_11.app;
using lab_11.tests;

namespace lab_11;

class Program
{
    public static void Main(string[] args)
    {
        TestRunner.RunTests();
        var settings = new Settings();
        var app = new App(settings);
        app.Start();
    }
}