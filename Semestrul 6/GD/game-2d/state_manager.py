from states.menu_state import MenuState
from states.waiting_state import WaitingState
from states.game_state import GameState

class StateManager:
    def __init__(self, network):
        self.network = network
        self.room_code = ""
        self.is_host = False
        self.states = {
            "menu": MenuState(self),
            "waiting": WaitingState(self),
            "game": GameState(self)
        }
        self.current_state = self.states["menu"]

    def change_state(self, state_name):
        if state_name in self.states:
            self.current_state = self.states[state_name]

    def handle_events(self, events):
        self.current_state.handle_events(events)

    def update(self):
        self.current_state.update()

    def draw(self, surface):
        self.current_state.draw(surface)