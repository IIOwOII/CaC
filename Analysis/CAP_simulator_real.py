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
                           xlim=[0.8, 1.2], ylim=[0, 1],
                           yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1])
    ax.scatter(data[0], data[1], s=1, color='gray', alpha=0.2, zorder=0)
    ax.scatter(c_rho, c_p, s=1, color=COLOR_BLUE_C, zorder=1)
    ax.plot(rho_fit, PSI_fit, linewidth=1, color=COLOR_GREEN_C, zorder=2)
    return fig, ax

def rho_t(rho, t_data):
    # data normalize (T=30)
    t_data = t_data/T
    
    # data sort
    data = np.vstack((rho, t_data))
    sorted_idx = np.argsort(data)
    sorted_data = data[:, sorted_idx[0]]
    
    # duple remove
    c_rho = np.unique(rho)
    mu = np.array([np.mean(sorted_data[1], where=(sorted_data[0]==r)) for r in c_rho])
    var = np.array([np.var(sorted_data[1], where=(sorted_data[0]==r)) for r in c_rho])
    
    # figure setting
    fig, ax = plot_setting(xlabel=r'$\rho$'+' (Difficulty)', 
                           ylabel=r'$t$'+' (Trial Time)',
                           xlim=[0.8, 1.2], ylim=[0.0, 1.67],
                           yticks=[0, 0.5, 1, 1.5], yticklabels=[0, 15, 30, 45])
    
    # plot
    ax.axhline(1, linewidth=0.3, linestyle='-.', color=COLOR_YELLOW_C, zorder=-1)
    ax.scatter(rho, t_data, s=1, color='k', alpha=0.2, zorder=0)
    ax.scatter(c_rho, mu, s=1, color=COLOR_BLUE_C, zorder=1)
    return mu, var

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
                           xlim=[0.8, 1.2], ylim=[0.0, 1.67],
                           yticks=[0, 0.5, 1, 1.5], yticklabels=[0, 15, 30, 45])
    
    # plot
    ax.axhline(1, linewidth=0.3, linestyle='-.', color=COLOR_YELLOW_C, zorder=-1)
    ax.scatter(rho, t_data, s=1, color='k', alpha=0.2, zorder=0)
    ax.scatter(c_rho, c_t, s=1, color=COLOR_BLUE_C, zorder=1)
    ax.plot(rho_fit, mu_fit/T, linewidth=1, color=COLOR_GREEN_C, zorder=2)
    return fig, ax


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
                           xlim=[0, c_n.shape[0]], ylim=[0.8, 1.2],
                           yticks=np.arange(0.8, 1.21, 0.1))
    
    # plot
    ax.plot(c_n, c_rho, color=COLOR_BLUE_C, zorder=1)
    return fig, ax
    

# trial - entropy
def plot_trial_H(Hs):
    # data
    c_n = np.arange(Hs.shape[0])
    c_h = Hs
    h_max = np.log(160000) # temp
    
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


#%% Functions
def reshape_index(sgrid, idx_param, method):
    if (method == 'binary'):
        shape = psy_bin['shape']
    elif (method == 'continuous'):
        shape = psy_con['shape']
        
    GB = shape[1]
    GC = shape[2]
    GD = shape[3]
    
    if (idx_param == 3):
        idx = sgrid % GD
    elif (idx_param == 2):
        idx = (sgrid // GD) % GC
    elif (idx_param == 1):
        idx = ((sgrid // GD) // GC) % GB
    elif (idx_param == 0):
        idx = ((sgrid // GD) // GC) // GB
    return idx


def norm_L(L_hat):
    L = L_hat - np.log(np.sum(np.exp(L_hat)))
    return L


def norm_H(H_hat, gridsize):
    H = H_hat/np.log(gridsize)
    return H


def pseudo_PSI(rho):
    if (TASK == 0):
        theta = np.array([1.00177304e+00, 2.02693684e-02, 9.07413139e-02, 8.13989299e-24])
    elif (TASK == 1):
        theta = np.array([9.93701671e-01, 2.52427771e-02, 3.08789991e-13, 5.10370566e-02])
    m, w, gam, lam = theta
    
    P = gam + (1-gam-lam)/(1+(9**((rho-m)/w)))
    wl = np.random.choice([1,0], size=1, p=[P,1-P])[0]
    return wl

def pseudo_psi(rho):
    if (TASK == 0):
        theta = np.array([10, 0.11420823, 1.00868292, 0.1436978 ])
        rho_hat = 1/rho
    elif (TASK == 1):
        theta = np.array([11,  0.09327324,  1.00918455,  0.19635029])
        rho_hat = rho
    k, m, h, w = theta
    mu = np.where(rho_hat >= (h-w)+w/(10.0-m), T*(m+(1.0/(1+((rho_hat-h)/w)))), T*10.0)
    dt = 0.1
    ts = np.arange(0.1, T, dt)
    
    x = (k*ts)/mu
    dP = dt*(((x**k)*np.exp(-x))/(ts*math.gamma(k)))
    P_over_T = 1-np.sum(dP)
    P_given_inT = dP/np.sum(dP)
    
    is_over = np.random.choice([True, False], size=1, p=[P_over_T, 1-P_over_T])[0]
    if (is_over):
        t=T
        if (TASK==0): wl = 0
        elif (TASK==1): wl = 1
    else:
        t = np.random.choice(ts, size=1, p=P_given_inT)[0]
        if (TASK==0): wl = 1
        elif (TASK==1): wl = 0
    return t, wl
    

def cal_gaussian(x, mu, sigma):
    P = (1/(np.sqrt(2*np.pi)*sigma)) * np.exp(-0.5*((x-mu)/sigma)**2)
    return P


def cal_PSI(rho, theta):
    # theta = [m, w, gamma, lambda]
    rho = np.repeat(rho.reshape(-1,1), theta.shape[0], axis=-1)
    m, w, gam, lam = theta.T
    
    P = gam + (1-gam-lam)/(1+(9**((rho-m)/w)))
    P[P<P_MIN] = P_MIN
    P[P>P_MAX] = P_MAX
    return P


# rho - P
def cal_Polyexp_PSI(rho_hat, theta):
    # theta = [k, m, h, w]
    rho_hat = np.repeat(rho_hat.reshape(-1,1), theta.shape[0], axis=-1)
    k, m, _, _ = theta.T
    
    # 1 - e^(-X) * (X^0/0! + X^1/1! + ... + X^(k-1)/(k-1)!)
    X_T = X_COEF * (T-m*T) # [diff][grid]
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


def cal_Polyexp_L(L, rho_idx, t, theta):
    # X = k*(t-m*T)/(mu-m*T)
    k, m, _, _ = theta.T
    x = np.where(t>m*T, X_COEF[rho_idx]*(t-m*T), P_MIN) # [grid]
    vec_fact = np.vectorize(math.gamma)
    
    L_update = L + np.where(t>m*T, k*np.log(x)-x-np.log(t)-np.log(vec_fact(k)), np.log(P_MIN))
    L_update = norm_L(L_update)
    return L_update
    

def cal_Mu(rho_hat, theta):
    # theta = [k, m, h, w]
    M = 10.0
    rho_hat = np.repeat(rho_hat.reshape(-1,1), theta.shape[0], axis=-1)
    _, m, h, w = theta.T
    mu = np.where(rho_hat >= (h-w)+w/(M-m), T*(m+(1.0/(1+((rho_hat-h)/w)))), M*T)
    return mu


def cal_X_coef(theta):
    # theta = [k, m, h, w]
    # X = k*(t-m*T)/(mu-m*T)
    # coef : k/(mu-m*T)
    k, m, _, _ = theta.T
    x_coef = k/(MU-m*T)
    return x_coef
    

def cal_ExP(P, L):
    ExP = np.sum(P * np.exp(L), axis=1)
    return ExP


def cal_ExL(P, L, P_ex):
    P_ex = P_ex.reshape(-1,1)
    ExL = np.array([L+np.log(P)-np.log(P_ex), L+np.log(1-P)-np.log(1-P_ex)])
    return ExL


def cal_ExH(L_ex):
    ExH = -np.sum(L_ex * np.exp(L_ex), axis=2)
    return ExH


def cal_EIG(H, P_ex, H_ex):
    EIG = H - P_ex * H_ex[0] - (1-P_ex)*H_ex[1]
    return EIG


def cal_H(L):
    H = -np.sum(L*np.exp(L))
    return H


#%% Color Map (MANIM)
COLOR_BLUE_C = '#58C4DD'
COLOR_GREEN_C = '#83C167'
COLOR_YELLOW_C = '#F7D96F'


#%% final variables
TASK = 1
T = 30
TPS = 20
P_MIN = 1.0E-12
P_MAX = 1 - 1.0E-12


#%% file load
# Param load
dir_comp = '../MCmod/run/cacutil/components'
dir_beh = '../MCmod/run/cacutil/behaviors'
if (TASK == 0):
    name_task = 'chasing'
elif (TASK == 1):
    name_task = 'chased'

with open(f'{dir_comp}/pool_psychometric.json', 'r') as f_psy:
    pool_psy = json.load(f_psy)
psy_bin = pool_psy['binary']['parameter']
psy_con = pool_psy['continuous']['parameter']
f_psy.close()

# sim load
with open(f'{dir_beh}/simulation/simulation_{name_task}/log_gameplay.json') as f_sim:
    sim = json.load(f_sim)['cac']
sim_type = np.array(sim['type'])
sim_rho = np.array(sim['difficulty'])
sim_wl = np.array(sim['winlose'])
sim_t = np.array(sim['time'])
sim_spawn = np.array(sim['spawnpoint_opponent'])
f_sim.close()

# tick to sec
sim_t = sim_t/TPS
sim_t[sim_t>T] = T

# filter type
mask_task = (sim_type==TASK)
sim_rho = sim_rho[mask_task]
sim_wl = sim_wl[mask_task]
sim_t = sim_t[mask_task]
sim_spawn = sim_spawn[mask_task]
sim_data_size = sim_rho.shape[0]
sim_data_idx = np.random.permutation(np.arange(sim_data_size))


#%% Initialize
# Constant
RHO_SIZE = 41
RHO = np.round(np.linspace(0.8, 1.20, RHO_SIZE), 2)
if (TASK == 0):
    RHO_HAT = 1.0/RHO
elif (TASK == 1):
    RHO_HAT = RHO

GRID_BIN = np.prod(psy_bin['shape']) # flatten
GRID_CON = np.prod(psy_con['shape']) # flatten
H_MAX_bin = np.log(GRID_BIN)
H_MAX_con = np.log(GRID_CON)


# Current
P_bin = np.zeros((GRID_BIN, RHO_SIZE)) # [diff][grid]
L_bin = np.zeros(GRID_BIN) # [grid]
H_bin = H_MAX_bin

P_con = np.zeros((GRID_CON, RHO_SIZE)) # [diff][grid]
L_con = np.zeros(GRID_CON) # [grid]
H_con = H_MAX_con


# init Param
theta_bin = []
theta_con = []
for i in range(4):
    theta_bin.append(np.round(np.linspace(psy_bin['min'][i], psy_bin['max'][i]-psy_bin['step'][i], psy_bin['shape'][i]), 3))
    theta_con.append(np.round(np.linspace(psy_con['min'][i], psy_con['max'][i]-psy_con['step'][i], psy_con['shape'][i]), 3))

theta_bin = np.array(list(itertools.product(*theta_bin)))
theta_con = np.array(list(itertools.product(*theta_con)))
theta_bin_idx = np.array(list(itertools.product(*[np.arange(psy_bin['shape'][i]) for i in range(4)])))
theta_con_idx = np.array(list(itertools.product(*[np.arange(psy_con['shape'][i]) for i in range(4)])))


# init Prior
theta_bin_prior = np.array(psy_bin['prior'])
theta_con_prior = np.array(psy_con['prior'])

L_bin = np.log(0.5+0.5*np.exp(-np.sum(((theta_bin_idx-theta_bin_prior)/np.array(psy_bin['shape']))**2, axis=-1)/2.0))
L_con = np.log(0.5+0.5*np.exp(-np.sum(((theta_con_idx-theta_con_prior)/np.array(psy_con['shape']))**2, axis=-1)/2.0))
L_bin = norm_L(L_bin)
L_con = norm_L(L_con)


# init P, X coef
MU = cal_Mu(RHO_HAT, theta_con)
X_COEF = cal_X_coef(theta_con)
P_bin = cal_PSI(RHO, theta_bin)
P_con = cal_Polyexp_PSI(RHO_HAT, theta_con)


#%%
# history
Hs_bin = []
Hs_con = []
IG_bin = []
IG_con = []
rho_best_bin = []
rho_best_con = []
thetas_best_bin = []
thetas_best_con = []

# Sampled data
trace_rho = np.array([])
trace_wl = np.array([])
trace_t = np.array([])

# sample number
sample_size = 20
# sample_size = sim_data_size

# simul fitting by sim data
for trial_num in range(sample_size):
    # update trial before
    ExP_bin = cal_ExP(P_bin, L_bin)
    ExL_bin = cal_ExL(P_bin, L_bin, ExP_bin)
    ExH_bin = cal_ExH(ExL_bin)
    EIG_bin = cal_EIG(H_bin, ExP_bin, ExH_bin)
    rho_best_bin.append(RHO[np.argmax(EIG_bin)])
    ExP_con = cal_ExP(P_con, L_con)
    ExL_con = cal_ExL(P_con, L_con, ExP_con)
    ExH_con = cal_ExH(ExL_con)
    EIG_con = cal_EIG(H_con, ExP_con, ExH_con)
    rho_best_con.append(RHO[np.argmax(EIG_con)])
    
    # set rho best
    rho_best = RHO[np.argmax(EIG_con)]
    
    # log save
    Hs_bin.append(H_bin)
    Hs_con.append(H_con)
    thetas_best_bin.append(theta_bin[np.argmax(L_bin)])
    thetas_best_con.append(theta_con[np.argmax(L_con)])
    
    ## sampling data (trial result)
    # using simul data
    sam_idx = sim_data_idx[trial_num]
    sam_rho = sim_rho[sam_idx]
    sam_wl = sim_wl[sam_idx]
    sam_t = sim_t[sam_idx]
    # using pseudo distribution
    # sam_rho = rho_best
    # sam_wl = pseudo_PSI(sam_rho)
    # sam_t = pseudo_psi(sam_rho, sam_wl)
    
    # update trial after
    if (sam_wl == 0): wl_idx = 1
    elif (sam_wl == 1): wl_idx = 0
    rho_idx = np.where(RHO==sam_rho)[0][0]
    
    IG_bin.append(H_bin - ExH_bin[wl_idx][rho_idx])
    L_bin = np.copy(ExL_bin[wl_idx][rho_idx])
    H_bin = ExH_bin[wl_idx][rho_idx]
    
    L_con_past = np.copy(L_con)
    H_con_past = H_con
    if ((TASK == 0 and sam_wl == 0) or (TASK == 1 and sam_wl == 1)):
        L_con = np.copy(ExL_con[wl_idx][rho_idx])
        H_con = ExH_con[wl_idx][rho_idx]
    else:
        L_con = cal_Polyexp_L(L_con_past, rho_idx, sam_t, theta_con)
        H_con = cal_H(L_con)
    IG_con.append(H_con_past - H_con)
    
    # plotting raw data and fit data
    PSI_fit_bin = P_bin[:, np.argmax(L_bin)]
    PSI_fit_con = P_con[:, np.argmax(L_con)]
    MU_fit_con = MU[:, np.argmax(L_con)]
    
    # Sampled data
    trace_rho = np.append(trace_rho, sam_rho)
    trace_wl = np.append(trace_wl, sam_wl)
    trace_t = np.append(trace_t, sam_t)
    
    # plotting
    fig1, ax1 = plot_rho_p(trace_rho, trace_wl, RHO, PSI_fit_bin)
    fig2, ax2 = plot_rho_p(trace_rho, trace_wl, RHO, PSI_fit_con)
    fig3, ax3 = plot_rho_t(trace_rho, trace_t, RHO, MU_fit_con)
    ax1.set_title('Binary')
    ax2.set_title('Continuous')
    ax3.set_title('Continuous')
    plt.show()
    
    # print log
    print(' ')
    print(f'---trial {trial_num}---')
    print(f'difficulty: {sam_rho}')
    print(f'time: {sam_t}')
    print(f'winlose: {sam_wl}')


# Normalized information gain
NIG_bin = np.array(IG_bin)/H_MAX_bin
NIG_con = np.array(IG_con)/H_MAX_con


# Get best parameter
theta_star_bin = theta_bin[np.argmax(L_bin)]
theta_star_con = theta_con[np.argmax(L_con)]


# Additional Plot
fig_Hb, ax_Hb = plot_trial_H(np.array(Hs_bin))
fig_Hc, ax_Hc = plot_trial_H(np.array(Hs_con))
ax_Hb.axhline(np.log(GRID_BIN), linewidth=0.3, linestyle='-.', color=COLOR_YELLOW_C, alpha=0.3, zorder=-1)
ax_Hc.axhline(np.log(GRID_CON), linewidth=0.3, linestyle='-.', color=COLOR_YELLOW_C, alpha=0.3, zorder=-1)
ax_Hb.set_title('Entropy of binary')
ax_Hc.set_title('Entropy of continuous')


# plot rho best
fig_Rb, ax_Rb = plot_trial_rho(np.array(rho_best_bin))
fig_Rc, ax_Rc = plot_trial_rho(np.array(rho_best_con))
ax_Rb.set_title(r'$\rho^{*}$' + ' (binary)')
ax_Rc.set_title(r'$\rho^{*}$' + ' (continuous)')


# parameter likelihood
fig_Lb, ax_Lb = plot_theta_L(L_bin, psy_bin['shape'], psy_bin['prior'], psy_bin['name'])
fig_Lc, ax_Lc = plot_theta_L(L_con, psy_con['shape'], psy_con['prior'], psy_con['name'])
ax_Lb.axvline(0, linewidth=0.3, linestyle='-.', color='k', alpha=0.3, zorder=-1) # prior
ax_Lc.axvline(0, linewidth=0.3, linestyle='-.', color='k', alpha=0.3, zorder=-1) # prior
ax_Lb.set_title('Log likelihood of '+ r'$\theta$' + ' (binary)')
ax_Lc.set_title('Log likelihood of '+ r'$\theta$' + ' (continuous)')