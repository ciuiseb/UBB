import pygame

class PhysicsFacade:
    @staticmethod
    def check_collisions(player, active_balls):
        for ball in active_balls:
            if ball.active:
                if player.rect.colliderect(ball.rect):
                    ball.active = False
                    return True
        return False