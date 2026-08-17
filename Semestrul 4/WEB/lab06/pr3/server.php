<?php

header('Content-Type: application/json');

class StudentService
{
    private $db;

    public function __construct()
    {
        try {
            $this->db = new PDO('sqlite:db_3');
            $this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $e) {
            throw new Exception('Database connection failed');
        }
    }

    public function getAllStudents()
    {
        $stmt = $this->db->prepare("SELECT id, nume, prenume FROM studenti ORDER BY nume, prenume");
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public function getStudentById($id)
    {
        $stmt = $this->db->prepare("SELECT * FROM studenti WHERE id = ?");
        $stmt->execute([$id]);
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    public function updateStudent($id, $nume, $prenume, $email)
    {
        $stmt = $this->db->prepare("UPDATE studenti SET nume = ?, prenume = ?, email = ? WHERE id = ?");
        return $stmt->execute([$nume, $prenume, $email, $id]);
    }
}

try {
    $service = new StudentService();
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Service initialization failed']);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $action = $_GET['action'] ?? '';

    switch ($action) {
        case 'getAll':
            echo json_encode($service->getAllStudents());
            break;

        case 'getStudent':
            $id = $_GET['id'] ?? '';
            if (!is_numeric($id)) {
                http_response_code(400);
                echo json_encode(['error' => 'Invalid ID']);
                break;
            }

            $student = $service->getStudentById((int)$id);
            if (!$student) {
                http_response_code(404);
                echo json_encode(['error' => 'Student not found']);
                break;
            }

            echo json_encode($student);
            break;

        default:
            http_response_code(400);
            echo json_encode(['error' => 'Invalid action']);
    }
}
elseif ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = json_decode(file_get_contents('php://input'), true);

    if (!$input || $input['action'] !== 'updateStudent') {
        http_response_code(400);
        echo json_encode(['error' => 'Invalid request']);
        exit;
    }

    $required = ['id', 'nume', 'prenume', 'email'];
    foreach ($required as $field) {
        if (!isset($input[$field]) || trim($input[$field]) === '') {
            http_response_code(400);
            echo json_encode(['error' => "Missing field: $field"]);
            exit;
        }
    }

    $success = $service->updateStudent($input['id'], $input['nume'], $input['prenume'], $input['email']);

    if ($success) {
        echo json_encode(['success' => true]);
    } else {
        http_response_code(500);
        echo json_encode(['error' => 'Update failed']);
    }
}
else {
    http_response_code(405);
    echo json_encode(['error' => 'Method not allowed']);
}
