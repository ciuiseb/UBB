<?php
header('Content-Type: application/json');

class PaginationService {
    private $db;

    public function __construct() {
        $this->db = new PDO('sqlite:db_2');
    }

    public function getPage($page, $limit) {
        if ($page < 1) $page = 1;
        if ($limit < 1) $limit = 3;

        $offset = ($page - 1) * $limit;

        $stmt = $this->db->prepare("SELECT * FROM persoane LIMIT ? OFFSET ?");
        $stmt->execute([$limit + 1, $offset]);
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);

        $hasNext = count($results) > $limit;

        if ($hasNext) {
            array_pop($results);
        }

        $hasPrevious = $page > 1;

        return [
            'data' => $results,
            'current_page' => $page,
            'has_next' => $hasNext,
            'has_previous' => $hasPrevious
        ];
    }
}

$service = new PaginationService();

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $page = isset($_GET['page']) ? (int)$_GET['page'] : 1;
    $limit = isset($_GET['limit']) ? (int)$_GET['limit'] : 3;

    try {
        echo json_encode($service->getPage($page, $limit));
    } catch (Exception $e) {
        http_response_code(500);
        echo json_encode(['error' => 'Eroare la accesarea bazei de date']);
    }
} else {
    http_response_code(405);
    echo json_encode(['error' => 'Doar cererile GET sunt permise']);
}
