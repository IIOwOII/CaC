#%% CaC Simulator
# python = 3.9.18
# pygame-ce = 2.5.3

import pygame as pg

import os
import numpy as np
import pickle
import itertools

from copy import deepcopy


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
        if isinstance(other, Vec2):
            return Vec2(self.x + other.x, self.y + other.y)
        elif isinstance(other, (int,float)):
            return Vec2(self.x + other, self.y + other)
    
    def __radd__(self, other):
        if isinstance(other, Vec2):
            return Vec2(self.x + other.x, self.y + other.y)
        elif isinstance(other, (int,float)):
            return Vec2(self.x + other, self.y + other)

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
    
    def distance(self, other, method='E'):
        if (method == 'E'): # Euclidean
            D = abs(self - other)
        elif (method == 'M'): # Manhattan
            D = abs(self.x-other.x) + abs(self.y-other.y)
        return D
    
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
        #
        self.TPS = 20
        self.map_x = map_size[0]
        self.map_y = map_size[1] # In MC, it represents z-coordinate
        
        # 
        self.screen_w = 1280
        self.screen_h = 720
        self.color = {
            'font': (255,255,255),
            'shade': (255,255,255,128),
            'grass': (93,114,59),
            'wall': (93,75,51),
            'obstacle': (180,148,91),
            'spawn_player': (0,0,255),
            'spawn_opponent': (0,233,255),
            'entity_predator': (202,198,197),
            'entity_prey': (253,207,140)}
        
        #
        self.W_wall = np.zeros(map_size, dtype=bool)
        self.W_obstacle = np.zeros(map_size, dtype=bool)
        self.W_spawn_player = np.zeros(map_size, dtype=bool)
        self.W_spawn_opponent = np.zeros(map_size, dtype=bool)
        
        # Cursor
        self.mx = 0 # px
        self.my = 0 # px
        self.mw = 0 # wheel
        
        # Entity
        self.E_predator = self.CaC_Entity(env=self, mode='predator')
        self.E_prey = self.CaC_Entity(env=self, mode='prey')
        
        # Initialize
        self.mode_game = 'edit'
        self.mode_render = mode_render
        self.init_render()
    
    def init_render(self):
        pg.init()
        pg.display.set_caption('Chasing and Chased Simulator')
        
        # Screen 생성
        self.screen = pg.display.set_mode((self.screen_w, self.screen_h))
        
        # pixel size
        self.px = 16
        
        # clock 생성
        self.clock = pg.time.Clock()
        self.font = pg.font.SysFont('Arial', size=16)
        
        # 초기 렌더
        self.screen.fill((0,0,0))
    
    
    def tick(self):
        self.tick_render()
    
    def tick_render(self):
        # Background
        self.screen.fill((0,0,0))
        
        # Grass
        pg.draw.rect(self.screen, self.color['grass'], 
                     (0, 0, self.map_x*self.px, self.map_y*self.px))
        
        # Wall and obstacle
        if np.any(self.W_wall):
            for wall in np.transpose(np.where(self.W_wall)):
                pg.draw.rect(self.screen, self.color['wall'], 
                             (*(wall*self.px), self.px, self.px))
        if np.any(self.W_obstacle):
            for obstacle in np.transpose(np.where(self.W_obstacle)):
                pg.draw.rect(self.screen, self.color['obstacle'], 
                             (*(obstacle*self.px), self.px, self.px))
        
        # Entity
        if (self.E_predator.awake):
            pg.draw.rect(self.screen, self.color['entity_predator'], 
                         (*(self.E_predator.P*self.px), self.px, self.px))
        if (self.E_prey.awake):
            pg.draw.rect(self.screen, self.color['entity_prey'], 
                         (*(self.E_prey.P*self.px), self.px, self.px))
            
        # other
        if (self.mode_game=='edit'):
            self.render_editor()
        
        # Grid
        for i in range(self.map_x+1):
            pg.draw.line(self.screen, (255,255,255), 
                         (i*self.px, 0), (i*self.px, self.map_y*self.px))
        for j in range(self.map_y+1):
            pg.draw.line(self.screen, (255,255,255), 
                         (0, j*self.px), (self.map_x*self.px, j*self.px))
        
        # Description
        desc_size_w = 256
        desc_size_h = 256
        desc_pos = (self.screen_w-desc_size_w, self.screen_h-desc_size_h)
        self.screen.blit(pg.transform.scale(
            util_image_load('spr_wood.png'), (desc_size_w, desc_size_h)),
            desc_pos)
        
        # Text
        desc_mode = self.font.render(text=f'{self.mode_game}', 
                         antialias=False, color=self.color['font'])
        self.screen.blit(desc_mode, desc_pos)
        
        # Render Update
        self.clock.tick(self.TPS)
        pg.display.update()
        
    def render_editor(self):
        M = self.util_px2block(Vec2(self.mx, self.my))
        if (M.x != -1):
            shade = pg.Surface((self.px, self.px), pg.SRCALPHA)
            shade.fill(self.color['shade'])
            self.screen.blit(shade, tuple(M*self.px))
    
    
    def step_mouse(self, action):
        """
        1 : Left
        2 : Middle
        3 : Right
        4 : Scroll Up
        5 : Scroll Down
        """
        if (action==4):
            self.mw += 1
        elif (action==5):
            self.mw -= 1
        
        if (action==1) and (self.mode_game=='edit'):
            M = self.util_px2block(Vec2(self.mx, self.my))
            tile_type = (self.mw % 4) 
            self.step_build(M, tile_type)
        
    def step_build(self, pos, tile):
        if (pos.x != -1):
            if (tile == 0):
                self.W_wall[pos.x, pos.y] = (not self.W_wall[pos.x, pos.y])
            elif (tile == 1):
                self.W_obstacle[pos.x, pos.y] = (not self.W_obstacle[pos.x, pos.y])
            elif (tile == 2):
                self.W_spawn_player[pos.x, pos.y] = (not self.W_spawn_player[pos.x, pos.y])
            elif (tile == 3):
                self.W_spawn_opponent[pos.x, pos.y] = (not self.W_spawn_opponent[pos.x, pos.y])
                
    
    def util_px2block(self, pos_px):
        pos = (pos_px//self.px)
        if ((pos.x < 0) or (pos.x >= self.map_x)) or ((pos.y < 0) or (pos.y >= self.map_y)):
            return Vec2(-1,-1)
        else:
            return pos
    
    def mode_change(self):
        mode = ['edit', 'play']
        self.mode_game = mode[np.mod(mode.index(self.mode_game)+1, len(mode))]
    
    def close(self):
        pg.quit()
    
    
    class CaC_Entity():
        def __init__(self, env, mode='None', AI=True, speed=784/227):
            # Self Reference
            self.env = env
            
            # 
            self.mode = mode # predator, prey
            self.awake = False
            
            # Position
            self.P = Vec2(0,0) # Current position
            
            # Property
            self.v = speed/env.TPS # [block/tick]
            self.r = 0 # [Deg]
            
            #
            self.AI = AI
            
            # Field
            self.field = Vec2(0,0)
            
            # Pathfinder
            self.pathfinder = Pathfinder()
            self.pathfinder.set_w(self.env.W_wall+self.env.W_obstacle)
        
        def tick(self):
            if (self.pathfinder.que_move):
                flag = self.pathfinder.que_move[-1]
                self.move(flag)
                if self.P.distance(flag) < 0.1:
                    self.pathfinder.que_move.remove(flag)
        
        def move(self, pos_target):
            vec_flag = pos_target - self.P
            ###
            # angle ignored (temp)
            ###
            if (abs(vec_flag) <= self.v):
                self.P += (self.v * vec_flag.unit())
            else:
                self.P += vec_flag
        

class Pathfinder:
    def __init__(self):
        self.set_open = []
        self.set_closed = []
        self.W = np.array([], dtype=bool) # wall and obstacle map
        self.que_move = []
    
    def set_w(self, W):
        self.W = W
    
    def reset(self):
        self.set_open.clear()
        self.set_closed.clear()
        self.que_move.clear()
    
    def findpath(self, pos_start, pos_end):
        self.reset()
        
        node_start = Node(pos=pos_start, g=0, 
                          h=round(10*pos_start.distance(pos_end, method='M')))
        node_end = Node(pos=pos_end, g=-1, h=0)
        self.set_open.append(deepcopy(node_start))
        
        while (self.set_open): # set_open is not empty
            min_f = -1
            min_idx = -1
            for idx, node in enumerate(self.set_open):
                if (min_f == -1) or (node.f < min_f):
                    min_f = node.f
                    min_idx = idx
            node_curr = self.set_open.pop(min_idx)
            self.set_closed.append(deepcopy(node_curr))
            
            if (node_curr == node_end):
                pos_trace = node_end.pos
                while (pos_trace != pos_start):
                    self.que_move.append(deepcopy(pos_trace))
                    pos_trace -= self.set_closed[self.set_closed.index(pos_trace)].r
                return
            
            pos_nears = self.findnear(node_curr)
            for pos in pos_nears:
                if (pos in self.set_open):
                    node_targ = self.set_open[self.set_open(self.set_open.index(pos))]
                    node_targ.update(node_curr, node_end)
                else:
                    node_new = Node(pos=pos)
                    node_new.update(node_curr, node_end)
                    self.set_open.append(deepcopy(node_new))
    
    def findnear(self, node):
        near_x = np.arange(max(node.pos.x-1, 0), min(node.pos.x+2, self.W.shape[0]))
        near_y = np.arange(max(node.pos.y-1, 0), min(node.pos.y+2, self.W.shape[1]))
        nears = list(itertools.product(near_x, near_y))
        
        pos_nears = []
        for near in nears:
            pos = Vec2(near[0], near[1])
            # Not wall and not closed set
            if (not self.W[near[0], near[1]]) and not (pos in self.set_closed):
                pos_nears.append(pos)
        
        return pos_nears
        

class Node:
    def __init__(self, pos, g=-1, h=-1):
        self.pos = pos # Vec2
        self.g = g # -1 = inf
        self.h = h # -1 = inf
        self.update_f()
        self.r = Vec2(0,0) # arrow
    
    def __repr__(self):
        # example : <<1,4>>
        return f'<{self.pos}>'
    
    def __eq__(self, other):
        if isinstance(other, Vec2):
            return (self.pos == other)
        elif isinstance(other, Node):
            return (self.pos == other.pos)
    
    def update(self, node_g, node_h):
        self.update_g(node_g)
        self.update_h(node_h)
        self.update_f()
        
    def update_g(self, node_ref):
        diff = round(self.pos.distance(node_ref.pos, method='M'))
        if (diff == 1):
            diff = 10
        elif (diff == 2):
            diff = 14
        else:
            print(diff)
        if (self.g == -1) or (self.g > (node_ref.g + diff)):
            self.r = self.pos - node_ref.pos
            self.g = node_ref.g + diff
    
    def update_h(self, node_end):
        self.h = round(10*self.pos.distance(node_end.pos, method='M'))
    
    def update_f(self):
        if (self.g == -1) or (self.h == -1):
            self.f = -1
        else:
            self.f = self.g + self.h


#%%
env = Env_CaC_Simulator()

env_switch = True
while env_switch:
    env.tick()
    for event in pg.event.get():
        if (event.type == pg.QUIT):
            env_switch = False
            env.close()
        if (event.type == pg.KEYDOWN):
            if (event.key == pg.K_m):
                env.mode_change()
        if (event.type == pg.MOUSEMOTION):
            env.mx, env.my = pg.mouse.get_pos() # 마우스 x,y좌표값 저장
        if (event.type == pg.MOUSEBUTTONDOWN):
            env.step_mouse(event.button)