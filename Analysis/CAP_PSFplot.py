#%% Library
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from mpl_toolkits.mplot3d import Axes3D

import json
import itertools
import math

#%% plotting
# plot util
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


# Difficulty - Win rate
def plot_rho_p(rho, wl, rho_fit, PSI_fit):
    # data sort
    data = np.vstack((rho, wl))
    sorted_idx = np.argsort(data)
    sorted_data = data[:, sorted_idx[0]]
    
    # duple remove
    c_rho = np.unique(rho)
    c_p = np.array([np.mean(sorted_data[1], where=(sorted_data[0]==r)) for r in c_rho])
    
    # plot
    fig, ax = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                           ylabel=r'$\Psi$'+' (Win Rate)',
                           xlim=[0.7, 1.3], ylim=[0, 1],
                           yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1])
    ax.scatter(data[0], data[1], s=1, color='gray', alpha=0.2, zorder=0)
    ax.scatter(c_rho, c_p, s=1, color=COLOR_BLUE_C, zorder=1)
    ax.plot(rho_fit, PSI_fit, linewidth=1, color=COLOR_GREEN_C, zorder=2)
    return fig, ax


# Difficulty - time
def plot_rho_t(rho, t_data, rho_fit, mu_fit):
    # data normalize (T=30)
    t_data = t_data/T
    
    # data sort
    data = np.vstack((rho, t_data))
    sorted_idx = np.argsort(data)
    sorted_data = data[:, sorted_idx[0]]
    
    # duple remove
    c_rho = np.unique(rho)
    c_t = np.array([np.mean(sorted_data[1], where=(sorted_data[0]==r)) for r in c_rho])
    
    # figure setting
    fig, ax = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                           ylabel=r'$t$'+' (Trial Time)',
                           xlim=[0.7, 1.3], ylim=[0.0, 1.67],
                           yticks=[0, 0.5, 1, 1.5], yticklabels=[0, 15, 30, 45])
    
    # plot
    ax.axhline(1, linewidth=0.3, linestyle='-.', color=COLOR_YELLOW_C, zorder=-1)
    ax.scatter(rho, t_data, s=1, color='k', alpha=0.2, zorder=0)
    ax.scatter(c_rho, c_t, s=1, color=COLOR_BLUE_C, zorder=1)
    ax.plot(rho_fit, mu_fit/T, linewidth=1, color=COLOR_GREEN_C, zorder=2)
    return fig, ax


def plot_psi_heatmap(dat_t, dat_rho, fit_theta):
    dt = 0.1
    ts = np.round(np.arange(dt, 50+dt, dt), 2)
    rh = np.round(np.arange(0.7, 1.31, 0.01), 2)
    if (TASK == 0): 
        rhh = 1/rh
    if (TASK == 1):
        rhh = rh
    z = cal_Polyexp_psi_opt(ts, rhh, fit_theta)
    c_t = np.array([np.where(ts==t)[0][0] for t in dat_t])
    c_rho = np.array([np.where(rh==r)[0][0] for r in dat_rho])
    
    # figure setting
    fig, ax = plt.subplots(figsize=(4,3), dpi=300)
    
    ax.set_xticks([0, 10, 20, 30, 40, 50, 60])
    ax.set_yticks([99, 199, 299, 399, 499])
    ax.set_xticklabels([0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3])
    ax.set_yticklabels([10, 20, 30, 40, 50])
    ax.set_xlabel(r'$\rho$'+' (Difficulty)')
    ax.set_ylabel(r'$t$'+' (Trial Time)')
    
    # plot heatmap
    ax.imshow(z, origin='lower', aspect='auto', cmap='YlGn')
    ax.axhline(299, linewidth=0.3, linestyle='-.', color=COLOR_RED_C, zorder=1)
    ax.scatter(c_rho, c_t, s=1, color='k', zorder=2)
    
    
# trial - NIG
def plot_trial_nig(nig):
    # data
    c_n = np.arange(nig.shape[0])
    c_nig = nig
    
    # figure setting
    fig, ax = plot_setting(xlabel=r'$N$'+' (Trial)', 
                           ylabel=r'$NIG$'+' (Normalized information gain)')
    
    # plot
    ax.plot(c_n, c_nig, color=COLOR_BLUE_C, zorder=1)
    return fig, ax


# trial - rho best
def plot_trial_rho(sampled_rho):
    # data
    sampled_rho = np.array(sampled_rho)
    c_n = np.arange(sampled_rho.shape[0])
    c_rho = sampled_rho
    
    # figure setting
    fig, ax = plot_setting(xlabel=r'$N$'+' (Trial)', 
                           ylabel=r'$\rho$'+' (Difficulty maximize IG)',
                           xlim=[0, c_n.shape[0]], ylim=[0.7, 1.3],
                           yticks=np.arange(0.7, 1.31, 0.1))
    
    # plot
    ax.plot(c_n, c_rho, color=COLOR_BLUE_C, zorder=1)
    return fig, ax
    

# trial - entropy
def plot_trial_H(Hs):
    # data
    c_n = np.arange(Hs.shape[0])
    c_h = Hs
    h_max = np.log(180000) # temp
    
    # figure setting
    fig, ax = plot_setting(xlabel=r'$N$'+' (Trial)', ylabel=r'$H$'+' (Entropy)',
                           xlim=[0, c_n.shape[0]], ylim=[0, h_max],
                           yticks=np.arange(2, h_max, 2))
    
    # plot
    ax.plot(c_n, c_h, color=COLOR_BLUE_C, zorder=1)
    return fig, ax


# parameter likelihood
def plot_theta_L(L, theta_shape, theta_prior, theta_name):
    # caution: theta length is 4.
    # theta info
    theta_shape = np.array(theta_shape)
    theta_prior = np.array(theta_prior)
    theta_num = theta_shape.shape[0]
    theta_min = -theta_prior
    theta_max = theta_shape - theta_prior
    
    # Data
    c_theta = []
    c_L = []
    L = L.reshape(theta_shape)
    L_axis = np.arange(theta_num)
    for i in range(theta_num):
        c_theta.append(np.arange(theta_min[i], theta_max[i]))
        sum_axis = tuple(np.delete(L_axis, i))
        c_L.append(np.log(np.sum(np.exp(L), axis=sum_axis)))
    
    # figure setting
    fig, ax = plot_setting(xlabel=r'$\Delta\theta$'+' (Parameter distance)',
                           ylabel=r'$L$'+' (Normalized log likelihood)')
    
    # plot
    for j in range(theta_num):
        line = ax.plot(c_theta[j], c_L[j])
        line[0].set_label(theta_name[j])
    plt.legend()
    return fig, ax


# trial - maxEIG
def plot_trial_maxEIG(maxEIGs):
    # data
    c_n = np.arange(maxEIGs.shape[0])
    c_eig = maxEIGs
    eig_max = max(maxEIGs)
    
    # figure setting
    fig, ax = plot_setting(xlabel=r'$N$'+' (Trial)', ylabel=r'$maxEIG$'+' (max expected info gain)',
                           xlim=[0, c_n.shape[0]], ylim=[0, eig_max],
                           yticks=np.arange(0, eig_max, 0.05))
    
    # plot
    ax.plot(c_n, c_eig, color=COLOR_BLUE_C, zorder=1)
    ax.axhline(0.05, linewidth=0.3, linestyle='-.', color=COLOR_RED_C, zorder=2)
    return fig, ax


#%% Functions
# rho - P
def cal_Polyexp_PSI(rho_hat, theta):
    # theta = [k, m, h, w]
    rho_hat = np.repeat(rho_hat.reshape(-1,1), theta.shape[0], axis=-1)
    k, m, _, _ = theta.T
    
    # 1 - e^(-X) * (X^0/0! + X^1/1! + ... + X^(k-1)/(k-1)!)
    X_T = X_COEF * T # [diff][grid]
    k_max = max(k)
    K_arange = np.tile(np.arange(k_max), reps=[theta.shape[0], 1]).T
    K_mask = K_arange < k
    vec_fact = np.vectorize(math.gamma)
    K_fact = 1/vec_fact(K_arange+1)
    
    S_T = np.zeros((rho_hat.shape[0], theta.shape[0]))
    for i, X in enumerate(X_T):
        S_T[i] = np.sum((X**K_arange)*(K_fact*K_mask), axis=0)

    P_hit = 1 - np.exp(-X_T)*S_T
    P_hit[P_hit<P_MIN] = P_MIN
    P_hit[P_hit>P_MAX] = P_MAX
    
    if (TASK == 0):
        PSI = P_hit
    elif (TASK == 1):
        PSI = 1-P_hit
    return PSI


def cal_Polyexp_PSI_opt(rho_hat, theta_star):
    k, m, h, w = theta_star # theta = [k, m, h, w]
    # 1 - e^(-X) * (X^0/0! + X^1/1! + ... + X^(k-1)/(k-1)!)
    k = round(k)
    X = np.where(rho_hat>=(h-w)+w/(M-m), k/(m+(1.0/(1+((rho_hat-h)/w)))), k/M)
    series = 0
    for i in range(k):
        series += (X**i)/math.gamma(i+1)
    P_hit = 1 - np.exp(-X)*series
    P_hit[P_hit<P_MIN] = P_MIN
    P_hit[P_hit>P_MAX] = P_MAX
    if (TASK == 0):
        PSI = P_hit
    elif (TASK == 1):
        PSI = 1-P_hit
    return PSI

def cal_Polyexp_psi_opt(t, rho_hat, theta_star):
    k, m, h, w = theta_star
    # x^k * e^-x / t * (k-1)!
    rho_hat, t = np.meshgrid(rho_hat, t)
    x = np.where(rho_hat>=(h-w)+w/(M-m), (k*t)/(T*(m+(1.0/(1+((rho_hat-h)/w))))), k*t/(M*T))
    psi = ((x**k)*np.exp(-x))/(t*math.gamma(k))
    return psi


def cal_Mu(rho_hat, theta):
    # theta = [k, m, h, w]
    rho_hat = np.repeat(rho_hat.reshape(-1,1), theta.shape[0], axis=-1)
    _, m, h, w = theta.T
    mu = np.where(rho_hat >= (h-w)+w/(M-m), T*(m+(1.0/(1+((rho_hat-h)/w)))), M*T)
    return mu


def cal_X_coef(theta):
    # theta = [k, m, h, w]
    # X = k*t/mu
    # coef : k/mu
    k, m, _, _ = theta.T
    x_coef = k/MU
    return x_coef


#%% Color Map (MANIM)
COLOR_RED_C = '#FC6255'
COLOR_BLUE_C = '#58C4DD'
COLOR_GREEN_C = '#83C167'
COLOR_YELLOW_C = '#F7D96F'
COLOR_PURPLE_C = '#9A72AC'
COLOR_GOLD_C = '#F0AC5F'

# hyp
T = 30
TPS = 20
P_MIN = 1.0E-12
P_MAX = 1 - 1.0E-12
RHO_SIZE = 60
RHO = np.round(np.linspace(0.7, 1.29, RHO_SIZE), 2)
M = 100.0

#%% final variables
TASK = 0
SUBJECT = 'PBJ'
RHO_POLICY = 'con'


if (TASK == 0): 
    RHO_HAT = 1.0/RHO
    TASK_NAME = 'chasing'
elif (TASK == 1): 
    RHO_HAT = RHO
    TASK_NAME = 'chased'
dir_comp = '../MCmod/run/cacutil/components'
dir_beh = '../MCmod/run/cacutil/behaviors'

with open(f'{dir_beh}/{SUBJECT}/fitting_{TASK_NAME}/log_fitting.json', 'r') as f_fit:
    dat_fit = json.load(f_fit)['cac']
f_fit.close()

with open(f'{dir_beh}/{SUBJECT}/fitting_{TASK_NAME}/log_gameplay.json', 'r') as f_play:
    dat_play = json.load(f_play)['cac']
f_play.close()

# Param load
with open(f'{dir_comp}/pool_psychometric.json', 'r') as f_psy:
    pool_psy = json.load(f_psy)
psy_bin = pool_psy['binary']['parameter']
psy_con = pool_psy['continuous']['parameter']
f_psy.close()

# Constant
GRID_BIN = np.prod(psy_bin['shape']) # flatten
GRID_CON = np.prod(psy_con['shape']) # flatten
H_MAX_bin = np.log(GRID_BIN)
H_MAX_con = np.log(GRID_CON)

# init Param
theta_bin = []
theta_con = []
for i in range(4):
    theta_bin.append(np.round(np.linspace(psy_bin['min'][i], psy_bin['max'][i]-psy_bin['step'][i], psy_bin['shape'][i]), 3))
    theta_con.append(np.round(np.linspace(psy_con['min'][i], psy_con['max'][i]-psy_con['step'][i], psy_con['shape'][i]), 3))
theta_bin = np.array(list(itertools.product(*theta_bin)))
theta_con = np.array(list(itertools.product(*theta_con)))

# init Prior
theta_bin_prior = np.array(psy_bin['prior'])
theta_con_prior = np.array(psy_con['prior'])
theta_bin_idx = np.array(list(itertools.product(*[np.arange(psy_bin['shape'][i]) for i in range(4)])))
theta_con_idx = np.array(list(itertools.product(*[np.arange(psy_con['shape'][i]) for i in range(4)])))


# Current
P_bin = np.zeros((GRID_BIN, RHO_SIZE)) # [diff][grid]
L_bin = np.zeros(GRID_BIN) # [grid]
H_bin = H_MAX_bin

P_con = np.zeros((GRID_CON, RHO_SIZE)) # [diff][grid]
L_con = np.zeros(GRID_CON) # [grid]
H_con = H_MAX_con


'''
# task
## trial_{num}
### {method}
#### rho_best / entropy / EIGs / param_best / likelihood
'''
# traces
trace_rho = []
trace_H = []
trace_maxEIG = []
trace_theta = []
trace_L = []
trace_t = dat_play['time'] # tick
trace_wl = dat_play['winlose']

total_trial = len(trace_t)
for i in range(total_trial):
    if not f'trial_{i}' in dat_fit: break
    dat = dat_fit[f'trial_{i}']['continuous']
    trace_rho.append(dat['rho_best'])
    trace_H.append(dat['entropy'])
    trace_maxEIG.append(max(dat['EIGs']))
    trace_theta.append(dat['param_best'])

trace_rho = np.array(trace_rho)
trace_H = np.array(trace_H)
trace_maxEIG = np.array(trace_maxEIG)
trace_theta = np.array(trace_theta)
trace_L = np.array(trace_L)
trace_t = np.round(np.array(trace_t)/TPS, 1) # sec
trace_wl = np.array(trace_wl)


# init P, X coef
MU = cal_Mu(RHO_HAT, theta_con)
X_COEF = cal_X_coef(theta_con)



P_con = cal_Polyexp_PSI(RHO_HAT, theta_con)

#
theta_star_idx = np.where(np.all(theta_con_idx == trace_theta[-1], axis=1))[0][0]
theta_star = theta_con[theta_star_idx]
PSI_fit_con = P_con[:, theta_star_idx]

# Difficulty - Win rate
trace_rw = np.vstack((trace_rho, trace_wl)) # data sort
sorted_trace_rw = trace_rw[:, np.argsort(trace_rw)[0]]
c_rho = np.unique(trace_rho) # duple remove
c_p = np.array([np.mean(sorted_trace_rw[1], where=(sorted_trace_rw[0]==r)) for r in c_rho])
fig, ax = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                       ylabel=r'$\Psi$'+' (Win Rate)',
                       xlim=[0.7, 1.3], ylim=[0, 1],
                       yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1]) # plot
ax.scatter(trace_rw[0], trace_rw[1], s=1, color='gray', alpha=0.2, zorder=0)
ax.scatter(c_rho, c_p, s=1, color=COLOR_BLUE_C, zorder=1)
#ax.plot(RHO, PSI_fit_bin, linewidth=1, color=COLOR_RED_C, zorder=2, label='Binary')
ax.plot(RHO, PSI_fit_con, linewidth=1, color=COLOR_GOLD_C, zorder=2, label='Continuous')

#PSI_true = cal_Polyexp_PSI_opt(RHO_HAT, THETA_TRUE)
#ax.plot(RHO, PSI_true, linewidth=1, color=COLOR_GREEN_C, zorder=3, label='True')

plot_psi_heatmap(trace_t, trace_rho, theta_star)

# for i in range(total_trial):
#     plot_psi_heatmap(trace_t[:(i+1)], trace_rho[:(i+1)], theta_con[np.where(np.all(theta_con_idx == trace_theta[i], axis=1))[0][0]])
#     plt.show()

plot_trial_maxEIG(trace_maxEIG)

ax.legend()
plt.show()