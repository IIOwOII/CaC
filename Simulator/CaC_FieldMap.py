import numpy as np
import pickle as pkl

map_path = '/home/owo/CLAB/CaC/Simulator/data/map/Map.p'
with open(map_path, mode='rb') as f:
    map_mask = pkl.load(f)
map_mask[map_mask==2] = 0

walls = np.where(map_mask==1) # wall

map_field = np.zeros((33,33))
map_field[map_mask!=0] = -1

walls[0] #x
walls[1] #y

for i in range(33):
    for j in range(33):
        if (map_field[i,j] != 0):
            map_field[i,j] = 1/((walls[0]-i)**2 + (walls[1]-j)**2)