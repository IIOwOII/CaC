#%%
import os
import json

import numpy as np
import matplotlib.pyplot as plt

#%%
subj = 'jmj03'
task = 'test_chased'

dir_main = f'{os.getcwd()}/../MCmod/run/cacutil/behaviors'
dir_beh = f'{dir_main}/{subj}/{task}'
with open(f'{dir_beh}/log_event.json') as f:
    log_event = json.load(f)['cac']
with open(f'{dir_beh}/log_gameplay.json') as f:
    log_gameplay = json.load(f)['cac']
with open(f'{dir_beh}/log_position.json') as f:
    log_position = json.load(f)['cac']


#%%
T = 600
trials = len(log_gameplay['time'])

is_chasing = np.array(log_gameplay['type'])==0
is_chased = np.array(log_gameplay['type'])==1


#%%
def get_r(trial):
    pos_pre = log_position[f'trial_{trial}']['preparation']
    pos = log_position[f'trial_{trial}']['gameplay']
    
    t_pre = np.array(pos_pre['time']) - pos_pre['time'][-1] -1
    t = np.array(pos['time']) - pos['time'][0]
    
    r_x_pre = np.array(pos_pre['player']['x']) - pos['opponent']['x'][0]
    r_z_pre = np.array(pos_pre['player']['z']) - pos['opponent']['z'][0]
    r_pre = ((r_x_pre**2)+(r_z_pre**2))**0.5
    
    r_x = np.array(pos['player']['x']) - np.array(pos['opponent']['x'])
    r_z = np.array(pos['player']['z']) - np.array(pos['opponent']['z'])
    r = ((r_x**2)+(r_z**2))**0.5
    
    t = np.concatenate([t_pre, t])
    r = np.concatenate([r_pre, r])
    return r, t
    

#%% 2D r(t)
# fig = plt.figure(figsize=(8,6), dpi=300)
# ax1 = fig.add_subplot(2,1,1)
# ax2 = fig.add_subplot(2,1,2)

# ax1.axhline(1, color='purple', linestyle='-.', linewidth=1)
# ax2.axhline(1, color='purple', linestyle='-.', linewidth=1)

# for i in range(trials):
#     r, t = get_r(i)
#     c = hex(int(32 + i*(223/trials)))[-2:]
#     if (log_gameplay['type'][i] == 0):
#         ax1.plot(t/20, r, color=f'#{c}0000', linewidth=0.7, alpha=0.8)
#     else:
#         ax2.plot(t/20, r, color=f'#0000{c}', linewidth=0.7, alpha=0.8)

# ax1.set_title('Distance between player and opponent (within trial)')
# ax2.set_xlabel('time(sec)')

# ax1.set_xlim((-5,T/20))
# ax1.set_ylim((0,18))
# ax1.set_ylabel('distance(block)')
# ax1.set_yticks(np.arange(0,17,4))

# ax2.set_xlim((-5,T/20))
# ax2.set_ylim((0,18))
# ax2.set_ylabel('distance(block)')
# ax2.set_yticks(np.arange(0,17,4))



#%%
# fig1 = plt.figure(figsize=(6,4), dpi=300)


# ax1 = fig1.add_subplot(2,1,1)
# ax1.scatter(gameplay_diff_abs[is_chasing], gameplay_winlose[is_chasing],
#             color='blue')
# ax1.set_title('chasing')
# ax1.set_ylim((-0.1, 1.1))


# ax2 = fig1.add_subplot(2,1,2)
# ax2.scatter(gameplay_diff_abs[is_chased], gameplay_winlose[is_chased],
#             color='red')
# ax2.set_title('chased')
# ax2.set_ylim((-0.1, 1.1))


# ax2.set_xlabel('Speed ratio (opponent/player)')

# 


#%% 3D r(t)

fig = plt.figure(figsize=(8,4), dpi=300)
ax1 = fig.add_subplot(1,2,1, projection='3d')
ax2 = fig.add_subplot(1,2,2, projection='3d')

font = {"fontsize":"medium", "color":"#448822", "fontweight":"bold"}

ax1.axhline(1, color='purple', linestyle='-.', linewidth=1)
ax2.axhline(1, color='purple', linestyle='-.', linewidth=1)

for i in range(trials):
    r, t = get_r(i)
    c = hex(int(32 + i*(223/trials)))[-2:]
    if (log_gameplay['type'][i] == 0):
        ax1.plot(t/20, log_gameplay['difficulty_absolute'][i], r, 
                 color=f'#{c}0000', linewidth=0.7, alpha=0.8)
    else:
        ax2.plot(t/20, log_gameplay['difficulty_absolute'][i], r, 
                 color=f'#0000{c}', linewidth=0.7, alpha=0.8)

ax1.set_title('Distance between player and opponent (within trial)')

ax1.set_xlabel('time(sec)', fontdict=font, labelpad=4)
ax2.set_xlabel('time(sec)', fontdict=font, labelpad=4)

ax1.set_xlim((-5,T/20))
ax1.set_ylim((0.8,1.2))
ax1.set_yticks(np.arange(0.8,1.2,0.1))
ax1.set_zlim((0,18))
ax1.set_zticks(np.arange(0,17,4))

ax2.set_xlim((-5,T/20))
ax2.set_ylim((0.8,1.2))
ax2.set_yticks(np.arange(0.8,1.2,0.1))
ax2.set_zlim((0,18))
ax2.set_zlabel('distance(block)', fontdict=font, labelpad=4)
ax2.set_zticks(np.arange(0,17,4))


#%%

plt.tight_layout()
plt.show()