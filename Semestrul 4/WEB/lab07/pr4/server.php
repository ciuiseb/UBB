<?php
class AuthService {
    private $db;

    public function __construct() {
        $this->db = new PDO('sqlite:db_4');
        $this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }

    public function login($user, $password) {
        $sql = "SELECT * FROM users WHERE user = ? AND password = ? and is_valid = 1";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([$user, $password]);
        $row = $stmt->fetch();

        if ($row) {
            header("Location: dashboard.html");
            exit();
        } else {
            header("Location: login.html?error=1");
            exit();
        }
    }

    public function signup($user, $password, $email) {
        $token = bin2hex(random_bytes(16));

        try {
            $sql = "INSERT INTO users (user, password, email, confirmation_token, is_valid) VALUES (?, ?, ?, ?, 0)";
            $stmt = $this->db->prepare($sql);
            $stmt->execute([$user, $password, $email, $token]);

            require_once 'PHPMailer/src/PHPMailer.php';
            require_once 'PHPMailer/src/SMTP.php';

            $mail = new PHPMailer\PHPMailer\PHPMailer();
            $mail->isSMTP();
            $mail->Host = 'smtp.gmail.com';
            $mail->SMTPAuth = true;
            $mail->Username = 'ciuisebi@gmail.com';
            $mail->Password = 'lyzv xgdq lhik bzlx';
            $mail->SMTPSecure = 'tls';
            $mail->Port = 587;

            $mail->setFrom('ciuisebi@gmail.com');
            $mail->addAddress($email);
            $mail->Subject = 'Verify your account';
            $mail->Body = 'Click this link to verify: http://' . $_SERVER['HTTP_HOST'] . '/server.php?action=verify&token=' . $token;
            $mail->isHTML(true);

            $mail->send();

            header("Location: login.html");
            exit();

        } catch(Exception $e) {
            header("Location: signup.html?error=1");
            exit();
        }
    }



    public function verify($token) {
        try {
            $sql = "UPDATE users SET is_valid = 1, confirmation_token = NULL WHERE confirmation_token = ?";
            $stmt = $this->db->prepare($sql);
            $stmt->execute([$token]);

            if ($stmt->rowCount() > 0) {
                header("Location: login.html?verified=1");
                exit();
            } else {
                header("Location: login.html?verify_error=1");
                exit();
            }

        } catch(PDOException $e) {
            header("Location: login.html?verify_error=1");
            exit();
        }
    }
}

$service = new AuthService();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['action'])) {
        $action = $_POST['action'];

        if ($action === 'signup') {
            $service->signup($_POST['user'], $_POST['password'], $_POST['email']);
        }

        if ($action === 'login') {
            $service->login($_POST['user'], $_POST['password']);
        }
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    if (isset($_GET['action'])) {
        $action = $_GET['action'];

        if ($action === 'verify' && isset($_GET['token'])) {
            $service->verify($_GET['token']);
        }
    }
}
?>