#%%
import pygame as pg

import os
import numpy as np


#%% Game System
def find_directory():
    return os.path.split(os.path.abspath(__file__))[0]

def load_image(file):
    """loads an image, prepares it for play"""
    file = os.path.join(find_directory(), 'Data', file)
    try:
        surface = pg.image.load(file)
    except:
        pg.quit()
        raise SystemExit(f'Could not load image "{file}"')
    return surface.convert_alpha()


#%%
class Env_CaC_Math:
    def __init__(self):
        #
        self.grid_offset = () # Topleft of grid
        
        # Initialize
        self.init_render()
    
    def init_render(self):
        pg.init()
        pg.display.set_caption('Chasing and Chased - Math')
        
        # Screen 생성
        self.screen = pg.display.set_mode((1280,720))
        
        # clock 생성
        self.G_clock = pg.time.Clock()
        self.G_font = pg.font.SysFont('Arial', 16)
        
        # 스프라이트 생성
        self.predator.img = pg.transform.scale(load_image("Spr_Yellow.png"), tuple(self.grid_size//2))
        self.predator.spr = pg.transform.rotate(self.predator.img, 0)
        self.prey.img = pg.transform.scale(load_image("Spr_Cyan.png"), tuple(self.grid_size//2))
        self.prey.spr = pg.transform.rotate(self.prey.img, 0)
        
        # 초기 렌더
        self.screen.fill((192,192,192))


class CaC_Entity():
    def __init__(self, role=0, pos=(0,0), speed=1):
        # Role (0:self, 1:Predator(AI), 2:Prey(AI))
        self.role = role
        
        # Position
        self.x = pos[0]
        self.y = pos[1]
        
        # Spec
        self.v = speed
         
        # Etc
        if (self.role==0):
            self.angle = 0
    
    def move(self, pos_target):
        if (self.role==0):
            rad = np.deg2rad(self.angle)
            self.x += self.v * np.cos(rad)
            self.y += self.v * np.sin(rad)
    
    def steering(self, angle):
        # Degree
        self.angle = angle



#%%
TPS = 20 # Tick rate

env = Env_CaC_Math()