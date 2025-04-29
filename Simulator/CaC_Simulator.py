#%% CaC Simulator
# python = 3.9.18
# pygame = 2.6.0

import pygame as pg

import os
import numpy as np
import queue

import itertools


#%% Vector
class Vec2: # 2차원 벡터 클래스
    def __init__(self, x, y):
        self.values = [x, y]
        self.x = self.values[0]
        self.y = self.values[1]
    
    def __repr__(self):
        return f"<{self.x}, {self.y}>"
    
    def __getitem__(self, item):
        return self.values.__getitem__(item)
    
    def __setitem__(self, key, value):
        self.values.__setitem__(key, value)
        self.x, self.y = self.values
    
    def __eq__(self, other):
        return (self.values == other.values)

    def __add__(self, other):
        return Vec2(self.x + other.x, self.y + other.y)
    
    def __radd__(self, other):
        return Vec2(self.x + other.x, self.y + other.y)

    def __mul__(self, num):
        return Vec2(num * self.x, num * self.y)
    
    def __rmul__(self, num):
        return Vec2(num * self.x, num * self.y)
    
    def __matmul__(self, other):
        return Vec2(self.x * other.x, self.y * other.y)

    def __neg__(self):
        return Vec2(-self.x, -self.y)

    def __sub__(self, other):
        return Vec2(self.x - other.x, self.y - other.y)
    
    def __truediv__(self, num):
        return Vec2(self.x / num, self.y / num)

    def __abs__(self):
        return (self.x**2 + self.y**2)**0.5
    
    def __pow__(self, num):
        return Vec2(self.x ** num, self.y ** num)
    
    def __floordiv__(self, num):
        return Vec2(self.x // num, self.y // num)

    def dot(self, other):
        return self.x*other.x + self.y*other.y

    def unit(self):
        if abs(self)==0:
            return self
        else:
            return self*(1/abs(self))
    
    def distance(self, other):
        return abs(self - other)
    
    def rotation(self, deg):
        rad = deg * (np.pi/180)
        _cos = np.cos(rad)
        _sin = np.sin(rad)
        return Vec2(_cos * self.x - _sin * self.y, _sin * self.x + _cos * self.y)


#%% Game System
def util_directory():
    return os.getcwd()

def util_image_load(img):
    """loads an image, prepares it for play"""
    dir_main = util_directory()
    img = f'{dir_main}/data//image/{img}'
    try:
        surface = pg.image.load(img)
    except:
        pg.quit()
        raise SystemExit(f'Could not load image "{img}"')
    return surface.convert_alpha()


#%% Environment
class Env_CaC_Simulator:
    def __init__(self, map_size=(33,33), mode_render='human'):
        self.TPS = 20
        self.map_x = map_size[0]
        self.map_y = map_size[1] # In MC, it represents z-coordinate
        
        #
        self.W_wall = np.zeros(map_size, dtype=bool)
        self.W_obstacle = np.zeros(map_size, dtype=bool)
        
        # Initialize
        self.mode_render = mode_render
        self.init_render()
    
    def init_render(self):
        pg.init()
        pg.display.set_caption('Chasing and Chased Simulator')
        
        # Screen 생성
        self.screen = pg.display.set_mode((1920,1080))
        
        # clock 생성
        self.clock = pg.time.Clock()
        self.font = pg.font.SysFont('Arial', 12)
        
        # 초기 렌더
        self.screen.fill((0,0,0))
    
    def tick(self):
        self.tick_render()
    
    def tick_render(self):
        # pixel size
        px = 24
        
        # Background
        self.screen.fill((0,0,0))
        
        # Grass (Grid)
        pg.draw.rect(self.screen, (93,114,59), (0, 0, self.map_x*px, self.map_y*px))
        for i in range(self.map_x+1):
            pg.draw.line(self.screen, (255,255,255), (i*px, 0), (i*px, self.map_y*px))
        for j in range(self.map_y+1):
            pg.draw.line(self.screen, (255,255,255), (0, j*px), (self.map_x*px, j*px))
            
        # Wall and obstacle
        if np.any(self.W_wall):
            for wall in np.transpose(np.where(self.W_wall)):
                pg.draw.rect(self.screen, (93,75,51), (*(wall*px), *((wall+1)*px)))
        if np.any(self.W_obstacle):
            for obstacle in np.transpose(np.where(self.W_obstacle)):
                pg.draw.rect(self.screen, (180,148,91), (*(obstacle*px), *((obstacle+1)*px)))
            
        # Render Update
        self.clock.tick(self.TPS)
        pg.display.update()
    
    def close(self):
        pg.quit()
    
    
    class CaC_Entity():
        def __init__(self, env, pos=(0,0), speed=1, AI=True):
            # Self Reference
            self.env = env
            
            # Position
            self.P = Vec2(pos[0], pos[1]) # Current position
            self.P_tg = Vec2(0,0) # target position
            
            # Property
            self.v = speed # [block/tick]
            self.r = 0 # [Deg]
            
            #
            self.AI = AI
            self.AI_r = np.arange(-135, 181, 45)
            
            # Field
            self.field = Vec2(0,0)
        
        def move(self, pos_target):
            self.P_tg = pos_target
            
            if (self.role==0):
                rad = np.deg2rad(self.angle)
                self.x += self.v * np.cos(rad)
                self.y += self.v * np.sin(rad)
        
        def steering(self, angle):
            # Degree
            self.angle = angle
        
        def pathfinder(self, pos_end):
            node_end = Node(pos_end, 9999, 0)
            set_open = [Node(self.P, 0, abs(self.P.x-pos_end.x)+abs(self.P.y-pos_end.y))]
            set_closed = []
            
            while True:
                min_f = 9999
                min_f_idx = -1
                for idx, node in enumerate(set_open):
                    if node.f() < min_f:
                        min_f_idx = idx
                        min_f = node.f()
            
            
            # while (len(node_open)!=0): # open set is not empty
            #     min_f = 9999
            #     min_f_idx = -1
            #     for idx, f in enumerate(f_open):
            #         if f < min_f:
            #             min_f_idx = idx
            #     f_curr = f_open.pop(min_f_idx)
            #     node_curr = node_open.pop(min_f_idx)
            #     f_closed.append(f_curr)
            #     node_closed.append(node_curr)
                
            #     if node_curr == node_end:
            #         return
                
            #     W = self.W_wall + self.W_obstacle
            #     near_x = np.arange(max(pos_curr.x-1, 0), min(pos_curr.x+2, self.map_x))
            #     near_y = np.arange(max(pos_curr.y-1, 0), min(pos_curr.y+2, self.map_y))
            #     near = [Vec2(i,j) for i,j in itertools.product(near_x, near_y)]
                
            #     for node_near in near:
            #         if W[node_near.x, node_near.y] or ([node==node_near for node in node_closed][0]): # wall or closed node
            #             pass
            #         elif ([node==node_near for node in node_open][0]): # already open node
            #             f_open[node_open.index(node_near)]
            #         else:
            #             node_open.append(node_near)
                
        
        def cost_h(self, node, node_end):
            pos = node.pos
            pos_end = node_end.pos
            h = abs(pos.x - pos_end.x) + abs(pos.y - pos_end.y)
            return h

class Node():
    def __init__(self, pos, g, h):
        self.pos = pos
        self.g = g
        self.h = h
    
    def f(self):
        return self.g + self.h


#%%
env = Env_CaC_Simulator()

while True:
    env.tick()
    for event in pg.event.get():
        if (event.type == pg.QUIT):
            env.close()
            break