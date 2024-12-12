#%% 마우스로 선 그리기
import pygame as pg

import numpy as np
import os
import itertools


#%% Game System

def load_image(file):
    """loads an image, prepares it for play"""
    file = os.path.join(os.path.split(os.path.abspath(__file__))[0], "Data", file)
    try:
        surface = pg.image.load(file)
    except:
        pg.quit()
        raise SystemExit(f'Could not load image "{file}"')
    return surface.convert_alpha()


#%%
class Env_CaC():
    def __init__(self, grid_num=(10,10), speed_prey=8, speed_predator=8):
        self.mode = 0
        self.show_field = 0
        
        # 그리드 설정
        self.grid_num = grid_num
        self.grid_size = 32
        self.grid_SP = (64,64) # 그리드가 시작되는 위치, 즉 그리드 왼쪽 상단의 위치
        self.grid_EP = (self.grid_SP[0]+self.grid_num[0]*self.grid_size, 
                        self.grid_SP[1]+self.grid_num[1]*self.grid_size)
        
        self.mouse_x = 0
        self.mouse_y = 0
        self.MX = 0
        self.MY = 0
        
        self.speed_prey = speed_prey
        self.speed_predator = speed_predator
        
        self.freq = int(self.grid_size/4)
        self.map_prey = np.zeros((grid_num[0]*self.freq, grid_num[1]*self.freq))
        self.map_predator = np.zeros((grid_num[0]*self.freq, grid_num[1]*self.freq))
        
        self.obstacle = []
        self.vec_obstacles = [] # center 위치 픽셀 좌표
        self.wall = []
        self.vec_walls = []
        self.wall.append((-1,-1))
        self.wall.append((-1,grid_num[1]))
        self.wall.append((grid_num[0],-1))
        self.wall.append(grid_num)
        for wall in itertools.product(np.arange(0,grid_num[0]),np.array([-1,grid_num[1]])):
            self.wall.append(wall)
        for wall in itertools.product(np.array([-1,grid_num[0]]),np.arange(0,grid_num[1])):
            self.wall.append(wall)
        
        for wall in self.wall:
            self.vec_walls.append(Vec2(self.grid_SP[0]+(wall[0]*self.grid_size)+self.grid_size//2, 
                                       self.grid_SP[1]+(wall[1]*self.grid_size)+self.grid_size//2))
        
        self.render_initialize()
    
    
    def step(self, action):
        # Mode Change
        if (action==0):
            self.mode = (self.mode+1)%4
            if (self.mode == 0):
                self.prey.__del__()
                self.predator.__del__()
            else:
                available = False
                while not available:
                    available = True
                    prey_pos = (np.random.randint(0,self.grid_num[0]), np.random.randint(0,self.grid_num[1]))
                    predator_pos = (np.random.randint(0,self.grid_num[0]), np.random.randint(0,self.grid_num[1]))
                    if (prey_pos == predator_pos):
                        available = False
                    for pos in self.obstacle:
                        if (pos == prey_pos) or (pos == predator_pos):
                            available = False
                
                self.prey = self.Prey(self, (self.mode%2==1), np.array(prey_pos)*self.grid_size+self.grid_SP)
                self.predator = self.Predator(self, (self.mode>=2), np.array(predator_pos)*self.grid_size+self.grid_SP)
        
        if (self.mode==0): # edit
            self.MX = (self.mouse_x-self.grid_SP[0])//self.grid_size
            self.MY = (self.mouse_y-self.grid_SP[1])//self.grid_size
            if (action==1): # 설치
                pos = (self.MX, self.MY)
                if pos in self.obstacle: # 이미 설치되었다면 지움
                    idx = self.obstacle.index(pos)
                    del self.obstacle[idx]
                    del self.vec_obstacles[idx]
                else: # 없다면 설치
                    if ((pos[0]<self.MX_max) and (pos[1]<self.MY_max)) and ((pos[0]>=0) and (pos[1]>=0)):
                        self.obstacle.append(pos)
                        self.vec_obstacles.append(Vec2(self.grid_SP[0]+(pos[0]*self.grid_size)+self.grid_size//2, 
                                                       self.grid_SP[1]+(pos[1]*self.grid_size)+self.grid_size//2))
            elif (action==2): # 리셋
                self.obstacle = []
                self.vec_obstacles = []
        elif (self.mode==1): # chasing
            if (action==1):
                self.predator.pos[0] += self.speed_predator
            elif (action==2):
                self.predator.pos[1] -= self.speed_predator
            elif (action==3):
                self.predator.pos[0] -= self.speed_predator
            elif (action==4):
                self.predator.pos[1] += self.speed_predator
            self.prey.move()
        elif (self.mode==2): # chased
            if (action==1):
                self.prey.pos[0] += self.speed_prey
            elif (action==2):
                self.prey.pos[1] -= self.speed_prey
            elif (action==3):
                self.prey.pos[0] -= self.speed_prey
            elif (action==4):
                self.prey.pos[1] += self.speed_prey
            self.predator.move()
        elif (self.mode==3): # AI
            self.prey.move()
            self.predator.move()
            
    
    def render_initialize(self):
        pg.init()
        pg.display.set_caption('CaC')
        
        # Screen 생성
        self.screen = pg.display.set_mode((640,640))
        
        # clock 생성
        self.G_clock = pg.time.Clock()
        self.G_font = pg.font.SysFont('Arial', 24)
        
        self.MX_max = self.grid_num[0] # 미만
        self.MY_max = self.grid_num[1] # 미만
        
        # 스프라이트 생성
        self.spr_select = pg.transform.scale(load_image("Grid_Selected.png"), (self.grid_size,self.grid_size))
        self.spr_block = pg.transform.scale(load_image("Grid_White.png"), (self.grid_size,self.grid_size))
        self.spr_wall = pg.transform.scale(load_image("Grid_Green.png"), (self.grid_size,self.grid_size))
        self.spr_predator = pg.transform.scale(load_image("Spr_Yellow.png"), (self.grid_size//2,self.grid_size//2))
        self.spr_prey = pg.transform.scale(load_image("Spr_Cyan.png"), (self.grid_size//2,self.grid_size//2))
        
        # 초기 렌더
        self.screen.fill((0,0,0)) # 화면 검은색으로
    
    
    def render(self):
        self.screen.fill((0,0,0)) # 화면 검은색으로
        
        # field show 나중수정
        if (self.show_field!=0): # Field On
            if (self.show_field==1): # Prey
                for x in range(self.map_prey.shape[0]):
                    for y in range(self.map_prey.shape[0]):
                        subscalar = 0
                        for obstacle in self.vec_obstacles:
                            subscalar += Field_Point_Scalar(((x+0.5)*self.freq+self.grid_SP[0], (y+0.5)*self.freq+self.grid_SP[1]), 
                                                            tuple(obstacle), 
                                                            K=self.prey.k_obstacle, threshold=self.prey.thres, scale=self.prey.vec_scale)
                        for wall in self.vec_walls:
                            subscalar += Field_Point_Scalar(((x+0.5)*self.freq+self.grid_SP[0], (y+0.5)*self.freq+self.grid_SP[1]), 
                                                            tuple(wall), 
                                                            K=self.prey.k_wall, threshold=self.prey.thres, scale=self.prey.vec_scale)
                        subscalar += Field_Point_Scalar(((x+0.5)*self.freq+self.grid_SP[0], (y+0.5)*self.freq+self.grid_SP[1]), 
                                                        tuple(self.predator.pos),
                                                        K=self.predator.k_self, threshold=self.prey.thres, scale=self.prey.vec_scale)
                        
                        self.map_prey[x,y] = subscalar
            
            # 나중 수정
            self.map_field = self.map_prey/abs(self.map_prey).max()
            # self.map_field = np.log((self.map_prey/abs(self.map_prey).max())+1)
            for x in range(self.map_field.shape[0]):
                for y in range(self.map_field.shape[0]):
                    scalar = self.map_field[x,y]
                    if (scalar>=0):
                        color = (255*scalar,0,0)
                    else:
                        color = (0,0,255*scalar)
                    pg.draw.rect(self.screen, color, 
                                 [x*self.freq+self.grid_SP[0], y*self.freq+self.grid_SP[1], self.freq, self.freq])
        
        # Select Block
        if (self.mode==0): # edit
            self.MX = (self.mouse_x-self.grid_SP[0])//self.grid_size
            self.MY = (self.mouse_y-self.grid_SP[1])//self.grid_size
            if ((self.MX<self.MX_max) and (self.MY<self.MY_max)) and ((self.MX>=0) and (self.MY>=0)):
                self.screen.blit(self.spr_select,(self.grid_SP[0]+self.grid_size*self.MX,
                                                  self.grid_SP[1]+self.grid_size*self.MY,
                                                  self.grid_size, self.grid_size))
        
        ## Wall Block
        for pos in self.wall:
            self.screen.blit(self.spr_wall,(pos[0]*self.grid_size+self.grid_SP[0],
                                            pos[1]*self.grid_size+self.grid_SP[1],
                                            self.grid_size, self.grid_size))
        
        # Obstacle Block
        for pos in self.obstacle:
            self.screen.blit(self.spr_block,(pos[0]*self.grid_size+self.grid_SP[0],
                                             pos[1]*self.grid_size+self.grid_SP[1],
                                             self.grid_size, self.grid_size))
        
        # Play
        if (self.mode!=0):
            self.prey.rect.topleft = list(self.prey.pos)
            self.screen.blit(self.prey.spr, self.prey.rect)
            self.predator.rect.topleft = list(self.predator.pos)
            self.screen.blit(self.predator.spr, self.predator.rect)
            
            txts = []
            txts.append(self.G_font.render(f'py_wall = {self.prey.k_wall:.1f}', True, (255,255,255)))
            txts.append(self.G_font.render(f'pd_wall = {self.predator.k_wall:.1f}', True, (255,255,255)))
            txts.append(self.G_font.render(f'py_obstacle = {self.prey.k_obstacle:.1f}', True, (255,255,255)))
            txts.append(self.G_font.render(f'pd_obstacle = {self.predator.k_obstacle:.1f}', True, (255,255,255)))
            txts.append(self.G_font.render(f'py = {self.prey.k_self:.1f}', True, (255,255,255)))
            txts.append(self.G_font.render(f'pd = {self.predator.k_self:.1f}', True, (255,255,255)))
            for idx, txt in enumerate(txts):
                self.screen.blit(txt, (self.grid_EP[0]+self.grid_size*2, self.grid_SP[1]+64*idx))
        
        
        self.G_clock.tick(G_FPS)
        pg.display.update()
    
    
    def close(self):
        pg.quit()
    
    
    ## Game Component
    # Prey
    class Prey:
        def __init__(self, env, AI=False, pos=[0,0]):
            self.env = env
            
            self.AI = AI
            self.speed = env.speed_prey
            
            self.pos = Vec2(pos[0],pos[1]) # 픽셀 좌표
            self.spr = env.spr_prey
            self.rect = self.spr.get_rect()
            
            self.field = Vec2(0,0)
            self.thres = 100
            
            self.vec_scale = (1/self.env.grid_size)
            self.k_self = -10
            self.k_wall = 3
            self.k_obstacle = 1
            
        
        def __del__(self):
            pass
        
        def move(self):
            self.field = Vec2(0,0)
            
            # Predator Field
            subfield = Field_Point(self.pos, env.predator.pos, K=env.predator.k_self, threshold=self.thres, scale=self.vec_scale)
            self.field += subfield
            
            # Wall Field
            for wall in self.env.vec_walls:
                subfield = Field_Point(self.pos, wall, K=self.k_wall, threshold=self.thres, scale=self.vec_scale)
                self.field += subfield
            
            # Obstacle Field
            for obstacle in self.env.vec_obstacles:
                subfield = Field_Point(self.pos, obstacle, K=self.k_obstacle, threshold=self.thres, scale=self.vec_scale)
                self.field += subfield
            
            # Move by Field
            force = self.field.unit()*self.speed
            self.pos += force
            
            # Check Vaildity (Wall)
            if (self.pos[0] < self.env.grid_SP[0]):
                self.pos[0] = self.env.grid_SP[0]
            elif (self.pos[0] > self.env.grid_EP[0]):
                self.pos[0] = self.env.grid_EP[0]
            if (self.pos[1] < self.env.grid_SP[1]):
                self.pos[1] = self.env.grid_SP[1]
            elif (self.pos[1] > self.env.grid_EP[1]):
                self.pos[1] = self.env.grid_EP[1]
            
            # Check Vaildity (Obstacle)
            for obstacle in self.env.vec_obstacles:
                left = obstacle[0]-self.env.grid_size//2
                top = obstacle[1]-self.env.grid_size//2
                right = obstacle[0]+self.env.grid_size//2
                bottom = obstacle[1]+self.env.grid_size//2
                if all((self.pos[0]>left, self.pos[1]>top, self.pos[0]<right, self.pos[1]<bottom)):
                    self.pos -= force
            
    
    # Predator
    class Predator:
        def __init__(self, env, AI=False, pos=[0,0]):
            self.env = env
            
            self.AI = AI
            self.speed = env.speed_predator
            
            self.pos = Vec2(pos[0],pos[1]) # 픽셀 좌표
            self.spr = env.spr_predator
            self.rect = self.spr.get_rect()
            
            self.field = Vec2(0,0)
            self.thres = 100
            
            self.vec_scale = (1/self.env.grid_size)
            self.k_self = 10
            self.k_wall = 3
            self.k_obstacle = 1
        
        def __del__(self):
            pass
        
        def move(self):
            self.field = Vec2(0,0)
            
            # Prey Field
            subfield = Field_Point(self.pos, env.prey.pos, K=env.prey.k_self, threshold=self.thres, scale=self.vec_scale)
            self.field += subfield
            
            # Wall Field
            for wall in self.env.vec_walls:
                subfield = Field_Point(self.pos, wall, K=self.k_wall, threshold=self.thres, scale=self.vec_scale)
                self.field += subfield
            
            # Obstacle Field
            for obstacle in self.env.vec_obstacles:
                subfield = Field_Point(self.pos, obstacle, K=self.k_obstacle, threshold=self.thres, scale=self.vec_scale)
                self.field += subfield
            
            # Move by Field
            force = self.field.unit()*self.speed
            self.pos += force
            
            # Check Vaildity (Wall)
            if (self.pos[0] < self.env.grid_SP[0]):
                self.pos[0] = self.env.grid_SP[0]
            elif (self.pos[0] > self.env.grid_EP[0]):
                self.pos[0] = self.env.grid_EP[0]
            if (self.pos[1] < self.env.grid_SP[1]):
                self.pos[1] = self.env.grid_SP[1]
            elif (self.pos[1] > self.env.grid_EP[1]):
                self.pos[1] = self.env.grid_EP[1]
            
            # Check Vaildity (Obstacle)
            for obstacle in self.env.vec_obstacles:
                left = obstacle[0]-self.env.grid_size//2
                top = obstacle[1]-self.env.grid_size//2
                right = obstacle[0]+self.env.grid_size//2
                bottom = obstacle[1]+self.env.grid_size//2
                if all((self.pos[0]>left, self.pos[1]>top, self.pos[0]<right, self.pos[1]<bottom)):
                    self.pos -= force


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

    def __add__(self, other):
        return Vec2(self.x + other.x, self.y + other.y)
    
    def __radd__(self, other):
        return Vec2(self.x + other.x, self.y + other.y)

    def __mul__(self, num):
        return Vec2(num * self.x, num * self.y)
    
    def __rmul__(self, num):
        return Vec2(num * self.x, num * self.y)

    def __neg__(self):
        return Vec2(-self.x, -self.y)

    def __sub__(self, other):
        return Vec2(self.x - other.x, self.y - other.y)
    
    def __truediv__(self, num):
        return Vec2(self.x / num, self.y / num)

    def __repr__(self):
        return f"<{self.x}, {self.y}>"

    def __abs__(self):
        return (self.x**2 + self.y**2)**0.5
    
    def __getitem__(self, item):
        return self.values.__getitem__(item)
    
    def __setitem__(self, key, value):
        self.values.__setitem__(key, value)
        self.x, self.y = self.values

    def dot(self, other):
        return self.x*other.x + self.y*other.y

    def unit(self):
        if abs(self)==0:
            return self
        else:
            return self*(1/abs(self))


#%%
map_x = 10
map_y = 10

G_FPS = 30

env = Env_CaC(grid_num=(map_x,map_y))

while True:
    env.render()
    for event in pg.event.get():
        if event.type == pg.QUIT:
            env.close()
            break
        if (event.type == pg.KEYDOWN) and (event.key == pg.K_m):
            env.step(0)
        elif (event.type == pg.KEYDOWN) and (event.key == pg.K_n):
            env.show_field = (env.show_field+1)%3
        if (env.mode == 0): # edit
            if event.type == pg.MOUSEMOTION: # 마우스의 움직임이 감지되면
                env.mouse_x, env.mouse_y = pg.mouse.get_pos() # 마우스 x,y좌표값 저장
            if event.type == pg.MOUSEBUTTONDOWN: # 마우스 클릭을 하면
                if event.button == 1: # 왼쪽 클릭
                    env.step(1)
                elif event.button == 3: # 오른쪽 클릭
                    env.step(2)
        elif (env.mode != 0): # play
            if event.type == pg.KEYDOWN:
                if event.key == pg.K_RIGHT:
                    env.step(1)
                elif event.key == pg.K_UP:
                    env.step(2)
                elif event.key == pg.K_LEFT:
                    env.step(3)
                elif event.key == pg.K_DOWN:
                    env.step(4)
                if event.key == pg.K_q:
                    env.prey.k_wall += 0.2
                elif event.key == pg.K_a:
                    env.prey.k_wall -= 0.2
                elif event.key == pg.K_w:
                    env.predator.k_wall += 0.2
                elif event.key == pg.K_s:
                    env.predator.k_wall -= 0.2
                elif event.key == pg.K_e:
                    env.prey.k_obstacle += 0.2
                elif event.key == pg.K_d:
                    env.prey.k_obstacle -= 0.2
                elif event.key == pg.K_r:
                    env.predator.k_obstacle += 0.2
                elif event.key == pg.K_f:
                    env.predator.k_obstacle -= 0.2
                elif event.key == pg.K_t:
                    env.prey.k_self += 0.2
                elif event.key == pg.K_g:
                    env.prey.k_self -= 0.2
                elif event.key == pg.K_y:
                    env.predator.k_self += 0.2
                elif event.key == pg.K_h:
                    env.predator.k_self -= 0.2