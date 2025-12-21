#%%
import numpy as np
import json
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit
from sklearn.metrics import r2_score

#%%
def func_logistic(X, a, b, c, d):
    return c+((1-d-c)/(1+np.exp(-b*(X-a))))


#%%
dir_main = '../MCmod/run/cacutil/behaviors/simulation'

with open(f'{dir_main}/simulation_chasing/log_gameplay.json', 'r') as f1:
    chasing = json.load(f1)['cac']
with open(f'{dir_main}/simulation_chased/log_gameplay.json', 'r') as f2:
    chased = json.load(f2)['cac']

c1_diff = np.array(chasing['difficulty_absolute'])
c1_time = np.array(chasing['time'])/20
c1_spawn = np.array(chasing['spawnpoint_opponent'])
c1_wl = np.array(chasing['winlose'])

c2_diff = np.array(chased['difficulty_absolute'])
c2_time = np.array(chased['time'])/20
c2_spawn = np.array(chased['spawnpoint_opponent'])
c2_wl = np.array(chased['winlose'])


#%%
def plot_rho_t(rho, t, spawn, spawn_idx=0):
    c_x = np.arange(0.8, 1.2, 0.01)
    if (spawn_idx > 0):
        rho = rho[spawn==(spawn_idx-1)]
        t = t[spawn==(spawn_idx-1)]
    elif (spawn_idx < 0):
        rho = rho[spawn!=(-spawn_idx-1)]
        t = t[spawn!=(-spawn_idx-1)]
    t_norm = t/50
    
    fig, ax = plt.subplots(figsize=(4,3), dpi=300)
    
    ax.set_xlim([0.78,1.2])
    ax.set_ylim([-2,52])
    for side in ['right', 'top', 'bottom']:
        ax.spines[side].set_visible(False)
    
    ax.axhline(0, linewidth=0.8, linestyle='-', color='k', zorder=-1)
    ax.axhline(10, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(20, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(30, linewidth=0.8, linestyle='-.', color='orangered', zorder=-1)
    ax.axhline(40, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(50, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    
    ax.scatter(rho, t, s=1, color='k', alpha=0.5, zorder=1)
    
    popt, pcov = curve_fit(func_logistic, rho, t_norm, p0=[1,0,0,0], 
                           bounds=([0.8,-100,0,0],[1.2,100,0.2,0.2]), maxfev=20000)
    ax.plot(c_x, 50*func_logistic(c_x, *popt), color='forestgreen', zorder=2)
    
    return popt, pcov


def plot_rho_p(rho, p, spawn, spawn_idx=0):
    c_x = np.arange(0.8, 1.2, 0.01)
    if (spawn_idx > 0):
        rho = rho[spawn==(spawn_idx-1)]
        p = p[spawn==(spawn_idx-1)]
    elif (spawn_idx < 0):
        rho = rho[spawn!=(-spawn_idx-1)]
        p = p[spawn!=(-spawn_idx-1)]
    
    fig, ax = plt.subplots(figsize=(4,3), dpi=300)
    ax.set_ylim([-0.1,1.1])
    ax.set_yticks([0,0.5,1])
    
    ax.axhline(0, linewidth=0.5, linestyle='-', color='gray', zorder=-1)
    ax.axhline(1, linewidth=0.5, linestyle='-', color='gray', zorder=-1)
    
    ax.scatter(rho, p, s=1, color='k', zorder=1)
    
    popt, pcov = curve_fit(func_logistic, rho, p, p0=[1,0,0,0], 
                           bounds=([0.8,-100,0,0],[1.2,100,0.2,0.2]), maxfev=20000)
    c_y = func_logistic(c_x, *popt)
    
    ax.plot(c_x, c_y, color='green', zorder=2)
    
    return popt, pcov

#%% plot

rt1_popt, rt1_pcov = plot_rho_t(c1_diff, c1_time, c1_spawn)
rt2_popt, rt2_pcov = plot_rho_t(c2_diff, c2_time, c2_spawn)

# rp1_popt, rp1_pcov = plot_rho_p(c1_diff, c1_wl, c1_spawn)
# rp2_popt, rp2_pcov = plot_rho_p(c2_diff, c2_wl, c2_spawn)

#indifference y=0.5인 위치 세로선과 coeff 주기
plt.show()


#%% goodness of fit

