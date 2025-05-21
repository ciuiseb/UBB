using System;
using System.Windows.Forms;
using cs.Domain;
using cs.Service;

namespace cs.GUI;

public partial class LoginForm : Form
{
    private IService _service;

    public LoginForm()
    {
        InitializeComponent();
    }
    public void SetService(IService service)
    {
        _service = service;
    }

    private void InitializeComponent()
    {
        this.Text = "Referee Login";
        this.Size = new System.Drawing.Size(400, 250);
        this.StartPosition = FormStartPosition.CenterScreen;
        this.FormBorderStyle = FormBorderStyle.FixedDialog;
        this.MaximizeBox = false;
        this.MinimizeBox = false;

        Label titleLabel = new Label
        {
            Text = "Referee Login",
            Font = new System.Drawing.Font("Segoe UI", 20, System.Drawing.FontStyle.Bold),
            TextAlign = System.Drawing.ContentAlignment.MiddleCenter,
            Dock = DockStyle.Top,
            Height = 50
        };

        Label usernameLabel = new Label
        {
            Text = "Username:",
            TextAlign = System.Drawing.ContentAlignment.MiddleRight,
            Location = new System.Drawing.Point(50, 70),
            Size = new System.Drawing.Size(100, 30)
        };

        TextBox usernameField = new TextBox
        {
            Location = new System.Drawing.Point(160, 70),
            Size = new System.Drawing.Size(180, 30),
            Name = "usernameField"
        };

        Label passwordLabel = new Label
        {
            Text = "Password:",
            TextAlign = System.Drawing.ContentAlignment.MiddleRight,
            Location = new System.Drawing.Point(50, 110),
            Size = new System.Drawing.Size(100, 30)
        };

        TextBox passwordField = new TextBox
        {
            Location = new System.Drawing.Point(160, 110),
            Size = new System.Drawing.Size(180, 30),
            Name = "passwordField",
            PasswordChar = '*'
        };

        Button loginButton = new Button
        {
            Text = "Login",
            Location = new System.Drawing.Point(160, 160),
            Size = new System.Drawing.Size(100, 30),
            Name = "loginButton"
        };
        loginButton.Click += LoginButton_Click;

        this.Controls.Add(titleLabel);
        this.Controls.Add(usernameLabel);
        this.Controls.Add(usernameField);
        this.Controls.Add(passwordLabel);
        this.Controls.Add(passwordField);
        this.Controls.Add(loginButton);
    }

    private void LoginButton_Click(object sender, EventArgs e)
    {
        string username = Controls["usernameField"].Text.Trim();
        string password = Controls["passwordField"].Text.Trim();

        if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
        {
            ShowAlert("Please fill in both fields.");
            return;
        }

        Referee referee = _service.Login(username, password);

        if (referee != null)
        {
            try
            {
                RefereeDashboardForm dashboardForm = new RefereeDashboardForm(_service, referee);
                dashboardForm.Text = $"Referee Dashboard - {referee.GetName()}";

                this.Hide();
                dashboardForm.ShowDialog();
                this.Close();
            }
            catch (Exception ex)
            {
                ShowAlert($"Could not open dashboard: {ex.Message}");
            }
        }
        else
        {
            ShowAlert("Invalid credentials. Please try again.");
        }
    }

    private void ShowAlert(string message)
    {
        MessageBox.Show(
            message,
            "Login Error",
            MessageBoxButtons.OK,
            MessageBoxIcon.Error
        );
    }
}