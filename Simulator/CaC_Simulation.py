#%%
import pygame as pg

import numpy as np
import pickle as pkl
import os
import copy

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
class Env_CaC:
    def __init__(self, grid_num=(33,33), data_map='', field_line_ver=0, field_point_ver=0):
        # Components
        self.mode_num = -1
        self.field_line_ver = field_line_ver
        self.field_point_ver = field_point_ver
        
        # 그리드 설정
        self.grid_num = Vec2(*grid_num)
        self.grid_size = Vec2(16,16)
        self.grid_SP = Vec2(64,64) # 그리드가 시작되는 위치, 즉 그리드 왼쪽 상단의 위치
        
        # Field map
        self.K_OPP_prey = 10
        self.K_OBS_prey = 3
        self.K_WAL_prey = 10
        self.K_OPP_predator = -10
        self.K_OBS_predator = 1
        self.K_WAL_predator = 1
        
        self.field_mode = 0
        self.field_freq = 4
        self.field_map = np.zeros(self.field_freq * self.grid_num)
        
        # Player
        self.pla_role = 'First'
        self.pla_spd = 0.1
        self.pla_rspd = 5.0 # deg
        self.opp_spd = 0.1
        
        # Roles
        self.prey = CaC_Role(K_OPP=self.K_OPP_prey, K_OBS=self.K_OBS_prey, K_WAL=self.K_WAL_prey)
        self.predator = CaC_Role(K_OPP=self.K_OPP_predator, K_OBS=self.K_OBS_predator, K_WAL=self.K_WAL_predator)
        self.role_active = False
        
        # Offset
        self.offset_block = Vec2(0.5,0.5)
        
        # Mouse
        self.mouse_mode = 0
        self.mouse_x = 0
        self.mouse_y = 0
        self.vec_mouse = Vec2(0,0)
        
        # Initialize
        self.init_map(data_map)
        self.init_render()
    
    def init_map(self, data_map):
        # Points
        self.vec_obstacles = []
        self.vec_walls = []
        self.vec_pla_spawns = []
        self.vec_opp_spawns = []
        
        # Vertices
        self.vtc_obstacles = []
        self.vtc_walls = []
        
        if (data_map == ''):
            pass
        else:
            data_map = os.path.join(find_directory(), 'Data', data_map)
            with open(data_map, 'rb') as f:
                data = pkl.load(f)
                self.vec_obstacles = data['Obstacles']
                self.vec_walls = data['Walls']
                self.vec_pla_spawns = data['Player Spawn Points']
                self.vec_opp_spawns = data['Opponent Spawn Points']
    
    def init_render(self):
        pg.init()
        pg.display.set_caption('Chasing and Chased Simulator')
        
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
    
    def init_mode(self):
        self.mode_num = (self.mode_num + 1) % 5
        if (self.mode_num == 0):
            self.pla_role = 'Editor'
        elif (self.mode_num == 1):
            self.pla_role = 'Predator'
        elif (self.mode_num == 2):
            self.pla_role = 'Prey'
        elif (self.mode_num == 3):
            self.pla_role = 'Predator(Spectator)'
        elif (self.mode_num == 4):
            self.pla_role = 'Prey(Spectator)'
        
        if (self.pla_role == 'First') or (self.pla_role == 'Editor'):
            self.role_active = False
        else:
            self.vtc_obstacles = self.util_vertice(self.vec_obstacles)
            self.vtc_walls = self.util_vertice(self.vec_walls)
            self.role_active = True
            self.init_spawn()
    
    def init_spawn(self):
        if (not self.vec_pla_spawns or not self.vec_opp_spawns):
            self.util_error()
        elif (not self.role_active):
            pass
        else:
            vec_pla_spawn = self.vec_pla_spawns[np.random.randint(len(self.vec_pla_spawns))]
            vec_opp_spawn = self.vec_opp_spawns[np.random.randint(len(self.vec_opp_spawns))]
            if 'Predator' in self.pla_role:
                self.predator.warp(vec_pla_spawn)
                self.prey.warp(vec_opp_spawn)
            elif 'Prey' in self.pla_role:
                self.prey.warp(vec_pla_spawn)
                self.predator.warp(vec_opp_spawn)
    
    def init_fieldmap(self):
        print('Loading...')
        self.field_map = np.zeros(self.field_freq * self.grid_num)
        self.vtc_obstacles = self.util_vertice(self.vec_obstacles)
        self.vtc_walls = self.util_vertice(self.vec_walls)
        if (self.field_mode == 1): # Prey
            K_OBS = self.K_OBS_prey
            K_WAL = self.K_WAL_prey
        elif (self.field_mode == 2): # Predator
            K_OBS = self.K_OBS_predator
            K_WAL = self.K_WAL_predator
        for ssx in range(self.field_map.shape[0]):
            sx = ssx/self.field_freq
            sx_corr = sx-0.5+(1/(2*self.field_freq))
            for ssy in range(self.field_map.shape[1]):
                sy = ssy/self.field_freq
                sy_corr = sy-0.5+(1/(2*self.field_freq))
                if (Vec2(int(sx),int(sy)) in self.vec_obstacles) or (Vec2(int(sx),int(sy)) in self.vec_walls):
                    pass
                else:
                    self.field_map[ssx,ssy] = abs(Field_Line(Vec2(sx_corr,sy_corr), self.vtc_obstacles, K_OBS, self.field_line_ver) + Field_Line(Vec2(sx_corr,sy_corr), self.vtc_walls, K_WAL, self.field_line_ver))
        self.field_map = np.log(self.field_map+1)
        print('Done!')
        
    # Util
    def util_error(self):
        print('')
        print('***********')
        print('*** No! ***')
        print('***********')
        print('')
        self.close()
    
    def util_mouse(self):
        MX = (self.mouse_x-self.grid_SP.x)//(self.grid_size.x)
        MY = (self.mouse_y-self.grid_SP.y)//(self.grid_size.y)
        
        if (MX<0):
            MX = 0
        elif (MX>=self.grid_num.x):
            MX = self.grid_num.x - 1
        if (MY<0):
            MY = 0
        elif (MY>=self.grid_num.y):
            MY = self.grid_num.y - 1
            
        self.vec_mouse = Vec2(MX,MY)
    
    def util_exist(self, vec, lst):
        idx = -1
        if (vec in lst):
            idx = lst.index(vec)
        return idx
    
    def util_save(self):
        data = {}
        data['Obstacles'] = self.vec_obstacles
        data['Walls'] = self.vec_walls
        data['Player Spawn Points'] = self.vec_pla_spawns
        data['Opponent Spawn Points'] = self.vec_opp_spawns
        with open(f'{find_directory()}/Data/map.pickle', 'wb') as f:
            pkl.dump(data, f)
    
    def util_vertice(self, lst):
        vertices = []

        vertice_temp = []
        is_point = False
        for sx in range(self.grid_num.x):
            if (is_point): # End vertice (Last Case)
                if (vertice_temp[0] != Vec2(sx-1,self.grid_num.y-1)):
                    vertice_temp.append(Vec2(sx-1,self.grid_num.y-1))
                    if (len(vertice_temp) != 2):
                        self.util_error()
                    vertices.append(vertice_temp.copy())
                vertice_temp = []
            is_point = False
            is_point_before = False
            for sy in range(self.grid_num.y):
                is_point = (Vec2(sx,sy) in lst)
                if (is_point) and (not is_point_before): # Start vertice
                    is_point_before = True
                    vertice_temp.append(Vec2(sx,sy))
                    if (len(vertice_temp) != 1):
                        self.util_error()
                elif (not is_point) and (is_point_before): # End vertice
                    is_point_before = False
                    if (vertice_temp[0] != Vec2(sx,sy-1)):
                        vertice_temp.append(Vec2(sx,sy-1))
                        if (len(vertice_temp) != 2):
                            self.util_error()
                        vertices.append(vertice_temp.copy())
                    vertice_temp = []
        if (is_point): # End vertice (Last Case)
            if (vertice_temp[0] != Vec2(self.grid_num.x-1,self.grid_num.y-1)):
                vertice_temp.append(Vec2(self.grid_num.x-1,self.grid_num.y-1))
                if (len(vertice_temp) != 2):
                    self.util_error()
                vertices.append(vertice_temp.copy())
            vertice_temp = []
        
        vertice_temp = []
        is_point = False
        for sy in range(self.grid_num.y):
            if (is_point): # End vertice (Last Case)
                if (vertice_temp[0] != Vec2(self.grid_num.x-1,sy-1)):
                    vertice_temp.append(Vec2(self.grid_num.x-1,sy-1))
                    if (len(vertice_temp) != 2):
                        self.util_error()
                    vertices.append(vertice_temp.copy())
                vertice_temp = []
            is_point = False
            is_point_before = False
            for sx in range(self.grid_num.x):
                is_point = (Vec2(sx,sy) in lst)
                if (is_point) and (not is_point_before): # Start vertice
                    is_point_before = True
                    vertice_temp.append(Vec2(sx,sy))
                    if (len(vertice_temp) != 1):
                        self.util_error()
                elif (not is_point) and (is_point_before): # End vertice
                    is_point_before = False
                    if (vertice_temp[0] != Vec2(sx-1,sy)):
                        vertice_temp.append(Vec2(sx-1,sy))
                        if (len(vertice_temp) != 2):
                            self.util_error()
                        vertices.append(vertice_temp.copy())
                    vertice_temp = []
        if (is_point): # End vertice (Last Case)
            if (vertice_temp[0] != Vec2(self.grid_num.x-1,self.grid_num.y-1)):
                vertice_temp.append(Vec2(self.grid_num.x-1,self.grid_num.y-1))
                if (len(vertice_temp) != 2):
                    self.util_error()
                vertices.append(vertice_temp.copy())
            vertice_temp = []
        
        return vertices
    
    # Step
    def step_mouse(self, action):
        """
        1 : Left
        2 : Middle
        3 : Right
        4 : Scroll Up
        5 : Scroll Down
        """
        # Action(Mouse)
        if (self.pla_role == 'Editor'):
            if (action==1): # build or remove
                pointer_vec = self.vec_mouse
                if (self.mouse_mode==0):
                    pointer_list = self.vec_obstacles
                elif (self.mouse_mode==1):
                    pointer_list = self.vec_walls
                elif (self.mouse_mode==2):
                    pointer_list = self.vec_pla_spawns
                elif (self.mouse_mode==3):
                    pointer_list = self.vec_opp_spawns
                idx = self.util_exist(vec=pointer_vec, lst=pointer_list)
                if (idx==-1):
                    pointer_list.append(pointer_vec)
                else:
                    del pointer_list[idx]
            elif (action==3): # 리셋
                self.vec_obstacles = []
                self.vec_walls = []
                self.vec_pla_spawns = []
                self.vec_opp_spawns = []
            elif (action==4):
                self.mouse_mode = (self.mouse_mode - 1) % 4
            elif (action==5):
                self.mouse_mode = (self.mouse_mode + 1) % 4
    
    def step(self, action):
        # Mode Change
        if (action==-1):
            self.init_mode()
        elif (action==-2):
            self.field_mode = (self.field_mode + 1) % 3
            self.init_fieldmap()
        
        if (self.pla_role == 'Predator'): # chasing
            pointer_pla = self.predator
            pointer_opp = self.prey
        elif (self.pla_role == 'Prey'): # chased
            pointer_pla = self.prey
            pointer_opp = self.predator
        
        if (self.role_active):
            if (action==0): # Forward
                pointer_pla.move(self.pla_spd)
                pointer_opp.move_AI(self.opp_spd, pointer_pla.vec_pos, self.vtc_obstacles, self.vtc_walls, self.field_line_ver, self.field_point_ver)
            elif (action==1): # Left
                pointer_pla.rotation(-self.pla_rspd)
            elif (action==2): # Right
                pointer_pla.rotation(self.pla_rspd)
    
    
    def render(self):
        self.screen.fill((192,192,192))
        pg.draw.rect(self.screen, (93,114,59), (*(self.grid_SP), *(self.grid_size @ self.grid_num)))
        
        # Text
        self.screen.blit(self.G_font.render(f'Mode: {self.pla_role}', False, (0,0,0)), (0,0))
        if (self.pla_role == 'Editor'):
            if (self.mouse_mode==0):
                txt_block = 'Obstacle'
            elif (self.mouse_mode==1):
                txt_block = 'Wall'
            elif (self.mouse_mode==2):
                txt_block = 'Player Spawn'
            elif (self.mouse_mode==3):
                txt_block = 'Opponent Spawn'
            self.screen.blit(self.G_font.render(f'Selected: {txt_block}', False, (0,0,0)), (0,16))
        
        # Obstacle Block
        for obstacle in self.vec_obstacles:
            pg.draw.rect(self.screen, (64,64,64),
                         (*(self.grid_size @ obstacle + self.grid_SP), *self.grid_size))
        
        # Wall Block
        for wall in self.vec_walls:
            pg.draw.rect(self.screen, (0,0,0),
                         (*(self.grid_size @ wall + self.grid_SP), *self.grid_size))
        
        # Player Spawn Point
        for spawn_pla in self.vec_pla_spawns:
            pg.draw.rect(self.screen, (0,0,255),
                         (*(self.grid_size @ spawn_pla + self.grid_SP), *self.grid_size))
        
        # Opponent Spawn Point
        for spawn_opp in self.vec_opp_spawns:
            pg.draw.rect(self.screen, (0,255,255),
                         (*(self.grid_size @ spawn_opp + self.grid_SP), *self.grid_size))
        
        # Select Block & Roles
        if (self.pla_role == 'Editor'):
            self.util_mouse()
            pg.draw.rect(self.screen, (255,255,255),
                         (*(self.grid_size @ self.vec_mouse + self.grid_SP), *self.grid_size))
        elif (self.role_active):
            self.screen.blit(self.prey.spr, 
                             (*(self.grid_size @ self.prey.vec_pos + self.grid_SP + (self.grid_size//4)),
                             *(self.grid_size//2)))
            self.screen.blit(self.predator.spr, 
                             (*(self.grid_size @ self.predator.vec_pos + self.grid_SP + (self.grid_size//4)),
                             *(self.grid_size//2)))
        
        # Field map
        if (self.field_mode != 0):
            for ssx in range(self.field_map.shape[0]):
                for ssy in range(self.field_map.shape[1]):
                    c = int(255*self.field_map[ssx,ssy]/np.max(self.field_map))
                    pg.draw.rect(self.screen, (c,0,0),
                                 (*((self.grid_size//self.field_freq) @ Vec2(ssx,ssy) + self.grid_SP),
                                 *(self.grid_size//self.field_freq)))
        
        self.G_clock.tick(G_FPS)
        pg.display.update()
        
    
    def close(self):
        pg.quit()


class CaC_Role:
    def __init__(self, K_OPP=0, K_OBS=0, K_WAL=0):
        self.img = None
        self.spr = None
        
        self.K_OPP = K_OPP
        self.K_OBS = K_OBS
        self.K_WAL = K_WAL
        
        self.vec_pos = Vec2(0,0)
        self.vec_rot = Vec2(1.0,0.0)
    
    def warp(self, vec_warp):
        self.vec_pos = vec_warp
        
    def move(self, spd):
        self.vec_pos += spd * self.vec_rot
    
    def move_AI(self, spd, OPP, OBS, WAL, line_ver, point_ver):
        field = Field_Point(self.vec_pos, OPP, self.K_OPP, point_ver) + Field_Line(self.vec_pos, OBS, self.K_OBS, line_ver) + Field_Line(self.vec_pos, WAL, self.K_WAL, line_ver)
        self.vec_rot = field.unit()
        self.vec_pos += spd * self.vec_rot
    
    def rotation(self, deg):
        self.vec_rot = self.vec_rot.rotation(deg=deg).unit()
        #if (self.spr != None):
        #    self.spr = pg.transform.rotate(self.spr, deg)

#%%
def Field_Point(vec_p, vec_o, K=1, version=0):
    r = (vec_p - vec_o)
    if (version == 0):
        field = (K/(abs(r)**3)) * r
    elif (version == 1):
        field = (K/(abs(r)**2)) * r
    elif (version == 2):
        field = (K*(abs(r)**(-0.5))) * r
    
    return field

def Field_Line(vec_p, lst_vertices, K=1, version=0):
    field = Vec2(0,0)
    
    for vertice in lst_vertices:
        a = vertice[0] - vec_p
        b = vertice[1] - vec_p
        
        basis_u = (a-b).unit()
        
        l_a = a.dot(basis_u)
        l_b = b.dot(basis_u)
        
        h = (-l_b*a + l_a*b)/abs(a-b)
        D = abs(h)
        
        if (version == 0):
            if (D < 0.01):
                subfield = (K*((1/l_a)-(1/l_b))) * basis_u
            else:
                basis_n = h.unit()
                subfield = ((-K/D)*((l_a/((D**2+l_a**2)**0.5))-(l_b/((D**2+l_b**2)**0.5)))) * basis_n + ((K/((D**2+l_a**2)**0.5))-(K/((D**2+l_b**2)**0.5))) * basis_u
        elif (version == 1):
            if (D < 0.01):
                subfield = (-K * np.log(abs(l_a/l_b))) * basis_u
            else:
                basis_n = h.unit()
                subfield = (-K)*(np.arctan(l_a/D)-np.arctan(l_b/D)) * basis_n + (-K/2)*((np.log(D**2+l_a**2))-(np.log(D**2+l_b**2))) * basis_u
        
        field = field + subfield
        
    return field

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


#%%
map_x = 33
map_y = 33

G_FPS = 24

env = Env_CaC(grid_num=(map_x,map_y), data_map='Map01.pickle', 
              field_line_ver=0, field_point_ver=1)
key_up = False
key_left = False
key_right = False

while True:
    env.render()
    if (key_up):
        env.step(0)
    if (key_left):
        env.step(1)
    if (key_right):
        env.step(2)
    for event in pg.event.get():
        if (event.type == pg.QUIT):
            env.close()
            break
        if (event.type == pg.KEYDOWN):
            if (event.key == pg.K_m):
                env.step(-1)
            elif (event.key == pg.K_f):
                env.step(-2)
            elif (event.key == pg.K_s):
                env.util_save()
                env.close()
            elif (event.key == pg.K_r):
                env.init_spawn()
            if (event.key == pg.K_UP):
                key_up = True
            if (event.key == pg.K_LEFT):
                key_left = True
            if (event.key == pg.K_RIGHT):
                key_right = True
        if (event.type == pg.KEYUP):
            if (event.key == pg.K_UP):
                key_up = False
            if (event.key == pg.K_LEFT):
                key_left = False
            if (event.key == pg.K_RIGHT):
                key_right = False
        if (event.type == pg.MOUSEMOTION):
            env.mouse_x, env.mouse_y = pg.mouse.get_pos() # 마우스 x,y좌표값 저장
        if (event.type == pg.MOUSEBUTTONDOWN):
            env.step_mouse(event.button)