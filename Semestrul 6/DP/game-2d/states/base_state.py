class State:
    def __init__(self, state_manager):
        self.state_manager = state_manager

    def handle_events(self, events):
        pass

    def update(self):
        pass

    def draw(self, surface):
        pass