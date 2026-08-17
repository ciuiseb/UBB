import pygame
from states.base_state import State

class WaitingState(State):
    def handle_events(self, events):
        pass

    def update(self):
        if self.state_manager.network.messages:
            msg = self.state_manager.network.messages.pop(0)

            if msg.get("action") == "ROOM_CREATED":
                self.state_manager.room_code = msg.get("code")
            elif msg.get("action") == "START_GAME":
                self.state_manager.change_state("game")
            elif msg.get("action") == "ERROR":
                self.state_manager.change_state("menu")

    def draw(self, surface):
        surface.fill((44, 62, 80))
        font = pygame.font.SysFont(None, 48)

        if self.state_manager.is_host:
            text = font.render(f"Room Code: {self.state_manager.room_code} - Waiting for P2...", True, (255, 255, 255))
        else:
            text = font.render("Joining room...", True, (255, 255, 255))

        surface.blit(text, (100, 280))