using System;
using System.Data;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;
using WinFormsApp1;
namespace FactoryProducts;

public partial class MainForm : Form
{
    private int _selectedCofetarieId = -1;
    private int _selectedInghetataId = -1;

    public MainForm()
    {
        InitializeComponent();
        LoadFactories();
    }

    private void InitializeComponent()
    {
        this.Text = "";
        this.Size = new Size(800, 600);

        dgvFactories = new DataGridView
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
        dgvFactories.CellClick += DgvFactories_CellClick;

        dgvProducts = new DataGridView
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
        dgvProducts.CellClick += DgvProducts_CellClick;

        lblFactories = new Label { Text = "Cofetarii:", AutoSize = true, Location = new Point(10, 10) };
        lblProducts = new Label { Text = "Inghetate:", AutoSize = true, Location = new Point(10, 220) };

        btnAddInghetata = new Button { Text = "Adauga inghetata", Enabled = false, Size = new Size(100, 30), Location = new Point(10, 10) };
        btnAddInghetata.Click += BtnAddInghetata_Click;

        btnUpdateInghetata = new Button { Text = "Update inghetata", Enabled = false, Size = new Size(120, 30), Location = new Point(120, 10) };
        btnUpdateInghetata.Click += BtnUpdateInghetata_Click;

        btnDeleteProduct = new Button { Text = "Sterge inghetata", Enabled = false, Size = new Size(120, 30), Location = new Point(250, 10) };
        btnDeleteProduct.Click += BtnDeleteProduct_Click;

        Panel buttonPanel = new Panel { Dock = DockStyle.Bottom, Height = 50, Padding = new Padding(5) };
        buttonPanel.Controls.AddRange(new Control[] { btnAddInghetata, btnUpdateInghetata, btnDeleteProduct });

        this.Controls.AddRange(new Control[] { lblFactories, dgvFactories, lblProducts, dgvProducts, buttonPanel });

    }

    private DataGridView dgvFactories;
    private DataGridView dgvProducts;
    private Label lblFactories;
    private Label lblProducts;
    private Button btnAddInghetata;
    private Button btnUpdateInghetata;
    private Button btnDeleteProduct;

    private void LoadFactories()
    {
        try
        {
            using (SqlConnection connection = DatabaseConfig.GetConnection())
            {
                connection.Open();
                string query = "SELECT id, name, adresa, an_infiintare FROM Cofetarii ORDER BY name";
                using (SqlDataAdapter adapter = new SqlDataAdapter(query, connection))
                {
                    DataTable table = new DataTable();
                    adapter.Fill(table);
                    dgvFactories.DataSource = table;
                    dgvFactories.Columns["id"].HeaderText = "ID";
                    dgvFactories.Columns["name"].HeaderText = "Nume";
                    dgvFactories.Columns["adresa"].HeaderText = "Adresa";
                    dgvFactories.Columns["an_infiintare"].HeaderText = "An infiintare";
                }
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error loading factories: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void DgvFactories_CellClick(object sender, DataGridViewCellEventArgs e)
    {
        if (e.RowIndex >= 0)
        {
            _selectedCofetarieId = Convert.ToInt32(dgvFactories.Rows[e.RowIndex].Cells["id"].Value);
            btnAddInghetata.Enabled = true;
            LoadInghetata(_selectedCofetarieId);
            _selectedInghetataId = -1;
            btnUpdateInghetata.Enabled = false;
            btnDeleteProduct.Enabled = false;
        }
    }

    private void LoadInghetata(int c_id)
    {
        try
        {
            using (SqlConnection connection = DatabaseConfig.GetConnection())
            {
                connection.Open();
                string query = "SELECT id, name, ingrediente, valoare_vanzari_anul_precedent, c_id FROM Inghetate WHERE c_id = @c_id ORDER BY name";
                using (SqlCommand cmd = new SqlCommand(query, connection))
                {
                    cmd.Parameters.AddWithValue("@c_id", c_id);
                    using (SqlDataAdapter adapter = new SqlDataAdapter(cmd))
                    {
                        DataTable table = new DataTable();
                        adapter.Fill(table);
                        dgvProducts.DataSource = table;
                        dgvProducts.Columns["id"].HeaderText = "ID";
                        dgvProducts.Columns["name"].HeaderText = "Nume inghetata";
                        dgvProducts.Columns["ingrediente"].HeaderText = "Ingrediente";
                        dgvProducts.Columns["valoare_vanzari_anul_precedent"].HeaderText = "Vanzari an precedent";
                    }
                }
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error loading inghetate: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void DgvProducts_CellClick(object sender, DataGridViewCellEventArgs e)
    {
        if (e.RowIndex >= 0)
        {
            _selectedInghetataId = Convert.ToInt32(dgvProducts.Rows[e.RowIndex].Cells["id"].Value);
            btnUpdateInghetata.Enabled = true;
            btnDeleteProduct.Enabled = true;
        }
    }

    private void BtnAddInghetata_Click(object sender, EventArgs e)
    {
        try
        {
            using (InghetataForm productForm = new InghetataForm(_selectedCofetarieId))
            {
                if (productForm.ShowDialog() == DialogResult.OK)
                    LoadInghetata(_selectedCofetarieId);
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error adding product: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void BtnUpdateInghetata_Click(object sender, EventArgs e)
    {
        try
        {
            DataGridViewRow row = dgvProducts.SelectedRows[0];
            string name = row.Cells["name"].Value.ToString();
            string ingrediente = row.Cells["ingrediente"].Value.ToString();
            int val = Convert.ToInt32(row.Cells["valoare_vanzari_anul_precedent"].Value);
            using (InghetataForm productForm = new InghetataForm(_selectedCofetarieId, _selectedInghetataId, name, ingrediente, val))
            {
                if (productForm.ShowDialog() == DialogResult.OK)
                    LoadInghetata(_selectedCofetarieId);
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error updating product: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void BtnDeleteProduct_Click(object sender, EventArgs e)
    {
        try
        {
           
            if (MessageBox.Show("Are you sure you want to delete this product?", "Confirm",
                MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
            {
                using (SqlConnection connection = DatabaseConfig.GetConnection())
                {
                    connection.Open();
                    string query = "DELETE FROM Inghetate WHERE id = @id";
                    using (SqlCommand cmd = new SqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", _selectedInghetataId);
                        cmd.ExecuteNonQuery();
                        MessageBox.Show("Inghetata a fost stearsa!.", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        _selectedInghetataId = -1;
                        btnUpdateInghetata.Enabled = false;
                        btnDeleteProduct.Enabled = false;
                        LoadInghetata(_selectedCofetarieId);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error deleting product: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }
}

public class InghetataForm : Form
{
    private readonly int _c_id;
    private readonly int _i_id;
    private TextBox txtName;
    private TextBox txtIngrediente;
    private NumericUpDown numVal;
    private Button btnSave;
    private Button btnCancel;

    public InghetataForm(int c_id)
    {
        _c_id = c_id;
        _i_id = -1;
        InitializeComponent();
        this.Text = "Adauga inghetata noua";
    }

    public InghetataForm(int c_id, int i_id, string name, string ingrediente, int val)
    {
        _c_id = c_id;
        _i_id = i_id;
        InitializeComponent();
        this.Text = "Update inghetata";
        txtName.Text = name;
        txtIngrediente.Text = ingrediente;
        numVal.Value = val;
    }

    private void InitializeComponent()
    {
        this.Size = new Size(400, 220);
        this.FormBorderStyle = FormBorderStyle.FixedDialog;
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.StartPosition = FormStartPosition.CenterParent;

        Label lblName = new Label
        {
            Text = "Nume inghetata:",
            Location = new Point(20, 20),
            AutoSize = true
        };

        txtName = new TextBox
        {
            Location = new Point(170, 18),
            Size = new Size(190, 23)
        };

        Label lblIngrediente = new Label
        {
            Text = "Ingrediente inghetata:",
            Location = new Point(20, 50),
            AutoSize = true
        };

        txtIngrediente = new TextBox
        {
            Location = new Point(170, 48),
            Size = new Size(190, 23)
        };

        Label lblStock = new Label
        {
            Text = "Valoare vanzari an precedent:",
            Location = new Point(20, 80),
            AutoSize = true
        };

        numVal = new NumericUpDown
        {
            Location = new Point(220, 78),
            Size = new Size(140, 23),
            Minimum = 0,
            Maximum = 1_000_000
        };

        btnSave = new Button
        {
            Text = "Save",
            DialogResult = DialogResult.OK,
            Location = new Point(140, 120),
            Size = new Size(80, 30)
        };
        btnSave.Click += BtnSave_Click;

        btnCancel = new Button
        {
            Text = "Cancel",
            DialogResult = DialogResult.Cancel,
            Location = new Point(230, 120),
            Size = new Size(80, 30)
        };

        this.Controls.AddRange(new Control[] { lblName, txtName, lblIngrediente, txtIngrediente, lblStock, numVal, btnSave, btnCancel });
        this.AcceptButton = btnSave;
        this.CancelButton = btnCancel;
    }
    private void BtnSave_Click(object sender, EventArgs e)
    {
        try
        {
            if (string.IsNullOrWhiteSpace(txtName.Text))
            {
                MessageBox.Show("Please enter a product name.", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                this.DialogResult = DialogResult.None;
                return;
            }

            using (SqlConnection connection = DatabaseConfig.GetConnection())
            {
                connection.Open();
                string query = _i_id == -1
                    ? "INSERT INTO Inghetate (name, ingrediente, valoare_vanzari_anul_precedent, c_id) VALUES (@name, @ingrediente, @valoare_vanzari_anul_precendet, @c_id)"
                    : "UPDATE Inghetate SET name = @name, ingrediente = @ingrediente, valoare_vanzari_anul_precedent = @valoare_vanzari_anul_precendet, c_id = @c_id WHERE id = @_i_id";

                using (SqlCommand cmd = new SqlCommand(query, connection))
                {
                    cmd.Parameters.AddWithValue("@name", txtName.Text.Trim());
                    cmd.Parameters.AddWithValue("@ingrediente", txtIngrediente.Text.Trim());
                    cmd.Parameters.AddWithValue("@valoare_vanzari_anul_precendet", (int)numVal.Value);
                    cmd.Parameters.AddWithValue("@c_id", _c_id);

                    if (_i_id != -1)
                        cmd.Parameters.AddWithValue("@_i_id", _i_id);

                    cmd.ExecuteNonQuery();
                }
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error saving product: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            this.DialogResult = DialogResult.None;
        }
    }
}