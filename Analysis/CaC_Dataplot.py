#%%
import numpy as np
import json
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from scipy.optimize import curve_fit

#%%
def func_logistic(X, a, b, c, d):
    return c+((1-d-c)/(1+np.exp((X-a)/b)))

def func_exponential(X, a):
    return np.exp(X-a)


#%%
dir_main = '../MCmod/run/cacutil/behaviors/simulation'

try:
    with open(f'{dir_main}/simulation_fit_chasing/log_gameplay.json', 'r') as f1:
        chasing = json.load(f1)['cac']
    c1_diff = np.array(chasing['difficulty'])
    c1_time = np.array(chasing['time'])/20
    c1_spawn = np.array(chasing['spawnpoint_opponent'])
    c1_wl = np.array(chasing['winlose'])
except:
    print('no chasing data')
    
try:
    with open(f'{dir_main}/simulation_fit_chased/log_gameplay.json', 'r') as f2:
        chased = json.load(f2)['cac']
    c2_diff = np.array(chased['difficulty'])
    c2_time = np.array(chased['time'])/20
    c2_spawn = np.array(chased['spawnpoint_opponent'])
    c2_wl = np.array(chased['winlose'])
except:
    print('no chased data')



#%%
def plot_rho_t(rho, t, spawn, spawn_idx=0, task='chasing', func='logistic'):
    # spawnpoint sort
    if (spawn_idx > 0):
        rho = rho[spawn==(spawn_idx-1)]
        t = t[spawn==(spawn_idx-1)]
    elif (spawn_idx < 0):
        rho = rho[spawn!=(-spawn_idx-1)]
        t = t[spawn!=(-spawn_idx-1)]
    
    # data normalize
    T = max(t)
    t_norm = t/T
    
    # data sort
    c_x = list(set(rho))
    c_x.sort()
    c_x = np.array(c_x)
    c_y = np.zeros(c_x.size)
    for i, x in enumerate(c_x):
        x = np.around(x, 2)
        c_y[i] = np.mean(t[rho==x])
    
    # figure setting
    fig, ax = plt.subplots(figsize=(4,3), dpi=300)
    for side in ['right', 'top', 'bottom']:
        ax.spines[side].set_visible(False)
    ax.set_xlabel(r'$\rho$' + ' (Difficulty)')
    ax.set_ylabel(r'$t$' + ' (Trial Time)')
    ax.set_xlim([0.78,1.2])
    ax.set_ylim([-0.02*T,1.02*T])
    ax.set_yticks(np.arange(0,T+1,10))
    
    # figure design
    ax.axhline(0, linewidth=0.8, linestyle='-', color='k', zorder=-1)
    ax.axhline(10, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(20, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(30, linewidth=0.8, linestyle='-.', color='orangered', zorder=-1)
    ax.axhline(40, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(50, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    
    # plot
    ax.scatter(rho, t, s=1, color='k', alpha=0.2, zorder=0)
    ax.scatter(c_x, c_y, s=1, color='blue', zorder=1)
    
    if (func=='logistic'):
        if (task=='chasing'):
            beta_sign = -1
        elif (task=='chased'):
            beta_sign = 1
        beta_b0 = beta_sign*0.005
        beta_b1 = beta_sign*0.1
        
        popt, pcov = curve_fit(func_logistic, rho, t_norm, p0=[1,beta_sign*0.04,0,0], 
                               bounds=([0.9,min(beta_b0,beta_b1),0,0],[1.1,max(beta_b0,beta_b1),0.1,0.1]), maxfev=20000)
        ax.plot(c_x, T*func_logistic(c_x, *popt), color='forestgreen', zorder=2)
    
    return popt, pcov


# Difficulty - Win rate
def plot_rho_p(rho, p, spawn, spawn_idx=0):
    # spawnpoint sort
    if (spawn_idx > 0):
        rho = rho[spawn==(spawn_idx-1)]
        p = p[spawn==(spawn_idx-1)]
    elif (spawn_idx < 0):
        rho = rho[spawn!=(-spawn_idx-1)]
        p = p[spawn!=(-spawn_idx-1)]
        
    # data sort
    c_x = list(set(rho))
    c_x.sort()
    c_x = np.array(c_x)
    c_y = np.zeros(c_x.size)
    for i, x in enumerate(c_x):
        x = np.around(x, 2)
        c_y[i] = np.mean(p[rho==x])
    
    # figure setting
    fig, ax = plt.subplots(figsize=(4,3), dpi=300)
    for side in ['right', 'top', 'bottom']:
        ax.spines[side].set_visible(False)
    ax.set_xlabel(r'$\rho$' + ' (Difficulty)')
    ax.set_ylabel(r'$P$' + ' (Win Rate)')
    ax.set_xlim([0.78,1.2])
    ax.set_ylim([-0.02,1.02])
    ax.set_yticks([0,0.5,1])
    
    # figure design
    ax.axhline(0, linewidth=0.6, linestyle='-', color='gray', zorder=-1)
    ax.axhline(0.5, linewidth=0.3, linestyle='-.', color='gray', alpha=0.3, zorder=-1)
    ax.axhline(1, linewidth=0.6, linestyle='-', color='gray', zorder=-1)
    
    # plot
    ax.scatter(rho, p, s=1, color='gray', alpha=0.2, zorder=0)
    ax.scatter(c_x, c_y, s=1, color='blue', zorder=1)
    
    # fitting
    popt, pcov = curve_fit(func_logistic, c_x, c_y, p0=[1,0.04,0,0], 
                           bounds=([0.9,0.005,0,0],[1.1,0.1,0.1,0.1]), maxfev=20000)
    PSI_rho_p = func_logistic(c_x, *popt)
    
    ax.plot(c_x, PSI_rho_p, color='green', zorder=2)
    
    return popt, pcov


def plot_rho_mu(rho, t):
    c_x = np.arange(0.8, 1.2, 0.01)
    c_y = np.zeros(c_x.size)
    for i, x in enumerate(c_x):
        x = np.around(x, 2)
        c_y[i] = np.mean(t[rho==x])
    T = max(t)
    
    fig, ax = plt.subplots(figsize=(4,3), dpi=300)
    
    ax.set_xlim([0.78,1.2])
    ax.set_ylim([-0.02*T,1.02*T])
    for side in ['right', 'top', 'bottom']:
        ax.spines[side].set_visible(False)
    
    ax.axhline(0, linewidth=0.8, linestyle='-', color='k', zorder=-1)
    ax.axhline(10, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(20, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(30, linewidth=0.8, linestyle='-.', color='orangered', zorder=-1)
    ax.axhline(40, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(50, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    
    ax.scatter(rho, t, s=1, color='k', alpha=0.5, zorder=1)
    ax.plot(c_x, c_y, color='forestgreen', zorder=2)
    
    return c_y


def plot_rho_h(rho, t, task='chasing', corrected_rho=False):
    # if corrected_rho is true, rho will be rearranged.
    # (opp./pla.) -> (cat/mouse)
    c_x = list(set(rho))
    c_x.sort()
    c_x = np.array(c_x)
    c_y = np.zeros(c_x.size)
    for i, x in enumerate(c_x):
        x = np.around(x, 2)
        c_y[i] = 1/np.mean(t[rho==x])
    
    if (corrected_rho) and (task=='chasing'):
        c_x = 1/c_x
        rho = 1/rho
    
    fig, ax = plt.subplots(figsize=(4,3), dpi=300)
    
    ax.set_xlim([0.78, 1.21])
    ax.set_ylim([-0.005, 0.2])
    ax.set_yticks(np.linspace(0,0.2,5))
    for side in ['right', 'top', 'bottom']:
        ax.spines[side].set_visible(False)
    
    ax.axhline(0, linewidth=0.8, linestyle='-', color='k', zorder=-1)
    ax.axhline(1/30, linewidth=0.8, linestyle='-.', color='orangered', zorder=-1)
    ax.axhline(0.05, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(0.1, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(0.15, linewidth=0.4, linestyle='-', color='gray', alpha=0.2, zorder=-1)
    ax.axhline(0.2, linewidth=0.8, linestyle='-.', color='orangered', zorder=-1)
    
    ax.scatter(rho, 1/t, s=1, color='k', alpha=0.5, zorder=1)
    ax.plot(c_x, c_y, color='forestgreen', zorder=2)
    

#%% plot

rt1_popt, rt1_pcov = plot_rho_t(c1_diff, c1_time, c1_spawn, task='chasing')
rt2_popt, rt2_pcov = plot_rho_t(c2_diff, c2_time, c2_spawn, task='chased')

rp1_popt, rp1_pcov = plot_rho_p(c1_diff, c1_wl, c1_spawn)
rp2_popt, rp2_pcov = plot_rho_p(c2_diff, c2_wl, c2_spawn)

# mu1 = plot_rho_mu(c1_diff, c1_time)
# mu2 = plot_rho_mu(c2_diff, c2_time)

# plot_rho_h(c1_diff, c1_time, task='chasing', corrected_rho=False)
# plot_rho_h(c2_diff, c2_time, task='chased')

#indifference y=0.5인 위치 세로선과 coeff 주기
plt.show()

#%% goodness of fit

