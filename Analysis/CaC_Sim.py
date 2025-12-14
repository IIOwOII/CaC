#%%
import numpy as np
import json
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit


#%%
def func_logistic(X, a, b, c):
    return c/(1+np.exp(-b*(X-a)))


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
def plot_rho_t(rho, t, spawn, spawn_idx=-1):
    c_x = np.arange(0.8, 1.2, 0.01)
    if (spawn_idx != -1):
        rho = rho[spawn==spawn_idx]
        t = t[spawn==spawn_idx]
    
    c_mean = []
    for x in c_x:
        c_mean.append(np.mean(t[rho==np.round(x,2)]))
    c_mean = np.array(c_mean)
    
    fig, ax = plt.subplots(figsize=(6,4), dpi=300)
    ax.set_ylim([0,32])
    ax.set_xlabel('rho')
    ax.set_ylabel('t')
    
    ax.axhline(5, linewidth=1, linestyle='--', color='r')
    ax.axhline(30, linewidth=1, linestyle='--', color='r')
    
    ax.scatter(rho, t, s=4)
    ax.plot(c_x, c_mean)
    
    popt, pcov = curve_fit(func_logistic, rho, t, p0=[1,0,25])
    ax.plot(c_x, func_logistic(c_x, *popt))
    

#%% plot

plot_rho_t(c1_diff, c1_time, c1_spawn)
plot_rho_t(c2_diff, c2_time, c2_spawn)

plot_rho_t(c1_diff, c1_time, c1_spawn, 3)
plot_rho_t(c2_diff, c2_time, c2_spawn, 3)

plt.show()