using System;
using System.Data;
using System.Windows.Forms;
using Npgsql;

namespace FactoryProducts;

public partial class MainForm : Form
{
    private readonly string _connectionString = "Host=localhost;Port=5432;Database=sgbd;Username=postgres;Password=7979";
    private int _selectedFactoryId = -1;
    private int _selectedProductId = -1;

    public MainForm()
    {
        InitializeComponent();
        LoadFactories();
    }

    private void InitializeComponent()
    {
        this.Text = "Factory Products";
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

        lblFactories = new Label { Text = "Factories:", AutoSize = true, Location = new Point(10, 10) };
        lblProducts = new Label { Text = "Products:", AutoSize = true, Location = new Point(10, 220) };

        btnAddProduct = new Button { Text = "Add Product", Enabled = false, Size = new Size(100, 30), Location = new Point(10, 10) };
        btnAddProduct.Click += BtnAddProduct_Click;

        btnUpdateProduct = new Button { Text = "Update Product", Enabled = false, Size = new Size(120, 30), Location = new Point(120, 10) };
        btnUpdateProduct.Click += BtnUpdateProduct_Click;

        btnDeleteProduct = new Button { Text = "Delete Product", Enabled = false, Size = new Size(120, 30), Location = new Point(250, 10) };
        btnDeleteProduct.Click += BtnDeleteProduct_Click;

        Panel buttonPanel = new Panel { Dock = DockStyle.Bottom, Height = 50, Padding = new Padding(5) };
        buttonPanel.Controls.AddRange(new Control[] { btnAddProduct, btnUpdateProduct, btnDeleteProduct });

        this.Controls.AddRange(new Control[] { lblFactories, dgvFactories, lblProducts, dgvProducts, buttonPanel });

    }

    private DataGridView dgvFactories;
    private DataGridView dgvProducts;
    private Label lblFactories;
    private Label lblProducts;
    private Button btnAddProduct;
    private Button btnUpdateProduct;
    private Button btnDeleteProduct;

    private void LoadFactories()
    {
        try
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {
                connection.Open();
                string query = "SELECT id, name, adress, number_of_employees FROM factories ORDER BY name";
                using (NpgsqlDataAdapter adapter = new NpgsqlDataAdapter(query, connection))
                {
                    DataTable table = new DataTable();
                    adapter.Fill(table);
                    dgvFactories.DataSource = table;
                    dgvFactories.Columns["id"].HeaderText = "ID";
                    dgvFactories.Columns["name"].HeaderText = "Name";
                    dgvFactories.Columns["adress"].HeaderText = "Address";
                    dgvFactories.Columns["number_of_employees"].HeaderText = "Employees";
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
            _selectedFactoryId = Convert.ToInt32(dgvFactories.Rows[e.RowIndex].Cells["id"].Value);
            btnAddProduct.Enabled = true;
            LoadProducts(_selectedFactoryId);
            _selectedProductId = -1;
            btnUpdateProduct.Enabled = false;
            btnDeleteProduct.Enabled = false;
        }
    }

    private void LoadProducts(int factoryId)
    {
        try
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {
                connection.Open();
                string query = "SELECT id, name, factory_id, stock, last_updated FROM products WHERE factory_id = @factoryId ORDER BY name";
                using (NpgsqlCommand cmd = new NpgsqlCommand(query, connection))
                {
                    cmd.Parameters.AddWithValue("@factoryId", factoryId);
                    using (NpgsqlDataAdapter adapter = new NpgsqlDataAdapter(cmd))
                    {
                        DataTable table = new DataTable();
                        adapter.Fill(table);
                        dgvProducts.DataSource = table;
                        dgvProducts.Columns["id"].HeaderText = "ID";
                        dgvProducts.Columns["name"].HeaderText = "Product Name";
                        dgvProducts.Columns["factory_id"].HeaderText = "Factory ID";
                        dgvProducts.Columns["stock"].HeaderText = "Stock";
                        dgvProducts.Columns["last_updated"].HeaderText = "Last Updated";
                    }
                }
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error loading products: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void DgvProducts_CellClick(object sender, DataGridViewCellEventArgs e)
    {
        if (e.RowIndex >= 0)
        {
            _selectedProductId = Convert.ToInt32(dgvProducts.Rows[e.RowIndex].Cells["id"].Value);
            btnUpdateProduct.Enabled = true;
            btnDeleteProduct.Enabled = true;
        }
    }

    private void BtnAddProduct_Click(object sender, EventArgs e)
    {
        try
        {
            using (ProductForm productForm = new ProductForm(_selectedFactoryId))
            {
                if (productForm.ShowDialog() == DialogResult.OK)
                    LoadProducts(_selectedFactoryId);
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error adding product: " + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void BtnUpdateProduct_Click(object sender, EventArgs e)
    {
        try
        {
            DataGridViewRow row = dgvProducts.SelectedRows[0];
            string name = row.Cells["name"].Value.ToString();
            int stock = Convert.ToInt32(row.Cells["stock"].Value);
            using (ProductForm productForm = new ProductForm(_selectedFactoryId, _selectedProductId, name, stock))
            {
                if (productForm.ShowDialog() == DialogResult.OK)
                    LoadProducts(_selectedFactoryId);
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
                using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
                {
                    connection.Open();
                    string query = "DELETE FROM products WHERE id = @id";
                    using (NpgsqlCommand cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", _selectedProductId);
                        cmd.ExecuteNonQuery();
                        MessageBox.Show("Product deleted successfully.", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        _selectedProductId = -1;
                        btnUpdateProduct.Enabled = false;
                        btnDeleteProduct.Enabled = false;
                        LoadProducts(_selectedFactoryId);
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

public class ProductForm : Form
{
    private readonly string _connectionString = "Host=localhost;Port=5432;Database=sgbd;Username=postgres;Password=7979";
    private readonly int _factoryId;
    private readonly int _productId;
    private TextBox txtName;
    private NumericUpDown numStock;
    private Button btnSave;
    private Button btnCancel;

    public ProductForm(int factoryId)
    {
        _factoryId = factoryId;
        _productId = -1;
        InitializeComponent();
        this.Text = "Add New Product";
    }

    public ProductForm(int factoryId, int productId, string name, int stock)
    {
        _factoryId = factoryId;
        _productId = productId;
        InitializeComponent();
        this.Text = "Update Product";
        txtName.Text = name;
        numStock.Value = stock;
    }

    private void InitializeComponent()
    {
        this.Size = new Size(400, 200);
        this.FormBorderStyle = FormBorderStyle.FixedDialog;
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.StartPosition = FormStartPosition.CenterParent;

        Label lblName = new Label
        {
            Text = "Product Name:",
            Location = new Point(20, 20),
            AutoSize = true
        };

        txtName = new TextBox
        {
            Location = new Point(120, 20),
            Size = new Size(240, 23)
        };

        Label lblStock = new Label
        {
            Text = "Stock:",
            Location = new Point(20, 50),
            AutoSize = true
        };

        numStock = new NumericUpDown
        {
            Location = new Point(120, 50),
            Size = new Size(120, 23),
            Minimum = 0,
            Maximum = 1_000_000
        };

        btnSave = new Button
        {
            Text = "Save",
            DialogResult = DialogResult.OK,
            Location = new Point(120, 100),
            Size = new Size(80, 30)
        };
        btnSave.Click += BtnSave_Click;

        btnCancel = new Button
        {
            Text = "Cancel",
            DialogResult = DialogResult.Cancel,
            Location = new Point(210, 100),
            Size = new Size(80, 30)
        };

        this.Controls.AddRange(new Control[] { lblName, txtName, lblStock, numStock, btnSave, btnCancel });

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

            using (NpgsqlConnection connection = new NpgsqlConnection(_connectionString))
            {
                connection.Open();
                string query = _productId == -1
                    ? "INSERT INTO products (name, factory_id, stock, last_updated) VALUES (@name, @factoryId, @stock, CURRENT_TIMESTAMP)"
                    : "UPDATE products SET name = @name, stock = @stock, last_updated = CURRENT_TIMESTAMP WHERE id = @productId";

                using (NpgsqlCommand cmd = new NpgsqlCommand(query, connection))
                {
                    cmd.Parameters.AddWithValue("@name", txtName.Text.Trim());
                    cmd.Parameters.AddWithValue("@factoryId", _factoryId);
                    cmd.Parameters.AddWithValue("@stock", (int)numStock.Value);

                    if (_productId != -1)
                        cmd.Parameters.AddWithValue("@productId", _productId);

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