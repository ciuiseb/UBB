﻿using System;
using System.Drawing;
using System.Windows.Forms;

namespace Triathlon.Client.WinForms.Gui
{
    partial class RefereeDashboardForm: Form
    {
        private System.ComponentModel.IContainer components = null;

        // Controls
        private Label titleLabel;
        private Label refereeNameLabel;
        private Label disciplineLabel;
        private Label eventLabel;
        private Button logoutButton;
        private Button updateScoreButton;
        private DataGridView disciplineScoresGrid;
        private DataGridView totalPointsGrid;
        private RadioButton allParticipantsRadio;
        private RadioButton ungradedParticipantsRadio;
        private TextBox scoreField;
        private Label updateScoreLabel;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        private void InitializeComponent()
        {
            this.titleLabel = new System.Windows.Forms.Label();
            this.refereeNameLabel = new System.Windows.Forms.Label();
            this.disciplineLabel = new System.Windows.Forms.Label();
            this.eventLabel = new System.Windows.Forms.Label();
            this.logoutButton = new System.Windows.Forms.Button();
            this.updateScoreButton = new System.Windows.Forms.Button();
            this.disciplineScoresGrid = new System.Windows.Forms.DataGridView();
            this.totalPointsGrid = new System.Windows.Forms.DataGridView();
            this.allParticipantsRadio = new System.Windows.Forms.RadioButton();
            this.ungradedParticipantsRadio = new System.Windows.Forms.RadioButton();
            this.scoreField = new System.Windows.Forms.TextBox();
            this.updateScoreLabel = new System.Windows.Forms.Label();
            this.topPanel = new System.Windows.Forms.Panel();
            this.eventPanel = new System.Windows.Forms.Panel();
            this.eventTextLabel = new System.Windows.Forms.Label();
            this.headerPanel = new System.Windows.Forms.Panel();
            this.refereeTextLabel = new System.Windows.Forms.Label();
            this.disciplineTextLabel = new System.Windows.Forms.Label();
            this.centerPanel = new System.Windows.Forms.Panel();
            this.totalPointsLabel = new System.Windows.Forms.Label();
            this.filterPanel = new System.Windows.Forms.Panel();
            this.participantsLabel = new System.Windows.Forms.Label();
            this.bottomPanel = new System.Windows.Forms.Panel();
            this.dataGridViewTextBoxColumn1 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.dataGridViewTextBoxColumn2 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.dataGridViewTextBoxColumn3 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.dataGridViewTextBoxColumn4 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            ((System.ComponentModel.ISupportInitialize)(this.disciplineScoresGrid)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.totalPointsGrid)).BeginInit();
            this.topPanel.SuspendLayout();
            this.eventPanel.SuspendLayout();
            this.headerPanel.SuspendLayout();
            this.centerPanel.SuspendLayout();
            this.filterPanel.SuspendLayout();
            this.bottomPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // titleLabel
            // 
            this.titleLabel.Location = new System.Drawing.Point(0, 0);
            this.titleLabel.Name = "titleLabel";
            this.titleLabel.Size = new System.Drawing.Size(100, 23);
            this.titleLabel.TabIndex = 0;
            // 
            // refereeNameLabel
            // 
            this.refereeNameLabel.AutoSize = true;
            this.refereeNameLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.refereeNameLabel.Location = new System.Drawing.Point(50, 5);
            this.refereeNameLabel.Name = "refereeNameLabel";
            this.refereeNameLabel.Size = new System.Drawing.Size(0, 15);
            this.refereeNameLabel.TabIndex = 1;
            // 
            // disciplineLabel
            // 
            this.disciplineLabel.AutoSize = true;
            this.disciplineLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.disciplineLabel.Location = new System.Drawing.Point(0, 0);
            this.disciplineLabel.Name = "disciplineLabel";
            this.disciplineLabel.Size = new System.Drawing.Size(119, 15);
            this.disciplineLabel.TabIndex = 3;
            this.disciplineLabel.Text = "Discipline Scores";
            // 
            // eventLabel
            // 
            this.eventLabel.AutoSize = true;
            this.eventLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.eventLabel.Location = new System.Drawing.Point(50, 10);
            this.eventLabel.Name = "eventLabel";
            this.eventLabel.Size = new System.Drawing.Size(0, 17);
            this.eventLabel.TabIndex = 1;
            // 
            // logoutButton
            // 
            this.logoutButton.Location = new System.Drawing.Point(700, 0);
            this.logoutButton.Name = "logoutButton";
            this.logoutButton.Size = new System.Drawing.Size(80, 25);
            this.logoutButton.TabIndex = 4;
            this.logoutButton.Text = "Logout";
            // 
            // updateScoreButton
            // 
            this.updateScoreButton.Location = new System.Drawing.Point(220, 10);
            this.updateScoreButton.Name = "updateScoreButton";
            this.updateScoreButton.Size = new System.Drawing.Size(100, 25);
            this.updateScoreButton.TabIndex = 2;
            this.updateScoreButton.Text = "Update Score";
            // 
            // disciplineScoresGrid
            // 
            this.disciplineScoresGrid.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.disciplineScoresGrid.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.disciplineScoresGrid.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.dataGridViewTextBoxColumn3,
            this.dataGridViewTextBoxColumn4});
            this.disciplineScoresGrid.Location = new System.Drawing.Point(0, 20);
            this.disciplineScoresGrid.MultiSelect = false;
            this.disciplineScoresGrid.Name = "disciplineScoresGrid";
            this.disciplineScoresGrid.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.disciplineScoresGrid.Size = new System.Drawing.Size(780, 200);
            this.disciplineScoresGrid.TabIndex = 4;
            // 
            // totalPointsGrid
            // 
            this.totalPointsGrid.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.totalPointsGrid.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.totalPointsGrid.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.dataGridViewTextBoxColumn1,
            this.dataGridViewTextBoxColumn2});
            this.totalPointsGrid.Location = new System.Drawing.Point(0, 260);
            this.totalPointsGrid.MultiSelect = false;
            this.totalPointsGrid.Name = "totalPointsGrid";
            this.totalPointsGrid.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.totalPointsGrid.Size = new System.Drawing.Size(780, 200);
            this.totalPointsGrid.TabIndex = 6;
            // 
            // allParticipantsRadio
            // 
            this.allParticipantsRadio.AutoSize = true;
            this.allParticipantsRadio.Checked = true;
            this.allParticipantsRadio.Location = new System.Drawing.Point(450, 8);
            this.allParticipantsRadio.Name = "allParticipantsRadio";
            this.allParticipantsRadio.Size = new System.Drawing.Size(104, 19);
            this.allParticipantsRadio.TabIndex = 1;
            this.allParticipantsRadio.TabStop = true;
            this.allParticipantsRadio.Text = "All Participants";
            // 
            // ungradedParticipantsRadio
            // 
            this.ungradedParticipantsRadio.AutoSize = true;
            this.ungradedParticipantsRadio.Location = new System.Drawing.Point(570, 8);
            this.ungradedParticipantsRadio.Name = "ungradedParticipantsRadio";
            this.ungradedParticipantsRadio.Size = new System.Drawing.Size(142, 19);
            this.ungradedParticipantsRadio.TabIndex = 2;
            this.ungradedParticipantsRadio.Text = "Ungraded Participants";
            // 
            // scoreField
            // 
            this.scoreField.Location = new System.Drawing.Point(100, 12);
            this.scoreField.Name = "scoreField";
            this.scoreField.PlaceholderText = "Enter score";
            this.scoreField.Size = new System.Drawing.Size(100, 23);
            this.scoreField.TabIndex = 1;
            // 
            // updateScoreLabel
            // 
            this.updateScoreLabel.AutoSize = true;
            this.updateScoreLabel.Location = new System.Drawing.Point(0, 15);
            this.updateScoreLabel.Name = "updateScoreLabel";
            this.updateScoreLabel.Size = new System.Drawing.Size(80, 15);
            this.updateScoreLabel.TabIndex = 0;
            this.updateScoreLabel.Text = "Update Score:";
            // 
            // topPanel
            // 
            this.topPanel.Controls.Add(this.eventPanel);
            this.topPanel.Controls.Add(this.headerPanel);
            this.topPanel.Dock = System.Windows.Forms.DockStyle.Top;
            this.topPanel.Location = new System.Drawing.Point(10, 10);
            this.topPanel.Name = "topPanel";
            this.topPanel.Size = new System.Drawing.Size(986, 70);
            this.topPanel.TabIndex = 2;
            // 
            // eventPanel
            // 
            this.eventPanel.Controls.Add(this.eventTextLabel);
            this.eventPanel.Controls.Add(this.eventLabel);
            this.eventPanel.Dock = System.Windows.Forms.DockStyle.Top;
            this.eventPanel.Location = new System.Drawing.Point(0, 30);
            this.eventPanel.Name = "eventPanel";
            this.eventPanel.Size = new System.Drawing.Size(986, 40);
            this.eventPanel.TabIndex = 0;
            // 
            // eventTextLabel
            // 
            this.eventTextLabel.AutoSize = true;
            this.eventTextLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.eventTextLabel.Location = new System.Drawing.Point(0, 10);
            this.eventTextLabel.Name = "eventTextLabel";
            this.eventTextLabel.Size = new System.Drawing.Size(48, 17);
            this.eventTextLabel.TabIndex = 0;
            this.eventTextLabel.Text = "Event:";
            // 
            // headerPanel
            // 
            this.headerPanel.Controls.Add(this.refereeTextLabel);
            this.headerPanel.Controls.Add(this.refereeNameLabel);
            this.headerPanel.Controls.Add(this.disciplineTextLabel);
            this.headerPanel.Controls.Add(this.logoutButton);
            this.headerPanel.Dock = System.Windows.Forms.DockStyle.Top;
            this.headerPanel.Location = new System.Drawing.Point(0, 0);
            this.headerPanel.Name = "headerPanel";
            this.headerPanel.Size = new System.Drawing.Size(986, 30);
            this.headerPanel.TabIndex = 1;
            // 
            // refereeTextLabel
            // 
            this.refereeTextLabel.AutoSize = true;
            this.refereeTextLabel.Location = new System.Drawing.Point(0, 5);
            this.refereeTextLabel.Name = "refereeTextLabel";
            this.refereeTextLabel.Size = new System.Drawing.Size(49, 15);
            this.refereeTextLabel.TabIndex = 0;
            this.refereeTextLabel.Text = "Referee:";
            // 
            // disciplineTextLabel
            // 
            this.disciplineTextLabel.AutoSize = true;
            this.disciplineTextLabel.Location = new System.Drawing.Point(250, 5);
            this.disciplineTextLabel.Name = "disciplineTextLabel";
            this.disciplineTextLabel.Size = new System.Drawing.Size(112, 15);
            this.disciplineTextLabel.TabIndex = 2;
            this.disciplineTextLabel.Text = "Assigned Discipline:";
            // 
            // centerPanel
            // 
            this.centerPanel.Controls.Add(this.disciplineLabel);
            this.centerPanel.Controls.Add(this.disciplineScoresGrid);
            this.centerPanel.Controls.Add(this.totalPointsLabel);
            this.centerPanel.Controls.Add(this.totalPointsGrid);
            this.centerPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.centerPanel.Location = new System.Drawing.Point(10, 115);
            this.centerPanel.Name = "centerPanel";
            this.centerPanel.Size = new System.Drawing.Size(986, 425);
            this.centerPanel.TabIndex = 0;
            // 
            // totalPointsLabel
            // 
            this.totalPointsLabel.AutoSize = true;
            this.totalPointsLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.totalPointsLabel.Location = new System.Drawing.Point(0, 240);
            this.totalPointsLabel.Name = "totalPointsLabel";
            this.totalPointsLabel.Size = new System.Drawing.Size(83, 15);
            this.totalPointsLabel.TabIndex = 5;
            this.totalPointsLabel.Text = "Total Points";
            // 
            // filterPanel
            // 
            this.filterPanel.Controls.Add(this.participantsLabel);
            this.filterPanel.Controls.Add(this.allParticipantsRadio);
            this.filterPanel.Controls.Add(this.ungradedParticipantsRadio);
            this.filterPanel.Dock = System.Windows.Forms.DockStyle.Top;
            this.filterPanel.Location = new System.Drawing.Point(10, 80);
            this.filterPanel.Name = "filterPanel";
            this.filterPanel.Size = new System.Drawing.Size(986, 35);
            this.filterPanel.TabIndex = 1;
            // 
            // participantsLabel
            // 
            this.participantsLabel.AutoSize = true;
            this.participantsLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.participantsLabel.Location = new System.Drawing.Point(0, 8);
            this.participantsLabel.Name = "participantsLabel";
            this.participantsLabel.Size = new System.Drawing.Size(181, 17);
            this.participantsLabel.TabIndex = 0;
            this.participantsLabel.Text = "Participants and Scores";
            // 
            // bottomPanel
            // 
            this.bottomPanel.Controls.Add(this.updateScoreLabel);
            this.bottomPanel.Controls.Add(this.scoreField);
            this.bottomPanel.Controls.Add(this.updateScoreButton);
            this.bottomPanel.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.bottomPanel.Location = new System.Drawing.Point(10, 540);
            this.bottomPanel.Name = "bottomPanel";
            this.bottomPanel.Size = new System.Drawing.Size(986, 50);
            this.bottomPanel.TabIndex = 3;
            // 
            // dataGridViewTextBoxColumn1
            // 
            this.dataGridViewTextBoxColumn1.HeaderText = "Participant";
            this.dataGridViewTextBoxColumn1.Name = "dataGridViewTextBoxColumn1";
            // 
            // dataGridViewTextBoxColumn2
            // 
            this.dataGridViewTextBoxColumn2.HeaderText = "Total Points";
            this.dataGridViewTextBoxColumn2.Name = "dataGridViewTextBoxColumn2";
            // 
            // dataGridViewTextBoxColumn3
            // 
            this.dataGridViewTextBoxColumn3.HeaderText = "Participant Name";
            this.dataGridViewTextBoxColumn3.Name = "dataGridViewTextBoxColumn3";
            // 
            // dataGridViewTextBoxColumn4
            // 
            this.dataGridViewTextBoxColumn4.HeaderText = "Discipline Score";
            this.dataGridViewTextBoxColumn4.Name = "dataGridViewTextBoxColumn4";
            // 
            // RefereeDashboardForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1006, 600);
            this.Controls.Add(this.centerPanel);
            this.Controls.Add(this.filterPanel);
            this.Controls.Add(this.topPanel);
            this.Controls.Add(this.bottomPanel);
            this.Name = "RefereeDashboardForm";
            this.Padding = new System.Windows.Forms.Padding(10);
            this.Text = "Referee Dashboard";
            ((System.ComponentModel.ISupportInitialize)(this.disciplineScoresGrid)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.totalPointsGrid)).EndInit();
            this.topPanel.ResumeLayout(false);
            this.eventPanel.ResumeLayout(false);
            this.eventPanel.PerformLayout();
            this.headerPanel.ResumeLayout(false);
            this.headerPanel.PerformLayout();
            this.centerPanel.ResumeLayout(false);
            this.centerPanel.PerformLayout();
            this.filterPanel.ResumeLayout(false);
            this.filterPanel.PerformLayout();
            this.bottomPanel.ResumeLayout(false);
            this.bottomPanel.PerformLayout();
            this.ResumeLayout(false);

        }

        private DataGridViewTextBoxColumn dataGridViewTextBoxColumn3;
        private DataGridViewTextBoxColumn dataGridViewTextBoxColumn4;
        private DataGridViewTextBoxColumn dataGridViewTextBoxColumn1;
        private DataGridViewTextBoxColumn dataGridViewTextBoxColumn2;
        private Panel topPanel;
        private Panel eventPanel;
        private Label eventTextLabel;
        private Panel headerPanel;
        private Label refereeTextLabel;
        private Label disciplineTextLabel;
        private Panel centerPanel;
        private Label totalPointsLabel;
        private Panel filterPanel;
        private Label participantsLabel;
        private Panel bottomPanel;
    }
}
