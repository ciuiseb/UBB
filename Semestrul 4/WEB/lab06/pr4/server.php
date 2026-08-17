<?php
session_start();
header('Content-Type: application/json');

class XOGame {

    public function __construct() {
        if (!isset($_SESSION['board'])) {
            $this->initializeGame();
        }
    }

    public function initializeGame() {
        $_SESSION['board'] = array_fill(0, 9, '');
        $_SESSION['human_player'] = rand(0, 1) ? 'X' : 'O';
        $_SESSION['computer_player'] = $_SESSION['human_player'] === 'X' ? 'O' : 'X';
        $_SESSION['current_player'] = 'X';
        $_SESSION['game_status'] = 'active';

        if ($_SESSION['computer_player'] === 'X') {
            $this->makeComputerMove();
        }
    }

    public function makeHumanMove($position) {
        if ($_SESSION['game_status'] !== 'active') {
            return ['error' => 'Game is over'];
        }

        if ($_SESSION['current_player'] !== $_SESSION['human_player']) {
            return ['error' => 'Not your turn'];
        }

        if ($position < 0 || $position > 8 || $_SESSION['board'][$position] !== '') {
            return ['error' => 'Invalid move'];
        }

        $_SESSION['board'][$position] = $_SESSION['human_player'];
        $_SESSION['current_player'] = $_SESSION['computer_player'];

        $winner = $this->checkWinner();
        if ($winner) {
            $_SESSION['game_status'] = $winner === $_SESSION['human_player'] ? 'human_wins' : 'computer_wins';
        } else if ($this->isBoardFull()) {
            $_SESSION['game_status'] = 'draw';
        }

        if ($_SESSION['game_status'] === 'active') {
            $this->makeComputerMove();

            $winner = $this->checkWinner();
            if ($winner) {
                $_SESSION['game_status'] = $winner === $_SESSION['human_player'] ? 'human_wins' : 'computer_wins';
            } else if ($this->isBoardFull()) {
                $_SESSION['game_status'] = 'draw';
            }
        }

        return $this->getGameState();
    }

    public function makeComputerMove() {
        $emptySpots = [];
        for ($i = 0; $i < 9; $i++) {
            if ($_SESSION['board'][$i] === '') {
                $emptySpots[] = $i;
            }
        }

        if (!empty($emptySpots)) {
            $randomSpot = $emptySpots[array_rand($emptySpots)];
            $_SESSION['board'][$randomSpot] = $_SESSION['computer_player'];
            $_SESSION['current_player'] = $_SESSION['human_player'];
        }
    }

    public function checkWinner() {
        $board = $_SESSION['board'];
        $winningCombinations = [
            [0, 1, 2], [3, 4, 5], [6, 7, 8],
            [0, 3, 6], [1, 4, 7], [2, 5, 8],
            [0, 4, 8], [2, 4, 6]
        ];

        foreach ($winningCombinations as $combo) {
            if ($board[$combo[0]] !== '' &&
                $board[$combo[0]] === $board[$combo[1]] &&
                $board[$combo[1]] === $board[$combo[2]]) {
                return $board[$combo[0]];
            }
        }

        return null;
    }

    public function isBoardFull() {
        return !in_array('', $_SESSION['board']);
    }

    public function getGameState() {
        return [
            'board' => $_SESSION['board'],
            'human_player' => $_SESSION['human_player'],
            'computer_player' => $_SESSION['computer_player'],
            'current_player' => $_SESSION['current_player'],
            'game_status' => $_SESSION['game_status']
        ];
    }

    public function resetGame() {
        session_destroy();
        session_start();
        $this->initializeGame();
        return $this->getGameState();
    }
}

$game = new XOGame();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = json_decode(file_get_contents('php://input'), true);

    if ($input['action'] === 'move') {
        $position = $input['position'] ?? -1;
        echo json_encode($game->makeHumanMove($position));
    } else if ($input['action'] === 'reset') {
        echo json_encode($game->resetGame());
    } else {
        echo json_encode(['error' => 'Invalid action']);
    }
} else if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    if ($_GET['action'] === 'getState') {
        echo json_encode($game->getGameState());
    } else {
        echo json_encode(['error' => 'Invalid action']);
    }
} else {
    echo json_encode(['error' => 'Method not allowed']);
}
?>