    <?php
    header('Content-type: application/json');
    
    class TrainStationService{
        private $db;
    
        public function __construct()
        {
            $this->db = new PDO('sqlite:db_1');
        }
    
        public function getAll()
        {
            $statement = $this->db->prepare("SELECT DISTINCT id, nume FROM statii ORDER BY nume");
            $statement->execute();
    
            return $statement->fetchAll(PDO::FETCH_ASSOC);
        }
    
        public function getPossibleDestinations($id)
        {
            $statement = $this->db->prepare("
            select distinct s.id, s.nume
            from statii s 
            inner join trenuri t on s.id = t.statie_sosire_id
            where t.statie_plecare_id = ?
            order by s.nume
            ");
    
            $statement->execute([$id]);
            return $statement->fetchAll(PDO::FETCH_ASSOC);
        }
    }
    
    $service = new TrainStationService();
    
    if ($_SERVER['REQUEST_METHOD'] === 'GET') {
        if (isset($_GET['action'])) {
            switch ($_GET['action']) {
                case 'getAll':
                    echo json_encode($service->getAll());
                    break;
    
                case 'getPossibleDestinations':
                    if (isset($_GET['id']) && is_numeric($_GET['id'])) {
                        echo json_encode($service->getPossibleDestinations((int)$_GET['id']));
                    } else {
                        http_response_code(400);
                        echo json_encode(['error' => 'ID-ul stației este necesar și trebuie să fie numeric']);
                    }
                    break;
    
                default:
                    http_response_code(400);
                    echo json_encode(['error' => 'Acțiune necunoscută']);
            }
        } else {
            http_response_code(400);
            echo json_encode(['error' => 'Parametrul action este necesar']);
        }
    } else {
        http_response_code(405);
        echo json_encode(['error' => 'Doar cererile GET sunt permise']);
    }