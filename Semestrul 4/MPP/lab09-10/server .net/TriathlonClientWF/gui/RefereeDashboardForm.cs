using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using Triathlon.Model;
using Triathlon.Services;
using Triathlon.Client.WinForms.Utils;

namespace Triathlon.Client.WinForms.Gui;

public partial class RefereeDashboardForm : Form, ITriathlonObserver
{
    // Services and data
    private ITriathlonServices server;
    private Referee referee;
    private Event currentEvent;
    private Discipline currentDiscipline;
    private RefereeDiscipline currentAssignment;

    // Data collections
    private BindingList<ResultViewModel> disciplineResults;
    private BindingList<TotalPointsViewModel> totalPointsResults;
    private BindingSource disciplineDataSource;
    private BindingSource totalPointsDataSource;

    // View models
    public class ResultViewModel
    {
        public string ParticipantName { get; set; }
        public int DisciplineScore { get; set; }
        public Result Result { get; set; }
    }

    public class TotalPointsViewModel
    {
        public string ParticipantName { get; set; }
        public int TotalPoints { get; set; }
        public Participant Participant { get; set; }
    }

    public RefereeDashboardForm()
    {
        InitializeComponent();
        Initialize();
    }

    private void Initialize()
    {
        // Initialize binding lists and data sources
        disciplineResults = new BindingList<ResultViewModel>();
        totalPointsResults = new BindingList<TotalPointsViewModel>();
        disciplineDataSource = new BindingSource(disciplineResults, null);
        totalPointsDataSource = new BindingSource(totalPointsResults, null);

        // Important: Set AutoGenerateColumns to false BEFORE setting the DataSource
        disciplineScoresGrid.AutoGenerateColumns = false;
        totalPointsGrid.AutoGenerateColumns = false;

        // Set up the columns manually
        disciplineScoresGrid.Columns.Clear();
        disciplineScoresGrid.Columns.Add("ParticipantName", "Participant Name");
        disciplineScoresGrid.Columns.Add("DisciplineScore", "Discipline Score");

        // Set the DataPropertyName for each column
        disciplineScoresGrid.Columns["ParticipantName"].DataPropertyName = "ParticipantName";
        disciplineScoresGrid.Columns["DisciplineScore"].DataPropertyName = "DisciplineScore";

        // Do the same for totalPointsGrid
        totalPointsGrid.Columns.Clear();
        totalPointsGrid.Columns.Add("ParticipantName", "Participant Name");
        totalPointsGrid.Columns.Add("TotalPoints", "Total Points");
        totalPointsGrid.Columns["ParticipantName"].DataPropertyName = "ParticipantName";
        totalPointsGrid.Columns["TotalPoints"].DataPropertyName = "TotalPoints";

        // Now set the data sources
        disciplineScoresGrid.DataSource = disciplineDataSource;
        totalPointsGrid.DataSource = totalPointsDataSource;

        // Setup event handlers
        updateScoreButton.Click += UpdateSelectedResult;
        logoutButton.Click += Logout;
        allParticipantsRadio.CheckedChanged += FilterParticipants;
        ungradedParticipantsRadio.CheckedChanged += FilterParticipants;
        disciplineScoresGrid.SelectionChanged += DisciplineScoresGrid_SelectionChanged;
    }

    public void SetServer(ITriathlonServices server)
    {
        this.server = server;
    }

    public void SetAssignment(RefereeDiscipline assignment)
    {
        this.currentAssignment = assignment;
        this.referee = assignment.Referee;
        this.currentEvent = assignment.Event;
        this.currentDiscipline = assignment.Discipline;

        // Update UI labels
        refereeNameLabel.Text = referee.Username;
        eventLabel.Text = currentEvent.Name;
        disciplineLabel.Text = currentDiscipline.ToString();

        LoadResults();
    }

    private async void LoadResults()
    {
        try
        {
            if (currentEvent == null) return;

            // Remember selected item
            ResultViewModel selectedResult = disciplineScoresGrid.CurrentRow?.DataBoundItem as ResultViewModel;

            // Get fresh data from server
            List<Result> allResults = await Task.Run(() => server.GetResultsByEvent(currentEvent.Id));

            // Update discipline scores
            var disciplineResultsList = allResults
                .Where(r => r.Discipline == currentDiscipline)
                .Select(r => new ResultViewModel
                {
                    ParticipantName = r.Participant.Name,
                    DisciplineScore = r.Points,
                    Result = r
                })
                .ToList();

            disciplineResults.Clear();
            foreach (var item in disciplineResultsList)
            {
                disciplineResults.Add(item);
            }

            // Update total points
            var uniqueParticipants = allResults
                .GroupBy(r => r.Participant.Id)
                .Select(g => g.First().Participant)
                .ToList();

            totalPointsResults.Clear();
            foreach (var participant in uniqueParticipants)
            {
                var totalPoints = await Task.Run(() => CalculateTotalPoints(participant));
                totalPointsResults.Add(new TotalPointsViewModel
                {
                    ParticipantName = participant.Name,
                    TotalPoints = totalPoints,
                    Participant = participant
                });
            }

            // Restore selection
            if (selectedResult != null)
            {
                for (int i = 0; i < disciplineResults.Count; i++)
                {
                    if (disciplineResults[i].Result.Id == selectedResult.Result.Id)
                    {
                        disciplineScoresGrid.Rows[i].Selected = true;
                        disciplineScoresGrid.CurrentCell = disciplineScoresGrid.Rows[i].Cells[0];
                        break;
                    }
                }
            }
            else if (disciplineResults.Count > 0)
            {
                disciplineScoresGrid.Rows[0].Selected = true;
                disciplineScoresGrid.CurrentCell = disciplineScoresGrid.Rows[0].Cells[0];
            }

            // Apply filter
            FilterParticipants(null, null);
        }
        catch (Exception e)
        {
            MessageUtil.ShowError($"Error loading results: {e.Message}");
        }
    }

    private int CalculateTotalPoints(Participant participant)
    {
        try
        {
            return (currentEvent == null) ? 0 :
                server.GetTotalPointsForParticipant(participant.Id, currentEvent.Id);
        }
        catch (Exception)
        {
            return 0;
        }
    }

    private void FilterParticipants(object sender, EventArgs e)
    {
        if (allParticipantsRadio.Checked)
        {
            disciplineDataSource.Filter = null;
        }
        else if (ungradedParticipantsRadio.Checked)
        {
            // Note: BindingSource filtering requires string format
            disciplineDataSource.Filter = "DisciplineScore <= 0";
        }

        disciplineScoresGrid.Refresh();
    }

    private void DisciplineScoresGrid_SelectionChanged(object sender, EventArgs e)
    {
        if (disciplineScoresGrid.CurrentRow?.DataBoundItem is ResultViewModel result)
        {
            scoreField.Text = result.DisciplineScore.ToString();
        }
    }

    private async void UpdateSelectedResult(object sender, EventArgs e)
    {
        // Add debugging to verify this is being called
        Console.WriteLine("Update button clicked");

        if (!(disciplineScoresGrid.CurrentRow?.DataBoundItem is ResultViewModel selected))
        {
            MessageUtil.ShowError("Please select a result to update");
            return;
        }

        try
        {
            string scoreText = scoreField.Text.Trim();
            if (string.IsNullOrEmpty(scoreText))
            {
                MessageUtil.ShowError("Please enter a score");
                return;
            }

            if (!int.TryParse(scoreText, out int newPoints))
            {
                MessageUtil.ShowError("Please enter a valid score");
                return;
            }

            // Add debugging to verify the values
            Console.WriteLine($"Updating score for {selected.ParticipantName} to {newPoints}");

            Result updatedResult = new Result(
                    selected.Result.Id,
                    selected.Result.Event,
                    selected.Result.Participant,
                    selected.Result.Discipline,
                    newPoints
            );

            await Task.Run(() => server.UpdateResult(updatedResult));

           
            MessageUtil.ShowAlert("Success", "Result updated successfully");
        }
        catch (Exception ex)
        {
            MessageUtil.ShowError($"Error updating result: {ex.Message}");
        }
    }

    private async void Logout(object sender, EventArgs e)
    {
        try
        {
            await Task.Run(() => server.Logout(referee, this));
            Application.Exit();
        }
        catch (TriathlonException ex)
        {
            MessageUtil.ShowError($"Error during logout: {ex.Message}");
        }
    }

    // ITriathlonObserver implementation
    public void ResultsUpdated(Result result)
    {
        // Update UI on the UI thread
        if (InvokeRequired)
        {
            Invoke(new Action(() => ActualResultsUpdate(result)));
        }
        else
        {
            ActualResultsUpdate(result);
        }
    }

    private void ActualResultsUpdate(Result result)
    {
        try
        {
            if (currentEvent != null &&
                result.Event.Id == currentEvent.Id)
            {
                    LoadResults();

                    // MessageUtil.ShowAlert("Update Notification", $"Result updated: {result.Participant.Name} - {result.Points} points");                
            }
        }
        catch (Exception e)
        {
            System.Diagnostics.Debug.WriteLine($"Error refreshing results: {e.Message}");
        }
    }

}