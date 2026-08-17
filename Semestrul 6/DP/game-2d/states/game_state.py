import pygame
import random
from states.base_state import State
from model.player import Player
from model.ball import BallFactory
from physics_engine import PhysicsFacade

class GameState(State):
    def __init__(self, state_manager):
        super().__init__(state_manager)
        self.player = Player(375, 530)
        self.active_balls = []
        self.game_over = False
        self.frames_survived = 0
        self.dodger_score = 0
        self.dropper_score = 0
        self.winner_text = ""
        self.local_ready = False
        self.remote_ready = False

    def handle_events(self, events):
        for event in events:
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    self.state_manager.change_state("menu")
                if self.game_over and event.key == pygame.K_r and not self.local_ready:
                    self.local_ready = True
                    self.state_manager.network.send({"action": "REPLAY_READY"})

            if not self.game_over and not self.state_manager.is_host:
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if event.pos[1] < 300:
                        ball_type = random.choice(["normal", "fast"])
                        ball = BallFactory.create_ball(ball_type)
                        ball.spawn(event.pos[0], event.pos[1])
                        self.active_balls.append(ball)
                        self.state_manager.network.send({
                            "action": "DROP",
                            "x": event.pos[0],
                            "y": event.pos[1],
                            "type": ball_type
                        })

    def update(self):
        while self.state_manager.network.messages:
            msg = self.state_manager.network.messages.pop(0)
            action = msg.get("action")

            if action == "MOVE" and not self.state_manager.is_host:
                self.player.rect.x = msg.get("x")
            elif action == "DROP" and self.state_manager.is_host:
                ball = BallFactory.create_ball(msg.get("type"))
                ball.spawn(msg.get("x"), msg.get("y"))
                self.active_balls.append(ball)
            elif action == "GAME_OVER":
                self.game_over = True
                self.dropper_score += 1
                self.winner_text = "Dropper Hits! +1 Point"
            elif action == "TIME_UP":
                self.game_over = True
                self.dodger_score += 1
                self.winner_text = "Dodger Survives! +1 Point"
            elif action == "REPLAY_READY":
                self.remote_ready = True

        if self.game_over and self.local_ready and self.remote_ready:
            self.reset_game()

        if self.game_over:
            return

        for ball in self.active_balls[:]:
            ball.update()
            if not ball.active:
                self.active_balls.remove(ball)

        self.frames_survived += 1

        if self.state_manager.is_host:
            keys = pygame.key.get_pressed()
            old_x = self.player.rect.x
            self.player.update(keys)

            if old_x != self.player.rect.x:
                self.state_manager.network.send({"action": "MOVE", "x": self.player.rect.x})

            hit = PhysicsFacade.check_collisions(self.player, self.active_balls)
            if hit:
                self.game_over = True
                self.dropper_score += 1
                self.winner_text = "Dropper Hits! +1 Point"
                self.state_manager.network.send({"action": "GAME_OVER"})
            elif self.frames_survived >= 1800:
                self.game_over = True
                self.dodger_score += 1
                self.winner_text = "Dodger Survives! +1 Point"
                self.state_manager.network.send({"action": "TIME_UP"})

    def draw(self, surface):
        surface.fill((39, 174, 96))
        pygame.draw.rect(surface, (46, 204, 113), (0, 0, 800, 300))

        self.player.draw(surface)
        for ball in self.active_balls:
            ball.draw(surface)

        font = pygame.font.SysFont(None, 36)
        score_text = font.render(f"Dodger: {self.dodger_score}  |  Dropper: {self.dropper_score}", True, (255, 255, 255))

        time_left = max(0, 30 - (self.frames_survived // 60))
        time_text = font.render(f"Time: {time_left}s", True, (255, 255, 255))

        surface.blit(score_text, (10, 10))
        surface.blit(time_text, (650, 10))

        if self.game_over:
            overlay = pygame.Surface((800, 600), pygame.SRCALPHA)
            overlay.fill((0, 0, 0, 180))
            surface.blit(overlay, (0, 0))

            big_font = pygame.font.SysFont(None, 64)
            text = big_font.render(self.winner_text, True, (255, 215, 0))

            if self.local_ready:
                status_str = "Waiting for other player..."
            else:
                status_str = "Press R to Ready Up or ESC to Menu"

            status_text = font.render(status_str, True, (255, 255, 255))

            text_rect = text.get_rect(center=(400, 250))
            status_rect = status_text.get_rect(center=(400, 310))

            surface.blit(text, text_rect)
            surface.blit(status_text, status_rect)

    def reset_game(self):
        self.player.rect.x = 375
        self.game_over = False
        self.frames_survived = 0
        self.local_ready = False
        self.remote_ready = False
        self.active_balls.clear()