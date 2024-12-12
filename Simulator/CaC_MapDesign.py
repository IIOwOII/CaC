import numpy as np
import matplotlib.pyplot as plt
import itertools

def obstacle_shaping(pos_obstacle):
    pos_point = []
    for pos in pos_obstacle:
        pos_target = [[pos[0],pos[1]],[pos[0]+1,pos[1]],[pos[0],pos[1]+1],[pos[0]+1,pos[1]+1]]
        for pos_temp in pos_target:
            if not pos_temp in pos_point:
                pos_point.append(pos_temp)
    pos_point = np.array(pos_point)
    return pos_point


map_x = 10
map_y = 10

pos_wall = []
pos_wall.append((0,0))
pos_wall.append((0,map_y))
pos_wall.append((map_x,0))
pos_wall.append((map_x,map_y))
for wall in itertools.product(np.arange(1,map_x),np.array([0,map_y])):
    pos_wall.append(wall)
for wall in itertools.product(np.array([0,map_x]),np.arange(1,map_y)):
    pos_wall.append(wall)
pos_wall = np.array(pos_wall)

# 장애물 ([0,0]이라 입력하면 [0,0],[0,1],[1,0],[1,1]에 기둥 세워짐)
pos_obstacle = [[4,4],[5,4],[7,5]]
pos_point = obstacle_shaping(pos_obstacle)

K = 1 # 전기장 세기
R_thres = 0.5

sam_f = 10 # sample frequency (10이면 0과 1 사이를 10구간 나눔)
sam_x = map_x*sam_f+1
sam_y = map_y*sam_f+1

X = np.linspace(0, map_x, sam_x)
Y = np.linspace(0, map_y, sam_y)
Z = np.zeros((sam_x, sam_y))
for x,y in itertools.product(X,Y):
    R_point = (pos_point[:,0]-x)**2+(pos_point[:,1]-y)**2
    R_point[np.where(R_point<R_thres)[0]] = R_thres
    Z[int(x*10),int(y*10)] += np.sum(K/R_point)
    
    R_wall = (pos_wall[:,0]-x)**2+(pos_wall[:,1]-y)**2
    R_wall[np.where(R_wall<R_thres)[0]] = R_thres
    Z[int(x*10),int(y*10)] += np.sum(K/R_wall)

# 2D map plot
fig = plt.figure(figsize=(8,8))

Z_rot = np.rot90(Z)

plt.imshow(Z_rot, extent=(0,map_x,0,map_y))
plt.scatter(pos_point[:,0], pos_point[:,1], marker='x', color='r')