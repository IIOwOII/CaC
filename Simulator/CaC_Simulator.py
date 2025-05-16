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
    
    def vec2int(self):
        return Vec2(round(self.x), round(self.y))

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
        # Vanila MC
        self.TPS = 20
        
        #
        self.map_x = map_size[0]
        self.map_y = map_size[1] # In MC, it represents z-coordinate
        
        # 
        self.screen_w = 1280
        self.screen_h = 720
        self.color = {
            'font': (255,255,255),
            'shade': (255,255,255),
            'grass': [(93,114,59), (32,32,32), (32,32,32)],
            'wall': [(93,75,51), (224,224,224), (224,224,224)],
            'obstacle': [(180,148,91), (224,224,224), (224,224,224)],
            'void': (0,0,0),
            'spawn_player': (0,0,224),
            'spawn_opponent': (0,192,224),
            'path_predator': (160,0,160),
            'path_prey': (0,160,160),
            'dest_predator': (255,0,255),
            'dest_prey': (0,255,255)}
        
        # map
        self.W = np.zeros(map_size)
        self.w_size = 1/8 # hitbox size of wall 
        
        # Cursor
        self.mx = 0 # px
        self.my = 0 # px
        self.mw = 2 # wheel
        
        # Entity
        self.rho_A = 1.00
        self.E_switch_render = False
        self.E_switch_play = False
        self.E_predator = self.CaC_Entity(env=self, label='predator')
        self.E_prey = self.CaC_Entity(env=self, label='prey')
        
        # Block List
        self.block_list = ['sp_opp',
                           'sp_pla',
                           'grass',
                           'wall',
                           'obs',
                           'void']
        
        # Mode Setting
        self.mode_render = mode_render
        
        self.mode_game_list = ['edit', 'play']
        self.mode_game = 0
        
        self.mode_map_list = ['default', 'field', 'path']
        self.mode_map = 0
        
        # Initialize rendering
        if (mode_render=='human'):
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
        self.font = pg.font.SysFont('Arial', size=24)
        
        # Menu
        offset_w = self.screen_w-220
        self.menu = Menu(env=self)
        self.menu.buttons = [
            Button(func='save', pos=(offset_w,20), text='Save', condition=['edit']),
            Button(func='load', pos=(offset_w,80), text='Load', condition=['edit']),
            Button(func='reset', pos=(offset_w,140), text='Reset', condition=['play']),
            Button(func='spd_down', pos=(offset_w,200), text='<', condition=['play'], size=(40,40)),
            Button(func='spd_up', pos=(self.screen_w-60,200), text='>', condition=['play'], size=(40,40)),
            Button(func='play/stop', pos=(offset_w,260), text='Play/Stop', condition=['play'])]
        
        # Entity
        self.E_predator.set_sprite('spr_predator.png')
        self.E_prey.set_sprite('spr_prey.png')
        
        # 초기 렌더
        self.screen.fill((0,0,0))
    
    
    def tick(self):
        self.tick_render()
        self.menu.tick()
        if self.E_switch_play:
            self.E_predator.tick()
            self.E_prey.tick()
        self.clock.tick(self.TPS)
    
    def tick_render(self):
        # Background
        self.screen.fill((0,0,0))
        
        # Grass
        pg.draw.rect(self.screen, self.color['grass'][self.mode_map], 
                     (0, 0, self.map_x*self.px, self.map_y*self.px))
        
        # Wall and obstacle
        self.render_wall()
        
        # Spawn point
        if (self.mode_map == 0):
            self.render_spawnpoint()
        
        # Entity path
        if (self.mode_map==2):
            self.render_path()
        
        # Menu
        self.render_menu()
        
        # submenu
        if (self.mode_game==0):
            self.render_editor()
        
        # Grid
        for i in range(self.map_x+1):
            pg.draw.line(self.screen, (128,128,128), 
                         (i*self.px, 0), (i*self.px, self.map_y*self.px))
        for j in range(self.map_y+1):
            pg.draw.line(self.screen, (128,128,128), 
                         (0, j*self.px), (self.map_x*self.px, j*self.px))
        
        # Entity
        self.E_predator.render()
        self.E_prey.render()
        
        # Description
        self.render_description()
        
        # Render Update
        pg.display.update()
    
    def render_description(self):
        width = 240
        height = 240
        offset = Vec2(self.screen_w-width, self.screen_h-height)
        self.screen.blit(pg.transform.scale(
            util_image_load('spr_wood.png'), (width, height)), tuple(offset))
        
        # Text
        text_mode = self.font.render(
            text=f'Mode: {self.mode_game_list[self.mode_game]}', antialias=True, color=self.color['font'])
        self.screen.blit(text_mode, tuple(offset+Vec2(20,20)))
        text_map = self.font.render(
            text=f'Map: {self.mode_map_list[self.mode_map]}', antialias=True, color=self.color['font'])
        self.screen.blit(text_map, tuple(offset+Vec2(20,60)))
        text_block = self.font.render(
            text=f'Block: {self.block_list[(self.mw%6)]}', antialias=True, color=self.color['font'])
        self.screen.blit(text_block, tuple(offset+Vec2(20,100)))
    
    def render_spawnpoint(self):
        if np.any(self.W == -1):
            for spawn_player in np.transpose(np.where(self.W == -1)):
                pg.draw.rect(self.screen, self.color['spawn_player'], 
                             (*(spawn_player*self.px), self.px, self.px))
        if np.any(self.W == -2):
            for spawn_opponent in np.transpose(np.where(self.W == -2)):
                pg.draw.rect(self.screen, self.color['spawn_opponent'], 
                             (*(spawn_opponent*self.px), self.px, self.px))
    
    def render_wall(self):
        # Including obstacle
        if np.any(self.W == 1):
            for wall in np.transpose(np.where(self.W == 1)):
                pg.draw.rect(self.screen, self.color['wall'][self.mode_map], 
                             (*(wall*self.px), self.px, self.px))
        if np.any(self.W == 2):
            for obstacle in np.transpose(np.where(self.W == 2)):
                pg.draw.rect(self.screen, self.color['obstacle'][self.mode_map], 
                             (*(obstacle*self.px), self.px, self.px))
        if np.any(self.W == 3):
            for void in np.transpose(np.where(self.W == 3)):
                pg.draw.rect(self.screen, self.color['void'], 
                             (*(void*self.px), self.px, self.px))
    
    def render_editor(self):
        M = self.util_px2block(Vec2(self.mx, self.my))
        if (M.x != -1):
            shade = pg.Surface((self.px, self.px), pg.SRCALPHA)
            shade.fill(self.color['shade'])
            self.screen.blit(shade, tuple(M*self.px))
    
    def render_menu(self):
        for btn in self.menu.buttons:
            self.screen.blit(btn.spr, btn.pos)
            btn_text = self.font.render(btn.text, True, self.color['font'])
            btn_text_rect = btn_text.get_rect()
            btn_text_rect.centerx = btn.pos[0] + btn.size[0]/2
            btn_text_rect.centery = btn.pos[1] + btn.size[1]/2
            self.screen.blit(btn_text, btn_text_rect)
        spd_text = self.font.render(f'{self.rho_A:.2f}', True, self.color['font'])
        self.screen.blit(spd_text, (self.screen_w-140, 210))
    
    def render_path(self):
        for path in self.E_predator.pathfinder.que_move:
            pg.draw.rect(self.screen, self.color['path_predator'],
                         (*(path*self.px), self.px, self.px))
        for path in self.E_prey.pathfinder.que_move:
            pg.draw.rect(self.screen, self.color['path_prey'],
                         (*(path*self.px), self.px, self.px))
        pg.draw.rect(self.screen, self.color['dest_predator'],
                     (*(self.E_predator.pathfinder.destination*self.px), self.px, self.px))
        pg.draw.rect(self.screen, self.color['dest_prey'],
                     (*(self.E_prey.pathfinder.destination*self.px), self.px, self.px))
    
    
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
        
        if (action==1) and (self.mode_game==0):
            M = self.util_px2block(Vec2(self.mx, self.my))
            tile_type = (self.mw % 6) -2
            self.step_build(M, tile_type)
        
        if (action==1):
            self.menu.call_button()
            
        
    def step_build(self, pos, tile):
        # 0: None
        # 1: wall
        # 2: obstacle
        # 3: void
        # -1: spawn_player
        # -2: spawn_opponent
        if (pos.x != -1):
            self.W[pos.x, pos.y] = tile
    
    
    def call(self, func):
        if (func=='save'):
            self.util_save()
        elif (func=='load'):
            self.util_load()
        elif (func=='reset'):
            self.util_reset()
        elif (func=='spd_down'):
            self.util_adjust_speed(-0.02)
        elif (func=='spd_up'):
            self.util_adjust_speed(0.02)
        elif (func=='play/stop'):
            self.util_play_or_stop()
        
    
    def util_px2block(self, pos_px):
        pos = (pos_px//self.px)
        if ((pos.x < 0) or (pos.x >= self.map_x)) or ((pos.y < 0) or (pos.y >= self.map_y)):
            return Vec2(-1,-1)
        else:
            return pos
    
    def util_categorize_map(self):
        self.walls = np.transpose(np.where(self.W==1))
        self.obstacles = np.transpose(np.where(self.W==2))
    
    def util_save(self):
        dir_main = util_directory()
        with open(f'{dir_main}/data/map/Map.p', 'wb') as f:
            pickle.dump(self.W, f)
    
    def util_load(self):
        dir_main = util_directory()
        with open(f'{dir_main}/data/map/Map.p', 'rb') as f:
            self.W = pickle.load(f)
        
    def util_reset(self):
        # categorize map
        self.util_categorize_map()
        # reset entity
        self.E_predator.reset()
        self.E_prey.reset()
    
    def util_adjust_speed(self, delta):
        self.rho_A += delta
        self.rho_A = round(self.rho_A, 2)
    
    def util_play_or_stop(self):
        self.E_switch_play = (not self.E_switch_play)
        
    def mode_change(self, mode_type):
        mode_types = ['game', 'map']
        if not (mode_type in mode_types):
            return
        else:
            if mode_type == 'game':
                self.mode_game = (self.mode_game + 1) % len(self.mode_game_list)
                self.E_switch_render = (self.mode_game != 0)
            elif mode_type == 'map':
                self.mode_map = (self.mode_map + 1) % len(self.mode_map_list)
    
    def close(self):
        pg.quit()
    
    
    class CaC_Entity():
        def __init__(self, env, size=(0.5,0.5), label='', speed=784/227):
            # Self Reference
            self.env = env
            
            # 
            self.size = Vec2(*size) # Hitbox
            self.label = label # predator, prey
            
            # Position
            self.P = Vec2(0,0) # Current position
            
            # Property
            self.v = (speed/env.TPS) # [block/tick]
            self.r = 0 # [Deg]
            if (self.label=='predator'):
                self.k = {'opponent': -50,
                          'wall': 0,
                          'obstacle': 0}
            elif (self.label=='prey'):
                self.k = {'opponent': 10,
                          'wall': 5,
                          'obstacle': 2}
            
            # Field
            self.timer = 0
            
            # Pathfinder
            self.pathfinder = Pathfinder()
        
        def set_sprite(self, spr):
            self.spr = pg.transform.scale(util_image_load(spr), tuple(self.size*self.env.px))
        
        def reset(self):
            W = self.env.W
            if (self.label=='predator'):
                w_label = -1
                self.other = self.env.E_prey
            elif (self.label=='prey'):
                w_label = -2
                self.other = self.env.E_predator
            try:
                self.spawnpoint = np.transpose(np.where(W==w_label))
                self.spawn()
                self.pathfinder.set_w(W)
            except:
                print('error')
        
        def spawn(self):
            idx = np.random.randint(self.spawnpoint.shape[0])
            self.P = Vec2(*self.spawnpoint[idx])
            
        def tick(self):
            self.timer += 1
            if (self.timer == 10):
                self.timer = 0
                self.update_path()
            elif (self.pathfinder.que_move):
                flag = self.pathfinder.que_move[-1]
                self.move(flag)
                if self.P.distance(flag) < 0.1:
                    self.pathfinder.que_move.remove(flag)
        
        def render(self):
            if (self.env.E_switch_render):
                offset = 0.5*self.env.px*(Vec2(1,1)-self.size)
                pos_render = tuple(self.P*self.env.px + offset)
                self.env.screen.blit(self.spr, pos_render)
        
        def move(self, pos_target):
            vec_flag = pos_target - self.P
            ###
            # angle ignored (temp)
            ###
            
            if (abs(vec_flag) <= self.v):
                self.P += vec_flag
            else:
                self.P += (self.v * vec_flag.unit())
        
        def update_path(self):
            F = self.update_field()
            dF = F.unit() * 0.1
            P_targ = self.P + F
            
            map_x = self.env.map_x
            map_y = self.env.map_y
            W = self.env.W
            while True:
                P_rnd = P_targ.vec2int()
                cond_x = (P_rnd.x >= 0) and (P_rnd.x < map_x)
                cond_y = (P_rnd.y >= 0) and (P_rnd.y < map_y)
                if cond_x and cond_y:
                    cond_w = (W[tuple(P_rnd)] <= 0)
                    if cond_w:
                        break
                P_targ -= dF
            
            self.pathfinder.findpath(self.P, P_targ)
            
        def update_field(self):
            r_walls = np.array(list(self.P))-self.env.walls
            r_obstacles = np.array(list(self.P))-self.env.obstacles
            
            sum_field = Vec2(0,0)
            sum_field += Field_point(np.array([list(self.P - self.other.P)]),
                                     k=self.k['opponent'], func='1/r^2')
            sum_field += Field_point(r_walls, k=self.k['wall'], func='1/r^2')
            sum_field += Field_point(r_obstacles, k=self.k['obstacle'], func='1/r^2')
            
            return sum_field
            

#%% Pathfinder
class Pathfinder:
    def __init__(self):
        self.set_open = []
        self.set_closed = []
        self.W = np.array([]) # wall and obstacle map
        self.que_move = []
        self.destination = Vec2(0,0)
    
    def set_w(self, W):
        self.W = W
    
    def reset(self):
        self.set_open.clear()
        self.set_closed.clear()
        self.que_move.clear()
    
    def findpath(self, vec_S, vec_E):
        self.reset()
        
        S = vec_S.vec2int()
        E = vec_E.vec2int()
        self.destination = E
        node_start = Node(pos=S, g=0, 
                          h=round(10*S.distance(E, method='M')))
        node_end = Node(pos=E, g=-1, h=0)
        self.set_open.append(deepcopy(node_start))
        
        while (self.set_open): # set_open is not empty
            min_f = -1
            min_idx = -1
            for idx, node in enumerate(self.set_open):
                if (min_f == -1 and node.f != -1) or (node.f < min_f):
                    min_f = node.f
                    min_idx = idx
            node_curr = self.set_open.pop(min_idx)
            self.set_closed.append(deepcopy(node_curr))
            
            if (node_curr == node_end):
                pos_trace = node_end.pos
                while (pos_trace != S):
                    self.que_move.append(deepcopy(pos_trace))
                    pos_trace -= self.set_closed[self.set_closed.index(pos_trace)].r
                return
            
            pos_nears = self.findnear(node_curr)
            for pos in pos_nears:
                if (pos in self.set_open):
                    node_targ = self.set_open[self.set_open.index(pos)]
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
            if (self.W[near[0], near[1]] <= 0) and not (pos in self.set_closed):
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


#%% Button
class Button():
    def __init__(self, func, pos=(0,0), size=(200,40), text='', condition=[]):
        self.func = func
        
        self.on_mouse = False
        self.pos = pos
        self.size = size
        self.text = text
        self.condition = condition
        
        self.spr_default = pg.transform.scale(util_image_load('spr_button_default.png'), size)
        self.spr_cursored = pg.transform.scale(util_image_load('spr_button_cursored.png'), size)
        self.spr_pressed = pg.transform.scale(util_image_load('spr_button_pressed.png'), size)
        self.spr = self.spr_default


#%% Menu
class Menu():
    def __init__(self, env):
        self.env = env
        self.buttons = []
    
    def tick(self):
        # check the button
        mx, my = self.env.mx, self.env.my
        for btn in self.buttons:
            if self.env.mode_game_list[self.env.mode_game] in btn.condition:
                btn_x, btn_y = btn.pos[0], btn.pos[1]
                btn_w, btn_h = btn.size[0], btn.size[1]
                on_x = (mx > btn_x) and (mx < btn_x + btn_w)
                on_y = (my > btn_y) and (my < btn_y + btn_h)
                btn.on_mouse = on_x and on_y
                if (on_x and on_y):
                    btn.spr = btn.spr_cursored
                else:
                    btn.spr = btn.spr_default
            else:
                btn.spr = btn.spr_pressed
    
    def call_button(self):
        for btn in self.buttons:
            if btn.on_mouse:
                btn.spr = btn.spr_pressed
                self.env.call(btn.func)
                break


#%% Field
def Field_point(vec_r, k=1, func='1/r^2'):
    sca_r = (np.sum(vec_r**2, axis=1, keepdims=True)**0.5)
    
    if func=='1/r^2':
        E = 1/(sca_r**2)
    
    unit_r = vec_r/sca_r
    
    fields = k * E * unit_r
    field = Vec2(*np.sum(fields, axis=0))
    
    return field


def Field_line

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
                env.mode_change('game')
            elif (event.key == pg.K_n):
                env.mode_change('map')
        if (event.type == pg.MOUSEMOTION):
            env.mx, env.my = pg.mouse.get_pos() # 마우스 x,y좌표값 저장
        if (event.type == pg.MOUSEBUTTONDOWN):
            env.step_mouse(event.button)