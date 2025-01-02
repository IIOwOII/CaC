#%%
import pygame as pg

import numpy as np
import pickle as pkl
import os

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
    def __init__(self, grid_num=(33,33), data_map=''):
        # Components
        self.mode_num = -1
        
        # Player
        self.pla_role = 'First'
        self.pla_spd = 0.5
        self.pla_dir = Vec2(1.0,0.0)
        self.pla_spd_rot = 0.5 * (np.pi/180) # rad
        
        # Roles
        self.prey = CaC_Role()
        self.predator = CaC_Role()
        self.role_active = False
        
        # 그리드 설정
        self.grid_num = Vec2(*grid_num)
        if (data_map == ''):
            pass
        else:
            with open(data_map, 'rb') as f:
                data = pkl.load(f)
                self.grid_num = data['Grid Number']
        self.grid_size = Vec2(16,16)
        self.grid_SP = Vec2(64,64) # 그리드가 시작되는 위치, 즉 그리드 왼쪽 상단의 위치
        
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
        self.vec_obstacles = []
        self.vec_walls = []
        self.vec_pla_spawns = []
        self.vec_opp_spawns = []
        if (data_map == ''):
            pass
        else:
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
        self.screen = pg.display.set_mode((640,640))
        
        # clock 생성
        self.G_clock = pg.time.Clock()
        self.G_font = pg.font.SysFont('Arial', 24)
        
        # 스프라이트 생성
        self.spr_predator = pg.transform.scale(load_image("Spr_Yellow.png"), tuple(self.grid_size//2))
        self.spr_prey = pg.transform.scale(load_image("Spr_Cyan.png"), tuple(self.grid_size//2))
        
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
            self.role_active = True
            self.init_spawn()
    
    def init_spawn(self):
        if (~self.vec_pla_spawns or ~self.vec_opp_spawns):
            self.util_error()
        elif (~self.role_active):
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
            
    # Util
    def util_error(self):
        print('No!')
        pg.close()
    
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
    
    def util_exist(self, _vec, _list):
        idx = -1
        if _vec in _list:
            idx = _list.index(_vec)
        return idx
    
    def util_save(self):
        data = {}
        data['Obstacles'] = self.vec_obstacles
        data['Walls'] = self.vec_walls
        data['Player Spawn Points'] = self.vec_pla_spawns
        data['Opponent Spawn Points'] = self.vec_opp_spawns
        with open(f'{find_directory()}/Data/map.pickle', 'wb') as f:
            pkl.dump(data, f)
    
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
                idx = self.util_exist(pointer_vec, pointer_list)
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
        
        if (self.pla_role == 'Predator'): # chasing
            pointer_pla = self.predator
            pointer_opp = self.prey
        elif (self.pla_role == 'Prey'): # chased
            pointer_pla = self.prey
            pointer_opp = self.predator
        
        if (self.role_active):
            if (action==0): # Forward
                pointer_pla.vec_pos += self.pla_spd * self.pla_dir
            if (action==1): # Left
                self.pla_dir = self.pla_dir.rotation(self.pla_spd_rot).unit()
            elif (action==2): # Right
                self.pla_dir = self.pla_dir.rotation(-self.pla_spd_rot).unit()
    
    
    def render(self):
        self.screen.fill((192,192,192)) # 화면 검은색으로
        
        # Select Block & Roles
        if (self.pla_role == 'Editor'):
            self.util_mouse()
            pg.draw.rect(self.screen, (255,255,255),
                         (*(self.grid_size @ self.vec_mouse + self.grid_SP), *self.grid_size))
        elif (self.role_active):
            self.screen.blit(self.spr_prey, 
                             (*(self.grid_size @ self.prey.vec_pos + self.grid_SP + (self.grid_size//4)),
                             *(self.grid_size//2)))
            self.screen.blit(self.spr_predator, 
                             (*(self.grid_size @ self.predator.vec_pos + self.grid_SP + (self.grid_size//4)),
                             *(self.grid_size//2)))
        
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
        
        self.G_clock.tick(G_FPS)
        pg.display.update()
        
    
    def close(self):
        pg.quit()


class CaC_Role:
    def __init__(self, AI=False, base_speed=1, 
                 K_OPP=0, K_OBS=0, K_WAL=0):
        self.K_OPP = K_OPP
        self.K_OBS = K_OBS
        self.K_WAL = K_WAL
        
        self.AI = AI
        self.base_speed = base_speed
        
        self.vec_pos = Vec2(0,0)
    
    def warp(self, vec_warp):
        self.vec_pos = vec_warp
        
    def move(self, vec_delta, depend_speed=True):
        if (self.active):
            if (depend_speed):
                self.vec_pos += self.base_speed * vec_delta.unit()
            else:
                self.vec_pos += vec_delta
    

#%%
def Field_Point(vec_ref, vec_point, K=1, threshold=10, scale=0.01):
    vec_ref *= scale
    vec_point *= scale
    field = (vec_ref-vec_point).unit()/(abs(vec_ref-vec_point)**2)*K
    if abs(field) > threshold:
        field = field/abs(field)*threshold
    
    return field


def Field_Point_Scalar(pos_ref, pos_point, K=1, threshold=100, scale=0.1):
    scalar = K/((scale**2)*((pos_ref[0]-pos_point[0])**2+(pos_ref[1]-pos_point[1])**2))
    if scalar > threshold:
        scalar = threshold
    
    return scalar


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
    
    def rotation(self, rad):
        _cos = np.cos(rad)
        _sin = np.sin(rad)
        return Vec2(_cos * self.x - _sin * self.y, _sin * self.x + _cos * self.y)


#%%
map_x = 33
map_y = 33

G_FPS = 24

env = Env_CaC(grid_num=(map_x,map_y))

while True:
    env.render()
    for event in pg.event.get():
        if (event.type == pg.QUIT):
            env.close()
            break
        if (event.type == pg.KEYDOWN):
            if (event.key == pg.K_m):
                env.step(-1)
            elif (event.key == pg.K_s):
                env.util_save()
                env.close()
            elif (event.key == pg.K_r):
                env.init_spawn()
            elif (event.key == pg.K_UP):
                env.step(0)
            elif (event.key == pg.K_LEFT):
                env.step(1)
            elif (event.key == pg.K_RIGHT):
                env.step(2)
        if (event.type == pg.MOUSEMOTION):
            env.mouse_x, env.mouse_y = pg.mouse.get_pos() # 마우스 x,y좌표값 저장
        if (event.type == pg.MOUSEBUTTONDOWN):
            env.step_mouse(event.button)