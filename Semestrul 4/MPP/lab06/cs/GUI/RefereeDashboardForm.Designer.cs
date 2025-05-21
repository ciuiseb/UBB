using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;
using cs.Domain;
using cs.Service;

namespace cs.GUI
{
    public partial class RefereeDashboardForm : Form
    {
        private readonly IService _service;
        private readonly Referee _referee;
        private RefereeDiscipline _currentAssignment;
        private List<Result> _allResults = new List<Result>();

        private Label _disciplineLabel;
        private Button _reportButton;
        private DataGridView _resultsGrid;
        private RadioButton _allParticipantsRadio;
        private RadioButton _ungradedParticipantsRadio;

        public RefereeDashboardForm(IService service, Referee referee)
        {
            _service = service;
            _referee = referee;
            InitializeComponent();
            LoadRefereeAssignment();
        }

        private void InitializeComponent()
        {
            this.Text = "Referee Dashboard";
            this.Size = new Size(800, 600);
            this.StartPosition = FormStartPosition.CenterScreen;
            this.FormBorderStyle = FormBorderStyle.Sizable;
            this.MinimumSize = new Size(800, 600);

            Panel topPanel = new Panel
            {
                Dock = DockStyle.Top,
                Height = 50,
                Padding = new Padding(10, 0, 10, 0)
            };

            Label assignedLabel = new Label
            {
                Text = "Assigned Discipline:",
                Font = new Font("Segoe UI", 12),
                AutoSize = true,
                Location = new Point(10, 15)
            };

            _disciplineLabel = new Label
            {
                Text = "Loading...",
                Font = new Font("Segoe UI", 12, FontStyle.Bold),
                AutoSize = true,
                Location = new Point(assignedLabel.Right + 10, 15)
            };

            _reportButton = new Button
            {
                Text = "View Event Report",
                Size = new Size(150, 30),
                Location = new Point(600, 10),  
                Anchor = AnchorStyles.Top | AnchorStyles.Right  
            };
            _reportButton.Click += ReportButton_Click;

            topPanel.Controls.Add(assignedLabel);
            topPanel.Controls.Add(_disciplineLabel);
            topPanel.Controls.Add(_reportButton);

                        Panel filterPanel = new Panel
            {
                Dock = DockStyle.Top,
                Height = 50,
                Padding = new Padding(10, 0, 10, 0),
                BorderStyle = BorderStyle.FixedSingle              };

            Label updateScoresLabel = new Label
            {
                Text = "Update Scores",
                Font = new Font("Segoe UI", 14, FontStyle.Bold),
                Location = new Point(10, 15),
                AutoSize = true
            };

            _allParticipantsRadio = new RadioButton
            {
                Text = "All Participants",
                Checked = true,
                Location = new Point(350, 15),
                AutoSize = true
            };

            _ungradedParticipantsRadio = new RadioButton
            {
                Text = "Ungraded Participants",
                Location = new Point(500, 15),
                AutoSize = true
            };

                        _allParticipantsRadio.CheckedChanged += FilterRadio_CheckedChanged;
            _ungradedParticipantsRadio.CheckedChanged += FilterRadio_CheckedChanged;

                        filterPanel.Controls.Add(updateScoresLabel);
            filterPanel.Controls.Add(_allParticipantsRadio);
            filterPanel.Controls.Add(_ungradedParticipantsRadio);

                        _resultsGrid = new DataGridView
            {
                Dock = DockStyle.Fill,
                AutoGenerateColumns = false,
                AllowUserToAddRows = false,
                AllowUserToDeleteRows = false,
                SelectionMode = DataGridViewSelectionMode.FullRowSelect,
                MultiSelect = false,
                EditMode = DataGridViewEditMode.EditOnEnter,
                AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill
            };

                        DataGridViewTextBoxColumn participantColumn = new DataGridViewTextBoxColumn
            {
                Name = "participantColumn",
                HeaderText = "Participant Name",
                DataPropertyName = "ParticipantName",
                ReadOnly = true,
                FillWeight = 70
            };

            DataGridViewTextBoxColumn scoreColumn = new DataGridViewTextBoxColumn
            {
                Name = "scoreColumn",
                HeaderText = "Score",
                DataPropertyName = "Points",
                ReadOnly = false,
                FillWeight = 30
            };

            _resultsGrid.Columns.Add(participantColumn);
            _resultsGrid.Columns.Add(scoreColumn);
            _resultsGrid.CellEndEdit += ResultsGrid_CellEndEdit;

                        this.Controls.Add(_resultsGrid);                 this.Controls.Add(filterPanel);                  this.Controls.Add(topPanel);         
                        _reportButton.BringToFront();

            this.Shown += (s, e) => { this.Width++; this.Width--; };



        }

        private void ResultsGrid_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            if (e.ColumnIndex == 1)             {
                try
                {
                    DataGridViewRow row = _resultsGrid.Rows[e.RowIndex];
                    Result result = row.Tag as Result;

                    if (result != null && int.TryParse(row.Cells[1].Value?.ToString(), out int newScore))
                    {
                        result.SetPoints(newScore);
                        _service.UpdateResult(result);
                    }
                }
                catch (Exception ex)
                {
                    ShowError($"Error updating score: {ex.Message}");
                }
            }
        }

        private void FilterRadio_CheckedChanged(object sender, EventArgs e)
        {
            UpdateFilter();
        }

        private void UpdateFilter()
        {
            if (_allResults == null) return;

            _resultsGrid.Rows.Clear();

            IEnumerable<Result> filteredResults = _allResults;
            if (_ungradedParticipantsRadio.Checked)
            {
                filteredResults = _allResults.Where(r => r.GetPoints() <= 0);
            }

            foreach (Result result in filteredResults)
            {
                int rowIndex = _resultsGrid.Rows.Add(
                    result.GetParticipant().GetName(),
                    result.GetPoints()
                );
                _resultsGrid.Rows[rowIndex].Tag = result;
            }
        }

        private void ReportButton_Click(object sender, EventArgs e)
        {
            try
            {
                ReportForm reportForm = new ReportForm(_service, _referee);
                reportForm.LoadEvents();

                this.Hide();
                reportForm.ShowDialog();
                this.Show();
            }
            catch (Exception ex)
            {
                ShowError($"Could not open Report view: {ex.Message}");
            }
        }

        private void LoadRefereeAssignment()
        {
            try
            {
                List<RefereeDiscipline> assignments = _service.GetRefereeDisciplines(_referee.GetId());

                if (assignments != null && assignments.Count > 0)
                {
                    _currentAssignment = assignments[0];

                    Event @event = _currentAssignment.GetEvent();
                    Discipline discipline = _currentAssignment.GetDiscipline();

                    _disciplineLabel.Text = $"{@event.GetName()} - {discipline}";

                    LoadResults(@event, discipline);
                }
                else
                {
                    _disciplineLabel.Text = "No assignment";
                    _resultsGrid.Rows.Clear();
                    _allResults.Clear();
                }
            }
            catch (Exception ex)
            {
                ShowError($"Error loading assignments: {ex.Message}");
            }
        }

        private void LoadResults(Event @event, Discipline discipline)
        {
            try
            {
                List<Result> eventResults = _service.GetResultsByEvent(@event.GetId());

                _allResults = eventResults
                    .Where(r => r.GetDiscipline() == discipline)
                    .ToList();

                UpdateFilter();
            }
            catch (Exception ex)
            {
                ShowError($"Error loading results: {ex.Message}");
            }
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

                protected override void OnResize(EventArgs e)
        {
            base.OnResize(e);
            if (_reportButton != null)
            {
                _reportButton.Left = this.ClientSize.Width - _reportButton.Width - 20;
            }
        }
    }
}