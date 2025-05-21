using System;
using System.Data;
using System.Windows.Forms;
using Npgsql;
using System.Configuration;
using System.Globalization;

namespace ParentsChildren;

public partial class MainForm : Form
{   
    private readonly string _connectionString = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
    private int _selectedParentPK = -1; 
    private int _selectedChildPK = -1;
    private readonly string parentTable = ConfigurationManager.AppSettings.Get("parentTable");
    private readonly string childTable = ConfigurationManager.AppSettings.Get("childTable");
    private readonly string parentPK = ConfigurationManager.AppSettings.Get("parentPK");
    private readonly string childPK = ConfigurationManager.AppSettings.Get("childPK");
    private readonly string childFK = ConfigurationManager.AppSettings.Get("childFK");

    private readonly string parentAsSingularNoum = ConfigurationManager.AppSettings.Get("parentAsSingularNoum");
    private readonly string parentAsPluralNoum = ConfigurationManager.AppSettings.Get("parentAsPluralNoum");
    private readonly string childAsSingularNoum = ConfigurationManager.AppSettings.Get("childAsSingularNoum");
    private readonly string childAsPluralNoum = ConfigurationManager.AppSettings.Get("childAsPluralNoum");

    public MainForm()
    {
        InitializeComponent();
        LoadParents();
    }

    private void InitializeComponent()
    {
        this.Text = parentAsSingularNoum + " " + childAsPluralNoum;
        this.Size = new Size(800, 600);

        dgvParents = new DataGridView
        {
            Location = new Point(10, 30),
            Size = new Size(760, 180),
            ReadOnly = true,
            SelectionMode = DataGridViewSelectionMode.FullRowSelect,
            MultiSelect = false,
            AllowUserToAddRows = false,
            AllowUserToDeleteRows = false,
            AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill
        };
        dgvParents.CellClick += DgvParents_CellClick;

        dgvChildren = new DataGridView
        {
            Location = new Point(10, 240),
            Size = new Size(760, 250),
            ReadOnly = true,
            SelectionMode = DataGridViewSelectionMode.FullRowSelect,
            MultiSelect = false,
            AllowUserToAddRows = false,
            AllowUserToDeleteRows = false,
            AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill
        };
        dgvChildren.CellClick += DgvChildren_CellClick;
        lblParents = new Label { Text = parentAsPluralNoum + ":", AutoSize = true, Location = new Point(10, 10) };
        lblChildren = new Label { Text = childAsPluralNoum + ":", AutoSize = true, Location = new Point(10, 220) };

        btnAddChild = new Button { Text = "Add " + childAsSingularNoum, Enabled = false, Size = new Size(100, 30), Location = new Point(10, 10) };
        btnAddChild.Click += BtnAddChild_Click;

        btnUpdateChild = new Button { Text = "Update " + childAsSingularNoum, Enabled = false, Size = new Size(120, 30), Location = new Point(120, 10) };
        btnUpdateChild.Click += BtnUpdateChild_Click;

        btnDeleteChild = new Button { Text = "Delete " + childAsSingularNoum, Enabled = false, Size = new Size(120, 30), Location = new Point(250, 10) };
        btnDeleteChild.Click += BtnDeleteChild_Click;

        Panel buttonPanel = new Panel { Dock = DockStyle.Bottom, Height = 50, Padding = new Padding(5) };
        buttonPanel.Controls.AddRange(new Control[] { btnAddChild, btnUpdateChild, btnDeleteChild });

        this.Controls.AddRange(new Control[] { lblParents, dgvParents, lblChildren, dgvChildren, buttonPanel });

    }
    private DataGridView dgvParents;
    private DataGridView dgvChildren;
    private Label lblParents;
    private Label lblChildren;
    private Button btnAddChild;
    private Button btnUpdateChild;
    private Button btnDeleteChild;

    private void LoadParents()
    {
        try
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {   
                connection.Open();
                string query = $"SELECT * FROM {parentTable} ORDER BY {parentPK}";
                using (NpgsqlDataAdapter adapter = new NpgsqlDataAdapter(query, connection))
                {
                    DataTable table = new DataTable();
                    adapter.Fill(table);
                    dgvParents.DataSource = table;

                    foreach (DataColumn column in table.Columns)
                    {
                        if (dgvParents.Columns[column.ColumnName] != null)
                        {
                            string headerText = TransformColumnNameToHeader(column.ColumnName);
                            dgvParents.Columns[column.ColumnName].HeaderText = headerText;
                        }
                    }
;
                }
            }
        }
        catch (Exception ex)
        { 
            MessageBox.Show("Error loading " + parentAsPluralNoum + ": " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void DgvParents_CellClick(object sender, DataGridViewCellEventArgs e)
    {
        if (e.RowIndex >= 0)
        {
            _selectedParentPK = Convert.ToInt32(dgvParents.Rows[e.RowIndex].Cells["id"].Value);
            btnAddChild.Enabled = true;
            LoadChildren(_selectedParentPK);
            _selectedChildPK = -1;
            btnUpdateChild.Enabled = false;
            btnDeleteChild.Enabled = false;
        }
    }

    private void LoadChildren(int parentPK)
    {
        try
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {   
                connection.Open();
                string query = $"SELECT * FROM {childTable} WHERE {childFK} = @parentId ORDER BY name";
                using (NpgsqlCommand cmd = new NpgsqlCommand(query, connection))
                {
                    cmd.Parameters.AddWithValue("@parentId", _selectedParentPK);
                    using (NpgsqlDataAdapter adapter = new NpgsqlDataAdapter(cmd))
                    {
                        DataTable table = new DataTable();
                        adapter.Fill(table);
                        dgvChildren.DataSource = table;

                        foreach (DataColumn column in table.Columns)
                        {
                            if (dgvChildren.Columns[column.ColumnName] != null)
                            {
                                string headerText = TransformColumnNameToHeader(column.ColumnName);
                                dgvChildren.Columns[column.ColumnName].HeaderText = headerText;
                            }
                        }

                    }
                }
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error loading" + childAsPluralNoum + ": "  + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void DgvChildren_CellClick(object sender, DataGridViewCellEventArgs e)
    {
        if (e.RowIndex >= 0)
        {  
            _selectedChildPK = Convert.ToInt32(dgvChildren.Rows[e.RowIndex].Cells[childPK].Value);
            btnUpdateChild.Enabled = true;
            btnDeleteChild.Enabled = true;
        }
    }
    private void BtnAddChild_Click(object sender, EventArgs e)
    {
        try
        {
            using (ChildForm productForm = new ChildForm(_selectedParentPK))
            {
                if (productForm.ShowDialog() == DialogResult.OK)
                    LoadChildren(_selectedParentPK);
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error adding" + childAsSingularNoum+": " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void BtnUpdateChild_Click(object sender, EventArgs e)
    {
        try
        {
            if (dgvChildren.SelectedRows.Count == 0)
            {
                MessageBox.Show($"Please select a {childAsSingularNoum} to update.", "Selection Required", MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            DataGridViewRow row = dgvChildren.SelectedRows[0];

            Dictionary<string, object> values = new Dictionary<string, object>();

            foreach (DataGridViewColumn column in dgvChildren.Columns)
            {
                string columnName = column.Name;
                if (columnName != childPK && columnName != childFK)
                {
                    values[columnName] = row.Cells[columnName].Value;
                }
            }

            using (ChildForm childForm = new ChildForm(_selectedParentPK, _selectedChildPK, values))
            {
                if (childForm.ShowDialog() == DialogResult.OK)
                    LoadChildren(_selectedParentPK);
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error updating {childAsSingularNoum}: {ex.Message}",
                "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void BtnDeleteChild_Click(object sender, EventArgs e)
    {
        try
        {
            if (MessageBox.Show($"Are you sure you want to delete this {childAsSingularNoum}?", "Confirm",
                MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
            {
                using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
                {
                    connection.Open();

                    string query = $"DELETE FROM {childTable} WHERE {childPK} = @id";

                    using (NpgsqlCommand cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", _selectedChildPK);
                        int rowsAffected = cmd.ExecuteNonQuery();

                        if (rowsAffected > 0)
                        {
                            MessageBox.Show($"{childAsSingularNoum} deleted successfully.",
                                "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        }
                        else
                        {
                            MessageBox.Show($"No {childAsSingularNoum} was deleted. The record may have been removed already.",
                                "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                        }

                        _selectedChildPK = -1;
                        btnUpdateChild.Enabled = false;
                        btnDeleteChild.Enabled = false;
                        LoadChildren(_selectedParentPK);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error deleting {childAsSingularNoum}: {ex.Message}",
                "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }
    private string TransformColumnNameToHeader(string columnName)
    {
        string result = columnName.Replace("_", " ");

        TextInfo textInfo = new CultureInfo("en-US", false).TextInfo;
        result = textInfo.ToTitleCase(result);

        return result;
    }

}

public class ChildForm : Form
{   
    private readonly string _connectionString = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
    private readonly int _parentPKValue;
    private readonly int _childPKValue;

    private readonly Dictionary<string, Control> _controls = new Dictionary<string, Control>();

    private Button btnSave;
    private Button btnCancel;

    private readonly string childTable = ConfigurationManager.AppSettings.Get("childTable");
    private readonly string childPK = ConfigurationManager.AppSettings.Get("childPK");
    private readonly string childFK = ConfigurationManager.AppSettings.Get("childFK");

    private readonly string childAsSingularNoum = ConfigurationManager.AppSettings.Get("childAsSingularNoum");

    public ChildForm(int parentPK)
    {
        _parentPKValue = parentPK;
        _childPKValue = -1;
        CreateFormControls();
        InitializeComponent();

        this.Text = $"Add New {childAsSingularNoum}";
    }

    public ChildForm(int parentPK, int childPK, Dictionary<string, object> values)
    {
        _parentPKValue = parentPK;
        _childPKValue = childPK;
        CreateFormControls();
        InitializeComponent();
        this.Text = $"Update {childAsSingularNoum}";

        foreach(var item in values)
        {
            if (_controls.ContainsKey(item.Key))
            {
                if (_controls[item.Key] is TextBox textBox)
                {
                    textBox.Text = item.Value?.ToString() ?? "";
                }
                else if (_controls[item.Key] is NumericUpDown numUpDown && item.Value != null)
                {
                    if (decimal.TryParse(item.Value.ToString(), out decimal value))
                    {
                        numUpDown.Value = Math.Min(numUpDown.Maximum, Math.Max(numUpDown.Minimum, value));
                    }
                }
                else if (_controls[item.Key] is CheckBox checkBox && item.Value != null)
                {
                    if (bool.TryParse(item.Value.ToString(), out bool value))
                    {
                        checkBox.Checked = value;
                    }
                }
            }
        }
    }

    private void InitializeComponent()
    {
        this.SuspendLayout();

        int height = 80 + (_controls.Count * 30) + 60;
        this.Size = new Size(400, Math.Max(200, height));
        this.FormBorderStyle = FormBorderStyle.FixedDialog;
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.StartPosition = FormStartPosition.CenterParent;

        int yPos = 20;

        foreach (var entry in _controls)
        {
            string displayName = entry.Key.Replace("_", " ");
            displayName = System.Globalization.CultureInfo.CurrentCulture.TextInfo.ToTitleCase(displayName);

            Label label = new Label
            {
                Text = displayName + ":",
                Location = new Point(20, yPos),
                AutoSize = true
            };

            Control control = entry.Value;
            control.Location = new Point(140, yPos);

            this.Controls.Add(label);
            this.Controls.Add(control);

            yPos += 30;
        }

        btnSave = new Button
        {
            Text = "Save",
            DialogResult = DialogResult.OK,
            Location = new Point(140, yPos + 10),
            Size = new Size(80, 30)
        };
        btnSave.Click += BtnSave_Click;

        btnCancel = new Button
        {
            Text = "Cancel",
            DialogResult = DialogResult.Cancel,
            Location = new Point(230, yPos + 10),
            Size = new Size(80, 30)
        };

        this.Controls.Add(btnSave);
        this.Controls.Add(btnCancel);

        this.AcceptButton = btnSave;
        this.CancelButton = btnCancel;

        this.ResumeLayout(false);
    }
    private void CreateFormControls()
    {
        try
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {
                connection.Open();

                string query = @"
                    SELECT column_name, data_type 
                    FROM information_schema.columns 
                    WHERE table_name = @tableName";

                using (NpgsqlCommand cmd = new NpgsqlCommand(query, connection))
                {
                    cmd.Parameters.AddWithValue("@tableName", childTable);

                    using (NpgsqlDataReader reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            string columnName = reader.GetString(0);
                            string dataType = reader.GetString(1);

                            if (columnName == childPK || columnName == childFK)
                                continue;

                            if ((dataType.Contains("timestamp") || dataType == "date" || dataType == "time") &&
                                (columnName.Contains("updated") || columnName.Contains("created")))
                                continue;

                            Control control;

                            if (dataType == "integer" || dataType == "numeric" || dataType == "bigint")
                            {
                                control = new NumericUpDown
                                {
                                    Minimum = 0,
                                    Maximum = 1000000,
                                    Size = new Size(120, 23)
                                };
                            }
                            else if (dataType == "boolean")
                            {
                                control = new CheckBox
                                {
                                    Text = "",
                                    Size = new Size(120, 23)
                                };
                            }
                            else
                            {
                                control = new TextBox
                                {
                                    Size = new Size(240, 23)
                                };
                            }

                            _controls[columnName] = control;
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error creating form: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }
    private void BtnSave_Click(object sender, EventArgs e)
    {
        try
        {
            foreach (var entry in _controls)
            {
                if (entry.Value is TextBox textBox && string.IsNullOrWhiteSpace(textBox.Text))
                {
                    string displayName = entry.Key.Replace("_", " ");
                    displayName = System.Globalization.CultureInfo.CurrentCulture.TextInfo.ToTitleCase(displayName);
                    
                    MessageBox.Show($"Please enter a value for {displayName}.", 
                        "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    textBox.Focus();
                    this.DialogResult = DialogResult.None;
                    return;
                }
            }
            
            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {
                connection.Open();
                
                List<string> columns = new List<string>();
                List<string> parameters = new List<string>();
                NpgsqlCommand cmd = new NpgsqlCommand();
                
                foreach (var entry in _controls)
                {
                    columns.Add(entry.Key);
                    parameters.Add("@" + entry.Key);
                    
                    object value = null;
                    
                    if (entry.Value is TextBox textBox)
                    {
                        value = textBox.Text.Trim();
                    }
                    else if (entry.Value is NumericUpDown numUpDown)
                    {
                        value = numUpDown.Value;
                    }
                    else if (entry.Value is CheckBox checkBox)
                    {
                        value = checkBox.Checked;
                    }
                    
                    cmd.Parameters.AddWithValue("@" + entry.Key, value ?? DBNull.Value);
                }
                
                string query;
                
                if (_childPKValue == -1)
                {
                  
                    columns.Add(childFK);
                    parameters.Add("@" + childFK);
                    cmd.Parameters.AddWithValue("@" + childFK, _parentPKValue);
                    
                    query = $"INSERT INTO {childTable} ({string.Join(", ", columns)}) VALUES ({string.Join(", ", parameters)})";
                }
                else
                {
                    List<string> setValues = new List<string>();
                    
                    for (int i = 0; i < columns.Count; i++)
                    {
                        setValues.Add($"{columns[i]} = {parameters[i]}");
                    }
                    
                    query = $"UPDATE {childTable} SET {string.Join(", ", setValues)} WHERE {childPK} = @id";
                    cmd.Parameters.AddWithValue("@id", _childPKValue);
                }
                
                cmd.CommandText = query;
                cmd.Connection = connection;
                cmd.ExecuteNonQuery();
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error saving {childAsSingularNoum}: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            this.DialogResult = DialogResult.None;
        }
    }
}
