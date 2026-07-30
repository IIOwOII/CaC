#%% Library
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import LinearSegmentedColormap
import matplotlib.patches as patches

import json
import itertools
from copy import deepcopy
import math

#%% plotting
class meowfig:
    def __init__(self, nrows=1, ncols=1, figsize=(4,3), dpi=300, 
                 design=True, grid=False, **kwargs):
        """
        Parameters
        ----------
        nrows, ncols : int
            The default is 1.
        size : tuple
            The default is (4,3).
        dpi : int
            The default is 300.
        **kwargs : optional
            title, xlabel, ylabel, xlim, ylim, xticks, yticks, xticklabels, yticklabels...
        """
        # essential
        self.dpi = dpi
        self.design = design
        self.grid = grid
        self.nrows = nrows
        self.ncols = ncols
        self.figsize = (figsize[0]*ncols, figsize[1]*nrows)
        
        # optional
        self.title = kwargs.get('title')
        self.xlabel = kwargs.get('xlabel')
        self.ylabel = kwargs.get('ylabel')
        self.xlim = kwargs.get('xlim')
        self.ylim = kwargs.get('ylim')
        self.xticks = kwargs.get('xticks')
        self.yticks = kwargs.get('yticks')
        self.xticklabels = kwargs.get('xticklabels')
        self.yticklabels = kwargs.get('yticklabels')
        self.xoffset = kwargs.get('xoffset')
        self.yoffset = kwargs.get('yoffset')
        
        # figure setting
        self.fig, self.axes = plt.subplots(nrows=self.nrows, ncols=self.ncols, 
                                           figsize=self.figsize, dpi=self.dpi)
        if self.nrows==1 and self.ncols==1: 
            self.axes = np.array([self.axes], dtype=object)
        self.axes = self.axes.reshape(self.nrows, self.ncols)
        
        for rax in self.axes:
            for ax in rax:
                for side in ['right', 'top', 'bottom']: 
                    ax.spines[side].set_visible(False)
        self.optional_setting()
    
    # additional setting
    def optional_setting(self):
        for rax in self.axes:
            for ax in rax:
                ax.set_title(self.title)
                ax.set_xlabel(self.xlabel)
                ax.set_ylabel(self.ylabel)
                if (self.xlim != None):
                    xlim = self.xlim
                    xrange = xlim[1] - xlim[0]
                    ax.set_xlim([xlim[0]-0.02*xrange, xlim[1]+0.02*xrange])
                if (self.ylim != None):
                    ylim = self.ylim
                    yrange = ylim[1] - ylim[0]
                    ax.set_ylim([ylim[0]-0.02*yrange, ylim[1]+0.02*yrange])
                if (self.xticks != None): ax.set_xticks(self.xticks)
                if (self.yticks != None): ax.set_yticks(self.yticks)
                if (self.xticklabels != None): ax.set_xticklabels(self.xticklabels)
                if (self.yticklabels != None): ax.set_yticklabels(self.yticklabels)
                if (self.design): self.optional_design()
    
    # figure design
    def optional_design(self):
        for rax in self.axes:
            for ax in rax:
                if (self.yoffset != None):
                    ax.axhline(self.yoffset, linewidth=0.6, linestyle='-', color='gray', zorder=-1)
                if (self.yticks != None):
                    for ytick in self.yticks:
                        ax.axhline(ytick, linewidth=0.3, linestyle='-.', color='gray', alpha=0.3, zorder=-1)
                if (self.xticks != None) and (self.grid):
                    for xtick in self.xticks:
                        ax.axvline(xtick, linewidth=0.3, linestyle='-.', color='gray', alpha=0.3, zorder=-1)
    

# Color Map (MANIM)
DEC_RED_C = np.array([252, 98, 85])
COLOR_RED_C = '#FC6255' # (252, 98, 85)
DEC_BLUE_C = np.array([88, 196, 221])
COLOR_BLUE_C = '#58C4DD' # (88, 196, 221)
DEC_GREEN_C = np.array([131, 193, 103])
COLOR_GREEN_C = '#83C167' # (131, 193, 103)
COLOR_YELLOW_C = '#F7D96F'
COLOR_PURPLE_C = '#9A72AC'
COLOR_GOLD_C = '#F0AC5F'
COLOR_JERRY = LinearSegmentedColormap.from_list('jerry', ['#A46E24','#CA8628','#E9A547'])
COLOR_TOM = LinearSegmentedColormap.from_list('tom', ['#6D6C6D','#998999','#CAC4C4'])

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

#%%
def util_load_json(self):
    dir_info = '../MCmod/run/cacutil/components/info_obstacle.json'
    with open(dir_info, 'r') as f: dat_map = json.load(f)
    
    vec_start = Vec2.vec2int(Vec2(*dat_map['border'][0]))
    for obs in dat_map['obstacle_point']:
        vec_obs = Vec2(*obs)
        vec_obs = Vec2.vec2int(vec_obs-vec_start)
        self.W[vec_obs.x, vec_obs.y] = 2
    for wal in dat_map['wall_point']:
        vec_wal = Vec2(*wal)
        vec_wal = Vec2.vec2int(vec_wal-vec_start)
        self.W[vec_wal.x, vec_wal.y] = 1

#%% hyp
dir_comp = '../MCmod/run/cacutil/components'
dir_beh = '../../CaC_Data'

# Point load
with open(f'{dir_comp}/pool_point.json', 'r') as f:
    pool_point = json.load(f)
border_start = np.delete(np.array(pool_point['border']['start'])-0.5, 1)
border_end = np.delete(np.array(pool_point['border']['end'])-0.5, 1)

with open(f'{dir_comp}/info_obstacle.json', 'r') as f:
    info_obs = json.load(f)
point_wall = np.array(info_obs['wall_point'])
point_obs = np.array(info_obs['obstacle_point'])

map_size = border_end-border_start+1
pos_obs = np.int32(np.concatenate([point_wall-border_start, point_obs-border_start]))
map_obs = np.zeros(np.int32(map_size))
map_obs[pos_obs[:,0], pos_obs[:,1]] = 1


#%% Distance
def load_position(dat_pos, trial):
    pos_pred = dat_pos[f'trial_{trial}']['gameplay']['predator']
    pos_prey = dat_pos[f'trial_{trial}']['gameplay']['prey']
    xz_pred = np.full((2, 600), np.nan)
    xz_pred[:,:len(pos_pred['x'])] = [pos_pred['x'], pos_pred['z']]
    xz_prey = np.full((2, 600), np.nan)
    xz_prey[:,:len(pos_prey['x'])] = [pos_prey['x'], pos_prey['z']]
    return xz_pred, xz_prey

# 8-way distance
def distance_8way(vec_s, vec_e, path):
    if (len(path)==1) or (len(path)==2 and abs(path[1]-path[0])==1): # case a)
        distance = distance_8way_agents(vec_s, vec_e)
    elif (len(path)==2 and abs(path[1]-path[0])!=1): # case b)
        distance = distance_8way_projection(vec_s, vec_e, path)
    else: # case c)
        distance = distance_8way_path(vec_s, vec_e, path)
    return distance

# case a) ignore path distance, and calculate 8-way distance between agents 
# a1) agents are in same block
# a2) 1 block distance, but line path (no obstacle between agents)
def distance_8way_agents(vec_s, vec_e):
    x, y = vec_e - vec_s
    x, y = abs(x), abs(y)
    # line comp
    d_line = max(x,y) - min(x,y)
    # diagonal comp
    d_dia = min(x,y)*math.sqrt(2)
    return d_line + d_dia

# case b) Draw the line which projects agent to diagonal path line
# b1) 1 block distance, but diagonal path (sometimes obstacle can be exist between agents)
def distance_8way_projection(vec_s, vec_e, path):
    vec_p = (path[1]+path[0])/2
    sx, sy = vec_p - vec_s
    ex, ey = vec_p - vec_e
    sx, sy = abs(sx), abs(sy)
    ex, ey = abs(ex), abs(ey)
    # vec_s projection
    d_s = (max(sx,sy)-min(sx,sy)) + (min(sx,sy)*math.sqrt(2))
    # vec_e projection
    d_e = (max(ex,ey)-min(ex,ey)) + (min(ex,ey)*math.sqrt(2))
    return d_s + d_e

# case c) agent to nextblockpoint 8-way distance + path distance without agent block
# c1) all other cases
def distance_8way_path(vec_s, vec_e, path):
    sx, sy = path[1] - vec_s
    sx, sy = abs(sx), abs(sy)
    ex, ey = path[-2] - vec_e
    ex, ey = abs(ex), abs(ey)
    # vec_s projection
    d_s = (max(sx,sy)-min(sx,sy)) + (min(sx,sy)*math.sqrt(2))
    # vec_e projection
    d_e = (max(ex,ey)-min(ex,ey)) + (min(ex,ey)*math.sqrt(2))
    # path comp
    d_p = 0
    if (len(path)>3):
        for i in range(1, len(path)-2):
            d_p += abs(path[i+1]-path[i])
    return d_s + d_e + d_p


#%%
subj = 'b04'
TASK_NAME = 'chased'
with open(f'{dir_beh}/{subj}/{TASK_NAME}/log_position.json', 'r') as f:
    dat_pos = json.load(f)['cac']
with open(f'{dir_beh}/{subj}/{TASK_NAME}/log_gameplay.json', 'r') as f:
    dat_play = json.load(f)['cac']
p_predicted = np.round(np.array(dat_play['predicted']['winrate']), 2)

# smoothing kernel
window_size = 10
kernel = np.ones(window_size)/window_size

# calculation 8 way distance
pathfinder = Pathfinder()
pathfinder.set_w(map_obs)
dist_path = []
for n in range(20):
    xz_pred, xz_prey = load_position(dat_pos, n)
    xz_pred = xz_pred-0.5-np.expand_dims(border_start, axis=-1)
    xz_prey = xz_prey-0.5-np.expand_dims(border_start, axis=-1)
    dist_path_temp = []
    for t in range(600):
        vec_pred = Vec2(xz_pred[0][t], xz_pred[1][t])
        vec_prey = Vec2(xz_prey[0][t], xz_prey[1][t])
        if (np.isnan(vec_pred.x)):
            break
        pathfinder.findpath(vec_pred, vec_prey)
        opt_path = pathfinder.que_move.copy()
        opt_path.insert(0, vec_pred.vec2int())
        dist_path_temp.append(distance_8way(vec_pred, vec_prey, opt_path))
    # smoothing (500 milli)
    dist_path_temp = np.array(dist_path_temp)
    dist_path_temp = np.convolve(dist_path_temp, kernel, mode='same')
    dist_path.append(dist_path_temp.copy())


#%% Plot
fig_t_d = meowfig(nrows=4, ncols=5, figsize=(8, 8), dpi=300,
                  xlabel=r'$t$'+' (Time)', 
                  ylabel=r'$L$'+' (Distance)',
                  xlim=[0, 600], ylim=[0, 32],
                  xticks=[0, 100, 200, 300, 400, 500, 600], 
                  xticklabels=[0, 5, 10, 15, 20, 25, 30],
                  yticks=[0, 2, 4, 8, 16, 32])

# gauge constant
x_wedge = 0.75
y_wedge = 0.75
r_wedge = 0.2
w_wedge = 0.1

# all trials
for n, dist in enumerate(dist_path):
    ax = fig_t_d.axes[n//5, n%5]
    ax.fill_between([-8, 600+8], [0, 0], [1, 1], color=COLOR_RED_C, alpha=0.2, edgecolor='none')
    ax.plot(dist, c=COLOR_BLUE_C)
    
    # cal value
    p_n = p_predicted[n]
    DEC_P = p_n*DEC_GREEN_C + (1-p_n)*DEC_RED_C
    COLOR_P = '#' + hex(int(DEC_P[0]))[-2:] + hex(int(DEC_P[1]))[-2:] + hex(int(DEC_P[2]))[-2:]
    
    # gauges
    bg_wedge = patches.Wedge((x_wedge, y_wedge), r_wedge, 0, 180, 
                             width=w_wedge, facecolor='#e0e0e0', 
                             edgecolor='none', transform=ax.transAxes)
    val_wedge = patches.Wedge((x_wedge, y_wedge), r_wedge, 180*(1-p_n), 180, 
                              width=w_wedge, facecolor=COLOR_P, 
                              edgecolor='none', transform=ax.transAxes)
    ax.add_patch(bg_wedge)
    ax.add_patch(val_wedge)

    # gauge needle
    ang_arr = np.deg2rad(180*(1-p_n))
    r_arr = r_wedge*0.9
    x_arr = x_wedge + r_arr * np.cos(ang_arr)
    y_arr = y_wedge + r_arr * np.sin(ang_arr)
    ax.annotate('', 
                xy=(x_arr, y_arr), 
                xytext=(x_wedge, y_wedge),
                xycoords='axes fraction',
                textcoords='axes fraction',
                arrowprops=dict(facecolor='black', width=3, headwidth=8, shrink=0))
    ax.text(x_wedge, y_wedge-0.05, f'{p_n}', 
            horizontalalignment='center', fontsize=16, weight='bold', transform=ax.transAxes)
    
    ax.set_title(f'trial {n+1}')
fig_t_d.fig.savefig(f'{dir_beh}/IMG/{subj}_{TASK_NAME}_dist.png', bbox_inches='tight', dpi=300)

plt.show()