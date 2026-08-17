import pygame

class Ball:
    def __init__(self, color, gravity):
        self.rect = pygame.Rect(0, 0, 20, 20)
        self.color = color
        self.gravity = gravity
        self.velocity_y = 0
        self.active = False

    def spawn(self, x, y):
        self.rect.x = x
        self.rect.y = y
        self.active = True
        self.velocity_y = 0

    def update(self):
        if self.active:
            self.velocity_y += self.gravity
            self.rect.y += self.velocity_y
            if self.rect.y > 600:
                self.active = False

    def draw(self, surface):
        if self.active:
            pygame.draw.ellipse(surface, self.color, self.rect)

class NormalBall(Ball):
    def __init__(self):
        super().__init__((231, 76, 60), 0.15)

class FastBall(Ball):
    def __init__(self):
        super().__init__((241, 196, 15), 0.35)

class BallFactory:
    @staticmethod
    def create_ball(ball_type):
        if ball_type == "fast":
            return FastBall()
        return NormalBall()