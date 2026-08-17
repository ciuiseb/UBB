import pygame
from states.base_state import State

class MenuState(State):
    def __init__(self, state_manager):
        super().__init__(state_manager)
        self.input_text = ""
        self.typing = False

    def handle_events(self, events):
        for event in events:
            if event.type == pygame.KEYDOWN:
                if self.typing:
                    if event.key == pygame.K_RETURN:
                        if len(self.input_text) == 4:
                            self.state_manager.network.send({"action": "JOIN", "code": self.input_text.upper()})
                            self.state_manager.is_host = False
                            self.state_manager.change_state("waiting")
                    elif event.key == pygame.K_BACKSPACE:
                        self.input_text = self.input_text[:-1]
                    elif len(self.input_text) < 4 and event.unicode.isalpha():
                        self.input_text += event.unicode.upper()
                else:
                    if event.key == pygame.K_c:
                        self.state_manager.network.send({"action": "CREATE"})
                        self.state_manager.is_host = True
                        self.state_manager.change_state("waiting")
                    elif event.key == pygame.K_j:
                        self.typing = True
                        self.input_text = ""

    def update(self):
        pass

    def draw(self, surface):
        surface.fill((40, 44, 52))
        font = pygame.font.SysFont(None, 48)

        if not self.typing:
            text_create = font.render("Press C to Create Room", True, (255, 255, 255))
            text_join = font.render("Press J to Join Room", True, (255, 255, 255))
            surface.blit(text_create, (200, 250))
            surface.blit(text_join, (200, 320))
        else:
            text_prompt = font.render("Enter 4-letter Code and press Enter:", True, (255, 255, 255))
            text_input = font.render(self.input_text, True, (255, 255, 0))
            surface.blit(text_prompt, (100, 250))
            surface.blit(text_input, (350, 320))