using System;
using System.Windows.Forms;
using Triathlon.Model;
using Triathlon.Services;
using Triathlon.Client.WinForms.Utils;
using Triathlon.Network.JsonProtocol;

namespace Triathlon.Client.WinForms.Gui
{
    public partial class LoginForm : Form
    {
        private ITriathlonServices server;

        public LoginForm()
        {
            InitializeComponent();
            Initialize();
        }

        public void SetServer(ITriathlonServices server)
        {
            this.server = server;
        }

        private void Initialize()
        {
            this.loginButton.Click += HandleLogin;
        }

        private async void HandleLogin(object sender, EventArgs e)
        {
            string username = usernameField.Text.Trim();
            string password = passwordField.Text.Trim();

            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
            {
                MessageUtil.ShowAlert("Login Error", "Please fill in both fields.");
                return;
            }

            try
            {
                // Disable login button to prevent multiple clicks
                loginButton.Enabled = false;
                loginButton.Text = "Logging in...";

                Referee referee = new Referee
                {
                    Username = username,
                    Password = password
                };

                // Verify server is not null before proceeding
                if (server == null)
                {
                    MessageUtil.ShowError("Server connection not established");
                    return;
                }

                RefereeDashboardForm dashboardForm = new RefereeDashboardForm();

                // Use the async version
                var assignment = server.Login(referee, dashboardForm);
                dashboardForm.SetServer(server);
                dashboardForm.SetAssignment(assignment);
                dashboardForm.Show();
                this.Hide();
            }
            catch (TriathlonException ex)
            {
                MessageUtil.ShowError("Invalid username or password");
            }
            catch (Exception ex)
            {
                MessageUtil.ShowError($"An error occurred: {ex.Message}");
            }
            finally
            {
                // Re-enable login button
                loginButton.Enabled = true;
                loginButton.Text = "Login";
            }
        }

        private void LoginForm_Load(object sender, EventArgs e)
        {
            // Empty event handler
        }
    }
}