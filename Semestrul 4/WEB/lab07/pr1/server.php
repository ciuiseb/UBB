<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

class TrenuriAPI {
    private $db;

    public function __construct() {
        $this->db = new PDO('sqlite:db_1');
        $this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }

    public function getLocalitati() {
        $sql = "SELECT DISTINCT localitate_plecare as localitate FROM trenuri
                UNION
                SELECT DISTINCT localitate_sosire as localitate FROM trenuri
                ORDER BY localitate";
        $stmt = $this->db->query($sql);
        return $stmt->fetchAll(PDO::FETCH_COLUMN);
    }

    public function cautaTrenuri($plecare, $sosire, $curse_directe) {
        if ($curse_directe) {
            $sql = "SELECT * FROM trenuri 
                    WHERE localitate_plecare = ? AND localitate_sosire = ?
                    ORDER BY ora_plecare";
            $stmt = $this->db->prepare($sql);
            $stmt->execute([$plecare, $sosire]);
            return [
                'tip' => 'directe',
                'data' => $stmt->fetchAll(PDO::FETCH_ASSOC)
            ];
        } else {
            $sql = "SELECT 
                        t1.nr_tren as tren1,
                        t1.tip_tren as tip1,
                        t1.localitate_plecare as plecare,
                        t1.localitate_sosire as intermediara,
                        t1.ora_plecare as ora1_plecare,
                        t1.ora_sosire as ora1_sosire,
                        t2.nr_tren as tren2,
                        t2.tip_tren as tip2,
                        t2.localitate_sosire as sosire,
                        t2.ora_plecare as ora2_plecare,
                        t2.ora_sosire as ora2_sosire
                    FROM trenuri t1
                    JOIN trenuri t2 ON t1.localitate_sosire = t2.localitate_plecare
                    WHERE t1.localitate_plecare = ? 
                    AND t2.localitate_sosire = ?
                    AND t1.ora_sosire < t2.ora_plecare
                    ORDER BY t1.ora_plecare";
            $stmt = $this->db->prepare($sql);
            $stmt->execute([$plecare, $sosire]);
            return [
                'tip' => 'cu_legatura',
                'data' => $stmt->fetchAll(PDO::FETCH_ASSOC)
            ];
        }
    }
}

$api = new TrenuriAPI();

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
     if ($_GET['action'] === 'getLocalitati') {
        echo json_encode($api->getLocalitati());
    }
    else if ($_GET['action'] === 'cautaTrenuri') {
        $plecare = $_GET['plecare'];
        $sosire = $_GET['sosire'];
        $curse_directe = (isset($_GET['curse_directe']) ? $_GET['curse_directe'] : 'false') === 'true';

        if ($plecare && $sosire) {
            echo json_encode($api->cautaTrenuri($plecare, $sosire, $curse_directe));
        } else {
            echo json_encode(['error' => 'Plecare și sosire sunt obligatorii']);
        }
    }
    else {
        echo json_encode(['error' => 'Acțiune invalidă']);
    }
} else {
    echo json_encode(['error' => 'Metodă nesuportată']);
}
?>