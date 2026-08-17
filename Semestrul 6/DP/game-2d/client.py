import pygame
import sys
from state_manager import StateManager
from network import Network

def main():
    pygame.init()
    screen = pygame.display.set_mode((800, 600))
    pygame.display.set_caption("¡Cuidate!")
    clock = pygame.time.Clock()

    network = Network()
    state_manager = StateManager(network)

    running = True
    while running:
        events = pygame.event.get()
        for event in events:
            if event.type == pygame.QUIT:
                running = False

        state_manager.handle_events(events)
        state_manager.update()

        state_manager.draw(screen)
        pygame.display.flip()

        clock.tick(60)

    pygame.quit()
    sys.exit()

if __name__ == "__main__":
    main()