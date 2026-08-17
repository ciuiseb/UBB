import pygame

class Player:
    def __init__(self, x, y):
        self.rect = pygame.Rect(x, y, 50, 50)
        self.speed = 5

        self.frames = []
        self.color = (52, 152, 219)
        self.use_images = False

        try:
            img1 = pygame.transform.scale(pygame.image.load("assets/player_1.png").convert_alpha(), (50, 50))
            img2 = pygame.transform.scale(pygame.image.load("assets/player_2.png").convert_alpha(), (50, 50))
            self.frames = [img1, img2]
            self.use_images = True
        except FileNotFoundError:
            pass

        self.current_frame = 0
        self.animation_timer = 0
        self.animation_speed = 10

    def update(self, keys):
        moving = False
        if keys[pygame.K_a] or keys[pygame.K_LEFT]:
            self.rect.x -= self.speed
            moving = True
        if keys[pygame.K_d] or keys[pygame.K_RIGHT]:
            self.rect.x += self.speed
            moving = True

        if self.rect.left < 0:
            self.rect.left = 0
        if self.rect.right > 800:
            self.rect.right = 800

        if moving and self.use_images:
            self.animation_timer += 1
            if self.animation_timer >= self.animation_speed:
                self.current_frame = (self.current_frame + 1) % len(self.frames)
                self.animation_timer = 0
        else:
            self.current_frame = 0

    def draw(self, surface):
        if self.use_images:
            surface.blit(self.frames[self.current_frame], self.rect)
        else:
            pygame.draw.rect(surface, self.color, self.rect)