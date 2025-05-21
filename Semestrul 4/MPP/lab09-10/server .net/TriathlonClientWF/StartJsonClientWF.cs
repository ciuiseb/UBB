using System;
using System.Windows.Forms;
using Triathlon.Client.WinForms.Gui;
using Triathlon.Network.JsonProtocol;
using Triathlon.Services;
using log4net;
using System.Configuration;
using System.Globalization;

namespace Triathlon.Client.WinForms;

static class StartJsonClientWF
{
    private static readonly ILog logger = LogManager.GetLogger(typeof(StartJsonClientWF));

    private const int DEFAULT_PORT = 55555;
    private const string DEFAULT_SERVER = "localhost";

    private static TriathlonServicesJsonProxy server;
    [STAThread]
    static void Main()
    {
        try
        {
            log4net.Config.XmlConfigurator.Configure(new FileInfo("log4net.config"));

            logger.Info("Starting Triathlon Client WinForms...");

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.ApplicationExit += Application_ApplicationExit;

            string serverIP = ConfigurationManager.AppSettings["triathlon.server.host"] ?? DEFAULT_SERVER;
            int serverPort = DEFAULT_PORT;

            string portString = ConfigurationManager.AppSettings["triathlon.server.port"];
            if (!string.IsNullOrEmpty(portString))
            {
                if (!int.TryParse(portString, out serverPort))
                {
                    logger.Error("Wrong port number in settings");
                    serverPort = DEFAULT_PORT;
                }
            }

            logger.Info($"Using server {serverIP}:{serverPort}");

            ITriathlonServices server = new TriathlonServicesJsonProxy(serverIP, serverPort);

            LoginForm loginForm = new LoginForm();
            loginForm.SetServer(server);

            logger.Info("Triathlon Client WinForms started successfully");

            // Run the application
            Application.Run(loginForm);
        }
        catch (Exception ex)
        {
            logger.Error("Error starting application", ex);
            MessageBox.Show($"Error starting application: {ex.Message}", "Error",
                MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }
    private static void Application_ApplicationExit(object sender, EventArgs e)
    {
        // This event is raised when the application is about to exit
        CleanupResources();
    }

    private static void CleanupResources()
    {
        try
        {
            // Close the server connection if it exists
            if (server != null)
            {
                logger.Info("Closing server connection...");
                server.CloseConnection();
                server = null;
            }

            logger.Info("Application shut down cleanly");
        }
        catch (Exception ex)
        {
            logger.Error("Error during application cleanup", ex);
        }
    }
}