#%% Library
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import LinearSegmentedColormap

import json
import itertools
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
        self.axes[0,0].set_ylabel(self.ylabel)
        for rax in self.axes:
            for ax in rax:
                ax.set_title(self.title)
                ax.set_xlabel(self.xlabel)
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
COLOR_RED_C = '#FC6255'
COLOR_BLUE_C = '#58C4DD'
COLOR_GREEN_C = '#83C167'
COLOR_YELLOW_C = '#F7D96F'
COLOR_PURPLE_C = '#9A72AC'
COLOR_GOLD_C = '#F0AC5F'
COLOR_JERRY = LinearSegmentedColormap.from_list('jerry', ['#A46E24','#CA8628','#E9A547'])
COLOR_TOM = LinearSegmentedColormap.from_list('tom', ['#6D6C6D','#998999','#CAC4C4'])


#%% Util
def psi_heatmap(dat_t, dat_rho, fit_theta, task_name):
    dt = 0.1
    ts = np.round(np.arange(dt, 50+dt, dt), 2)
    rh = np.round(np.arange(0.7, 1.31, 0.01), 2)
    if (task_name == 'chasing'): rhh = 1/rh
    elif (task_name == 'chased'): rhh = rh
    z = cal_Polyexp_psi_opt(ts, rhh, fit_theta)
    c_t = np.array([np.where(ts==t)[0][0] for t in dat_t])
    c_rho = np.array([np.where(rh==r)[0][0] for r in dat_rho])
    return z, c_rho, c_t


#%% Functions
# rho - P
def cal_Polyexp_PSI(rho_hat, theta, task_name):
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
    
    if (task_name == 'chasing'): PSI = P_hit
    elif (task_name == 'chased'): PSI = 1-P_hit
    return PSI

def cal_Polyexp_PSI_opt(rho_hat, theta_star, task_name):
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
    if (task_name == 'chasing'): PSI = P_hit
    elif (task_name == 'chased'): PSI = 1-P_hit
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

def cal_Polyexp_L(L, rho_idx, t, theta):
    # X = k*t/mu
    k, m, h, w = theta.T
    x = X_COEF[rho_idx] * t # [grid]
    vec_fact = np.vectorize(math.gamma)
    
    L_update = L + k*np.log(x)-x-np.log(t)-np.log(vec_fact(k))
    L_update = norm_L(L_update)
    return L_update

def norm_L(L_hat):
    L = L_hat - np.log(np.sum(np.exp(L_hat)))
    return L

#%% HYP
T = 30
TPS = 20
P_MIN = 1.0E-12
P_MAX = 1 - 1.0E-12
RHO_SIZE = 60
RHO = np.round(np.linspace(0.7, 1.29, RHO_SIZE), 2)

M = 100.0
dir_comp = '../MCmod/run/cacutil/components'
dir_beh = '../../CaC_Data'

# Point load
with open(f'{dir_comp}/pool_point.json', 'r') as f:
    pool_point = json.load(f)
border_start = pool_point['border']['start']
border_end = pool_point['border']['end']

# Param load
with open(f'{dir_comp}/pool_psychometric.json', 'r') as f_psy:
    pool_psy = json.load(f_psy)
psy_con = pool_psy['continuous']['parameter']
GRID_CON = np.prod(psy_con['shape']) # flatten
H_MAX_con = np.log(GRID_CON)

# init Param
theta_con = []
for i in range(4):
    theta_con.append(np.round(np.linspace(psy_con['min'][i], psy_con['max'][i]-psy_con['step'][i], psy_con['shape'][i]), 3))
theta_con = np.array(list(itertools.product(*theta_con)))
theta_con_idx = np.array(list(itertools.product(*[np.arange(psy_con['shape'][i]) for i in range(4)])))
theta_prior = np.array(psy_con['prior'])

#%%
subj = 'YSY'

for task in ['chasing', 'chased']:
    if (task == 'chasing'): 
        RHO_HAT = 1.0/RHO
        TASK_TYPE = 0
    elif (task == 'chased'): 
        RHO_HAT = RHO
        TASK_TYPE = 1
    
    # init P, X coef
    MU = cal_Mu(RHO_HAT, theta_con)
    X_COEF = cal_X_coef(theta_con)
    
    # current
    P_con = cal_Polyexp_PSI(RHO_HAT, theta_con, task)
    L_con = np.zeros(GRID_CON) # [grid]
    H_con = H_MAX_con
    
    # Prior
    L_con = np.log(0.5+0.5*np.exp(-np.sum(((theta_con_idx-theta_prior)/np.array(psy_con['shape']))**2, axis=-1)/2.0))
    L_con = norm_L(L_con)
    
    # File load
    with open(f'{dir_beh}/{subj}/fitting_{task}/log_fitting.json', 'r') as f:
        fdat_fit = json.load(f)['cac']
    with open(f'{dir_beh}/{subj}/fitting_{task}/log_gameplay.json', 'r') as f:
        fdat_play = json.load(f)['cac']
    with open(f'{dir_beh}/{subj}/{task}_0/log_gameplay.json', 'r') as f:
        dat_play = json.load(f)['cac']
    
    ftra_wl = np.array(fdat_play['winlose'])
    ftra_t = np.round(np.array(fdat_play['time'])/TPS, 1) # sec
    ftra_length = ftra_t.shape[0] # number of trials (fit ses)
    ftra_rho = np.array([fdat_fit[f'trial_{i}']['continuous']['rho_best'] for i in range(ftra_length)])
    ftra_H = np.array([fdat_fit[f'trial_{i}']['continuous']['entropy'] for i in range(ftra_length)])
    ftra_maxEIG = np.array([max(fdat_fit[f'trial_{i}']['continuous']['EIGs']) for i in range(ftra_length)])
    ftra_theta = np.array([fdat_fit[f'trial_{i}']['continuous']['param_best'] for i in range(ftra_length)])
    ftra_rho_wl = np.vstack((ftra_rho, ftra_wl)) # data sort
    ftra_rho_wl_sorted = ftra_rho_wl[:, np.argsort(ftra_rho_wl)[0]]
    
    tra_wl = np.array(dat_play['winlose'])
    tra_t = np.round(np.array(dat_play['time'])/TPS, 1)
    tra_rho = np.array(dat_play['difficulty'])
    tra_rho_wl = np.vstack((tra_rho, tra_wl)) # data sort
    tra_rho_wl_sorted = tra_rho_wl[:, np.argsort(tra_rho_wl)[0]]
    
    theta_star_idx = np.where(np.all(theta_con_idx == ftra_theta[-1], axis=1))[0][0]
    theta_star = theta_con[theta_star_idx]
    theta_PSI = P_con[:, theta_star_idx]
    
    # Plot
    # Difficulty - Win rate (fit)
    fig_rho_p_fit = meowfig(nrows=1, ncols=1,
                            xlabel=r'$\rho$'+' (Difficulty)', 
                            ylabel=r'$\Psi$'+' (Win Rate)',
                            xlim=[0.7, 1.3], ylim=[0, 1],
                            yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1],
                            yoffset=0)
    for i, ax in enumerate(fig_rho_p_fit.axes[0]):
        ax.scatter(ftra_rho_wl[0], ftra_rho_wl[1],
                   s=1, color='gray', alpha=0.2, zorder=0)
        c_rho = np.unique(ftra_rho) # duple remove
        c_p = np.array([np.mean(ftra_rho_wl_sorted[1], where=(ftra_rho_wl_sorted[0]==r)) for r in c_rho])
        ax.scatter(c_rho, c_p, s=1, color=COLOR_BLUE_C, zorder=1)
        ax.plot(RHO, theta_PSI, linewidth=1, color=COLOR_GOLD_C, zorder=2)
        ax.set_title(f'{subj}-{task} (fit)')
    
    # Difficulty - Time (fit)
    fig_rho_t_fit = meowfig(nrows=1, ncols=1,
                            xlabel=r'$\rho$'+' (Difficulty)', 
                            ylabel=r'$t$'+' (Time)',
                            xticks=[0, 10, 20, 30, 40, 50, 60], 
                            xticklabels=[0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3],
                            yticks=[99, 199, 299, 399, 499], 
                            yticklabels=[10, 20, 30, 40, 50])
    for i, ax in enumerate(fig_rho_t_fit.axes[0]):
        z, c_rho, c_t = psi_heatmap(ftra_t, ftra_rho, theta_star, task)
        ax.imshow(z, origin='lower', aspect='auto', cmap='YlGn')
        ax.axhline(299, linewidth=0.3, linestyle='-.', color=COLOR_RED_C, zorder=1)
        ax.scatter(c_rho, c_t, s=1, color='k', zorder=2)
        ax.set_title(f'{subj}-{task} (fit)')
    
    plt.show()
    
    # simul fitting by real data
    for n in range(20):
        # update trial before
        ExP_con = cal_ExP(P_con, L_con)
        ExL_con = cal_ExL(P_con, L_con, ExP_con)
        ExH_con = cal_ExH(ExL_con)
        EIG_con = cal_EIG(H_con, ExP_con, ExH_con)
        
        # update trial after
        if (tra_wl[n] == 0): wl_idx = 1
        elif (tra_wl[n] == 1): wl_idx = 0
        rho_idx = np.where(RHO==np.round(tra_rho[n],2))[0][0]
        
        L_con_past = np.copy(L_con)
        H_con_past = H_con
        if ((task == 'chasing' and tra_wl[n] == 0) or (task == 'chased' and tra_wl[n] == 1)):
            L_con = np.copy(ExL_con[wl_idx][rho_idx])
            H_con = ExH_con[wl_idx][rho_idx]
        else:
            L_con = cal_Polyexp_L(L_con_past, rho_idx, tra_t[n], theta_con)
            H_con = cal_H(L_con)
        
        # plotting raw data and fit data
        PSI_fit_con = P_con[:, np.argmax(L_con)]
        MU_fit_con = MU[:, np.argmax(L_con)]
        
        # Difficulty - Win rate (real time)
        fig_rho_p = meowfig(nrows=1, ncols=1,
                            xlabel=r'$\rho$'+' (Difficulty)', 
                            ylabel=r'$\Psi$'+' (Win Rate)',
                            xlim=[0.7, 1.3], ylim=[0, 1],
                            yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1],
                            yoffset=0)
        for i, ax in enumerate(fig_rho_p.axes[0]):
            ax.scatter(tra_rho_wl[0][:n+1], tra_rho_wl[1][:n+1],
                       s=1, color='gray', alpha=0.2, zorder=0)
            # c_rho = np.unique(tra_rho) # duple remove
            # c_p = np.array([np.mean(tra_rho_wl_sorted[1], where=(tra_rho_wl_sorted[0]==r)) for r in c_rho])
            # ax.scatter(c_rho, c_p, s=1, color=COLOR_BLUE_C, zorder=1)
            ax.plot(RHO, P_con[:, np.argmax(L_con)], linewidth=1, color=COLOR_GOLD_C, zorder=2)
            ax.set_title(f'{subj}-{task}')
        
        # Difficulty - Time (real time)
        fig_rho_t = meowfig(nrows=1, ncols=1,
                            xlabel=r'$\rho$'+' (Difficulty)', 
                            ylabel=r'$t$'+' (Time)',
                            xticks=[0, 10, 20, 30, 40, 50, 60], 
                            xticklabels=[0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3],
                            yticks=[99, 199, 299, 399, 499], 
                            yticklabels=[10, 20, 30, 40, 50])
        for i, ax in enumerate(fig_rho_t.axes[0]):
            z, c_rho, c_t = psi_heatmap(tra_t[:n+1], np.round(tra_rho[:n+1], 2), theta_con[np.argmax(L_con)], task)
            ax.imshow(z, origin='lower', aspect='auto', cmap='YlGn')
            ax.axhline(299, linewidth=0.3, linestyle='-.', color=COLOR_RED_C, zorder=1)
            ax.scatter(c_rho, c_t, s=1, color='k', zorder=2)
            ax.set_title(f'{subj}-{task}')
        
        plt.show()
        
        # print log
        print(' ')
        print(f'---trial {n}---')
        print(f'difficulty: {tra_rho[n]}')
        print(f'time: {tra_t[n]}')
        print(f'winlose: {tra_wl[n]}')