using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;
using cs.Domain;
using cs.Service;

namespace cs.GUI;

public partial class ReportForm : Form
{
    private readonly IService _service;
    private readonly Referee _referee;
    private Event _refereeEvent;

        private Button _backButton;
    private Label _eventLabel;
    private DataGridView _resultsGrid;

    public ReportForm(IService service, Referee referee)
    {
        _service = service;
        _referee = referee;
        InitializeComponent();
    }

    private void InitializeComponent()
    {
                this.Text = "Event Report";
        this.Size = new Size(800, 600);
        this.StartPosition = FormStartPosition.CenterScreen;
        this.FormBorderStyle = FormBorderStyle.Sizable;
        this.MinimumSize = new Size(600, 400);

                Panel topPanel = new Panel
        {
            Dock = DockStyle.Top,
            Height = 50,
            Padding = new Padding(10)
        };

        _backButton = new Button
        {
            Text = "Go Back",
            Size = new Size(100, 30),
            Location = new Point(10, 10)
        };
        _backButton.Click += BackButton_Click;

        _eventLabel = new Label
        {
            Text = "Event: ",
            Font = new Font("Segoe UI", 12, FontStyle.Bold),
            AutoSize = true,
            Location = new Point(_backButton.Right + 15, 15)
        };

        topPanel.Controls.Add(_backButton);
        topPanel.Controls.Add(_eventLabel);

                Label totalPointsLabel = new Label
        {
            Text = "Total Points",
            Font = new Font("Segoe UI", 12, FontStyle.Bold),
            Dock = DockStyle.Top,
            Padding = new Padding(10, 5, 10, 5),
            Height = 30
        };

                _resultsGrid = new DataGridView
        {
            Dock = DockStyle.Fill,
            AutoGenerateColumns = false,
            AllowUserToAddRows = false,
            AllowUserToDeleteRows = false,
            ReadOnly = true,
            SelectionMode = DataGridViewSelectionMode.FullRowSelect,
            MultiSelect = false,
            AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill
        };

                DataGridViewTextBoxColumn nameColumn = new DataGridViewTextBoxColumn
        {
            Name = "nameColumn",
            HeaderText = "Participant",
            DataPropertyName = "Name",
            FillWeight = 70
        };

        DataGridViewTextBoxColumn totalPointsColumn = new DataGridViewTextBoxColumn
        {
            Name = "totalPointsColumn",
            HeaderText = "Total Points",
            DataPropertyName = "TotalPoints",
            FillWeight = 30
        };

        _resultsGrid.Columns.Add(nameColumn);
        _resultsGrid.Columns.Add(totalPointsColumn);

                this.Controls.Add(_resultsGrid);
        this.Controls.Add(totalPointsLabel);
        this.Controls.Add(topPanel);
    }

    public void LoadEvents()
    {
        try
        {
            List<RefereeDiscipline> assignments = _service.GetRefereeDisciplines(_referee.GetId());

            if (assignments != null && assignments.Count > 0)
            {
                _refereeEvent = assignments[0].GetEvent();
                _eventLabel.Text = $"Event: {_refereeEvent.GetName()}";
                DisplayResults(_refereeEvent);
            }
            else
            {
                _eventLabel.Text = "No assigned events";
                _resultsGrid.Rows.Clear();
            }
        }
        catch (Exception ex)
        {
            ShowError($"Error loading events: {ex.Message}");
        }
    }

    private void DisplayResults(Event @event)
    {
        try
        {
            _resultsGrid.Rows.Clear();

            Dictionary<string, int> participantPoints = new Dictionary<string, int>();
            List<Result> eventResults = _service.GetResultsByEvent(@event.GetId());

            foreach (Result result in eventResults)
            {
                string name = result.GetParticipant().GetName();
                int points = result.GetPoints();

                if (participantPoints.ContainsKey(name))
                {
                    participantPoints[name] += points;
                }
                else
                {
                    participantPoints[name] = points;
                }
            }

            var sortedResults = participantPoints
                .OrderByDescending(entry => entry.Value)
                .ToList();

            foreach (var entry in sortedResults)
            {
                _resultsGrid.Rows.Add(entry.Key, entry.Value);
            }
        }
        catch (Exception ex)
        {
            ShowError($"Error displaying results: {ex.Message}");
        }
    }


    private void BackButton_Click(object sender, EventArgs e)
    {
        this.Close();
    }

    private void ShowError(string message)
    {
        MessageBox.Show(
            message,
            "Error",
            MessageBoxButtons.OK,
            MessageBoxIcon.Error
        );
    }
}