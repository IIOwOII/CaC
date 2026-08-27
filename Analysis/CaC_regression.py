#%% Library
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

import json
import math


#%% plot util
def plot_setting(**kwargs):
    # figure setting
    fig, ax = plt.subplots(figsize=(4,3), dpi=300)
    for side in ['right', 'top', 'bottom']:
        ax.spines[side].set_visible(False)
    
    # additional setting
    if ('title' in kwargs): ax.set_title(kwargs['title'])
    if ('xlabel' in kwargs): ax.set_xlabel(kwargs['xlabel'])
    if ('ylabel' in kwargs): ax.set_ylabel(kwargs['ylabel'])
    if ('xlim' in kwargs):
        xlim = kwargs['xlim']
        xrange = xlim[1] - xlim[0]
        ax.set_xlim([xlim[0]-0.02*xrange, xlim[1]+0.02*xrange])
    if ('ylim' in kwargs):
        ylim = kwargs['ylim']
        yrange = ylim[1] - ylim[0]
        ax.set_ylim([ylim[0]-0.02*yrange, ylim[1]+0.02*yrange])
    if ('xticks' in kwargs): ax.set_xticks(kwargs['xticks'])
    if ('yticks' in kwargs): ax.set_yticks(kwargs['yticks'])
    if ('xticklabels' in kwargs): ax.set_xticklabels(kwargs['xticklabels'])
    if ('yticklabels' in kwargs): ax.set_yticklabels(kwargs['yticklabels'])
    
    # figure design
    ax.axhline(0, linewidth=0.6, linestyle='-', color='gray', zorder=-1)
    if ('yticks' in kwargs):
        for ytick in kwargs['yticks']:
            ax.axhline(ytick, linewidth=0.3, linestyle='-.', color='gray', alpha=0.3, zorder=-1)
    return fig, ax


#%% Color Map (MANIM)
COLOR_BLUE_C = '#58C4DD'
COLOR_GREEN_C = '#83C167'
COLOR_YELLOW_C = '#F7D96F'


#%% final variables
TASK = 0
SIMNUM = 5

# path
dir_comp = '../MCmod/run/cacutil/components'
dir_beh = '../MCmod/run/cacutil/behaviors'
if (TASK == 0): name_task = 'chasing'
elif (TASK == 1): name_task = 'chased'

# hyp
T = 30
M = 10 # max t = 10T
TPS = 20
P_MIN = 1.0E-12
P_MAX = 1 - 1.0E-12
RHO_SIZE = 41
RHO = np.round(np.linspace(0.8, 1.2, RHO_SIZE), 2)
if (TASK == 0): RHO_HAT = 1.0/RHO
elif (TASK == 1): RHO_HAT = RHO


#%% file load
# Param load
with open(f'{dir_comp}/pool_psychometric.json', 'r') as f_psy:
    pool_psy = json.load(f_psy)
psy_bin = pool_psy['binary']['parameter']
psy_con = pool_psy['continuous']['parameter']
con_max = np.array(psy_con['max'])
con_min = np.array(psy_con['min'])
f_psy.close()

# sim load
with open(f'{dir_beh}/simulation_{SIMNUM}/simulation_{name_task}/log_gameplay.json') as f_sim:
    sim = json.load(f_sim)['cac']
sim_type = np.array(sim['type'])
sim_rho = np.array(sim['difficulty'])
sim_wl = np.array(sim['winlose'])
sim_t = np.array(sim['time'])
sim_spawn = np.array(sim['spawnpoint_opponent'])
f_sim.close()

# log_fitting
try:
    with open(f'{dir_beh}/simulation_{SIMNUM}/simulation_{name_task}/log_fitting.json') as f_fit:
        dat_fit = json.load(f_fit)['cac']
    fit_final = dat_fit['final']
    fit_trial = []
    num = 0
    while True:
        if not f'trial_{num}' in dat_fit:
            break
        fit_trial.append(dat_fit[f'trial_{num}'])
        num += 1
    f_fit.close()
except:
    print('No fitting data')


# tick to sec
sim_t = sim_t/TPS
# sim_t[sim_t>T] = T

# filter type
mask_task = (sim_type==TASK)
sim_rho = sim_rho[mask_task]
sim_wl = sim_wl[mask_task]
sim_t = sim_t[mask_task]
sim_spawn = sim_spawn[mask_task]
sim_data_size = sim_rho.shape[0]
sim_data_idx = np.random.permutation(np.arange(sim_data_size))
if (TASK == 0):
    sim_rho_hat = 1/sim_rho
elif (TASK == 1):
    sim_rho_hat = sim_rho


#%% regression fitting
# param normalize to regression (0 to 1)
def normalize(x, _min, _max):
    return (x-_min)/(_max-_min)
def unnormalize(x_hat, _min, _max):
    return (_max-_min)*x_hat + _min


def func_logistic(rho, m, w, gam, lam):
    P = gam + (1-gam-lam)/(1+(9**((rho-m)/w)))
    P[P<P_MIN] = P_MIN
    P[P>P_MAX] = P_MAX
    return P

def func_fraction(rho_hat, m, h, w):
    theta = np.array([0, m, h, w])
    vec_unnorm = np.vectorize(unnormalize)
    _, m, h, w = vec_unnorm(theta, con_min, con_max)[:]
    
    mu_norm = np.where(rho_hat >= (h-w)+w/(M-m), (m+(1.0/(1+((rho_hat-h)/w)))), M)
    return mu_norm

def func_exponent(rho_hat, k, m, h, w):
    theta = np.array([k, m, h, w])
    vec_unnorm = np.vectorize(unnormalize)
    k, m, h, w = vec_unnorm(theta, con_min, con_max)[:]
    
    mu_norm = m+((3*k)/(3*k-1)-m)*np.exp(-(rho_hat-h)/w)
    return mu_norm

# def pdf_polyexp(xy, k, m, h, w):
#     t, rho_hat = xy
#     k = round(k)
#     x = (k*t/T) * (1+(rho_hat-h)/w) / (1+m)
    
#     return pdf

def cdf_polyexp(rho_hat, k, m, h, w):
    theta = np.array([k, m, h, w])
    vec_unnorm = np.vectorize(unnormalize)
    k, m, h, w = vec_unnorm(theta, con_min, con_max)[:]
    
    X = np.where(rho_hat >= (h-w)+w/(M-m), k*(1+(rho_hat-h)/w)/(1+m), k/M)
    vec_fact = np.vectorize(math.gamma)
    series = 0
    for i in range(round(k)):
        series += (X**i)/vec_fact(i+1)
    P_hit = 1 - np.exp(-X)*series
    P_hit[P_hit<P_MIN] = P_MIN
    P_hit[P_hit>P_MAX] = P_MAX
    if (TASK == 0):
        cdf = P_hit
    elif (TASK == 1):
        cdf = 1 - P_hit
    return cdf
    
def plot_raw_mu(ax, rho, wl):
    mu = np.zeros(RHO_SIZE)
    for i, r in enumerate(RHO):
        mu[i] = np.mean(wl[rho==r])
    ax.plot(RHO, mu)
    

#%%
# fitting
popt_log, pcov_log = curve_fit(func_logistic, 
                       sim_rho, sim_wl, 
                       p0=[1.0, 0.1, 0.05, 0.05], 
                       bounds=(psy_bin['min'], psy_bin['max']), 
                       maxfev=200000)
popt_frac, pcov_frac = curve_fit(func_fraction,
                                 sim_rho_hat, sim_t/T,
                                 p0=[0.5, 0.5, 0.5],
                                 bounds=([0,0,0], [1,1,1]),
                                 maxfev=200000)
popt_exp, pcov_exp = curve_fit(func_exponent,
                               sim_rho_hat, sim_t/T,
                               p0=[0.5, 0.5, 0.5, 0.5],
                               bounds=([0,0,0,0], [1,1,1,1]),
                               maxfev=200000)
popt_pexp, pcov_pexp = curve_fit(cdf_polyexp,
                                 sim_rho_hat, sim_wl,
                                 p0=[0.5, 0.5, 0.5, 0.5],
                                 bounds=([0,0,0,0], [1,1,1,1]),
                                 maxfev=200000)

# plotting
# fig1, ax1 = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
#                        ylabel=r'$\Psi$'+' (Win Rate)',
#                        xlim=[0.8, 1.2], ylim=[0, 1],
#                        yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1])
# ax1.plot(RHO, func_logistic(RHO, *popt_log), zorder=2)
# ax1.scatter(sim_rho, sim_wl, s=1, color='gray', alpha=0.2, zorder=1)


fig2, ax2 = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                       ylabel=r'$t$'+' (Trial Time)',
                       xlim=[0.8, 1.2], ylim=[0.0, 1.67],
                       yticks=[0, 0.5, 1, 1.5], yticklabels=[0, 15, 30, 45])
ax2.axhline(1, linewidth=0.3, linestyle='-.', color=COLOR_YELLOW_C, zorder=-1)
ax2.scatter(sim_rho, sim_t/T, s=1, color='k', alpha=0.2, zorder=1)
ax2.plot(RHO, func_fraction(RHO_HAT, *popt_frac), linewidth=1, color=COLOR_GREEN_C, zorder=2)


fig3, ax3 = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                       ylabel=r'$\Psi$'+' (Win Rate)',
                       xlim=[0.8, 1.2], ylim=[0, 1],
                       yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1])
ax3.plot(RHO, cdf_polyexp(RHO_HAT, *popt_pexp), zorder=2)
ax3.scatter(sim_rho, sim_wl, s=1, color='gray', alpha=0.2, zorder=1)
plot_raw_mu(ax3, sim_rho, sim_wl)


fig2, ax2 = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                       ylabel=r'$t$'+' (Trial Time)',
                       xlim=[0.8, 1.2], ylim=[0.0, 1.67],
                       yticks=[0, 0.5, 1, 1.5], yticklabels=[0, 15, 30, 45])
ax2.axhline(1, linewidth=0.3, linestyle='-.', color=COLOR_YELLOW_C, zorder=-1)
ax2.scatter(sim_rho, sim_t/T, s=1, color='k', alpha=0.2, zorder=1)
ax2.plot(RHO, func_fraction(RHO_HAT, *popt_pexp[1:]), linewidth=1, color=COLOR_GREEN_C, zorder=2)

fig2, ax2 = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                       ylabel=r'$t$'+' (Trial Time)',
                       xlim=[0.8, 1.2], ylim=[0.0, 1.67],
                       yticks=[0, 0.5, 1, 1.5], yticklabels=[0, 15, 30, 45])
ax2.axhline(1, linewidth=0.3, linestyle='-.', color=COLOR_YELLOW_C, zorder=-1)
ax2.scatter(sim_rho, sim_t/T, s=1, color='k', alpha=0.2, zorder=1)
ax2.plot(RHO, func_exponent(RHO_HAT, *popt_pexp[0:]), linewidth=1, color=COLOR_GREEN_C, zorder=2)


fig3, ax3 = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                       ylabel=r'$\Psi$'+' (Win Rate)',
                       xlim=[0.8, 1.2], ylim=[0, 1],
                       yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1])
ax3.plot(RHO, cdf_polyexp(RHO_HAT, popt_pexp[0], *popt_frac), zorder=2)
ax3.scatter(sim_rho, sim_wl, s=1, color='gray', alpha=0.2, zorder=1)
plot_raw_mu(ax3, sim_rho, sim_wl)

fig3, ax3 = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                       ylabel=r'$\Psi$'+' (Win Rate)',
                       xlim=[0.8, 1.2], ylim=[0, 1],
                       yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1])
ax3.plot(RHO, cdf_polyexp(RHO_HAT, *popt_exp), zorder=2)
ax3.scatter(sim_rho, sim_wl, s=1, color='gray', alpha=0.2, zorder=1)
plot_raw_mu(ax3, sim_rho, sim_wl)

plt.show()