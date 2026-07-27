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
    
# surrender vote interval making
def sort_surrender(trace_vote):
    # if n=0, 4, 5, 8 is surrender,
    # return [0,1], [4,6], [8,9]
    N = trace_vote.shape[0]
    range_vote = []
    range_vote_temp = [-1, -1]
    if (trace_vote[0]==0): # if surrender on first trial
        range_vote_temp[0] = 0
    for i in range(1, N):
        if (trace_vote[i-1]==1 and trace_vote[i]==0):
            range_vote_temp[0] = i
        if (trace_vote[i-1]==0 and trace_vote[i]==1):
            range_vote_temp[1] = i
            range_vote.append(range_vote_temp.copy())
            range_vote_temp = [-1, -1]
    if range_vote_temp[0] != -1: # if surrender on last trial
        range_vote_temp[1] = N
        range_vote.append(range_vote_temp.copy())
    return range_vote

# winlose interval making
def sort_winlose(trace_wl):
    # if n=0, 4, 5, 8 is surrender,
    # return [0,1], [4,6], [8,9]
    N = trace_wl.shape[0]
    range_win = []
    range_lose = []
    range_win_temp = [-1, -1]
    range_lose_temp = [-1, -1]
    if (trace_wl[0]==1):
        range_win_temp[0] = 0
    else:
        range_lose_temp[0] = 0
    for i in range(1, N):
        if (trace_wl[i-1]==1 and trace_wl[i]==0):
            range_win_temp[1] = i
            range_lose_temp[0] = i
            range_win.append(range_win_temp.copy())
            range_win_temp = [-1,-1]
        elif (trace_wl[i-1]==0 and trace_wl[i]==1):
            range_win_temp[0] = i
            range_lose_temp[1] = i
            range_lose.append(range_lose_temp.copy())
            range_lose_temp = [-1,-1]
    if (range_win_temp[0] != -1):
        range_win_temp[1] = N
        range_win.append(range_win_temp.copy())
    elif (range_lose_temp[0] != -1):
        range_lose_temp[1] = N
        range_lose.append(range_lose_temp.copy())
    return range_win, range_lose


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


#%% hyp
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
with open(f'{dir_comp}/info_obstacle.json', 'r') as f:
    info_obs = json.load(f)
point_wall = np.array(info_obs['wall_point']).T
point_obs = np.array(info_obs['obstacle_point']).T

# Param load
with open(f'{dir_comp}/pool_psychometric.json', 'r') as f_psy:
    pool_psy = json.load(f_psy)
psy_con = pool_psy['continuous']['parameter']
GRID_CON = np.prod(psy_con['shape']) # flatten

# init Param
theta_con = []
for i in range(4):
    theta_con.append(np.round(np.linspace(psy_con['min'][i], psy_con['max'][i]-psy_con['step'][i], psy_con['shape'][i]), 3))
theta_con = np.array(list(itertools.product(*theta_con)))
theta_con_idx = np.array(list(itertools.product(*[np.arange(psy_con['shape'][i]) for i in range(4)])))


#%%
SUBJECTS = ['b01', 'b02', 'b03', 'b04']

for TASK_NAME in ['chasing', 'chased']:
    if (TASK_NAME == 'chasing'): 
        RHO_HAT = 1.0/RHO
        TASK_TYPE = 0
    elif (TASK_NAME == 'chased'): 
        RHO_HAT = RHO
        TASK_TYPE = 1
    
    # init P, X coef
    MU = cal_Mu(RHO_HAT, theta_con)
    X_COEF = cal_X_coef(theta_con)
    P_con = cal_Polyexp_PSI(RHO_HAT, theta_con, TASK_NAME)
    
    # File Load
    fdat_fit = []
    fdat_play = []
    dat_suv = []
    dat_play = []
    dat_sur = []
    dat_pos = []
    
    # Trace (fit)
    ftra_length = []
    ftra_wl = []
    ftra_t = []
    ftra_rho = []
    ftra_H = []
    ftra_maxEIG = []
    ftra_theta = []
    ftra_rho_wl = []
    ftra_rho_wl_sorted = []
    
    # Trace (main)
    tra_wl = []
    tra_t = []
    tra_rho = []
    tra_rho_wl = []
    tra_rho_wl_sorted = []
    tra_win_range = []
    tra_lose_range = []
    
    # Trace (survey & surrender)
    stra_score = []
    stra_score_t = []
    stra_vote = []
    stra_vote_t = []
    stra_vote_range = []
    
    # Trace (theta)
    theta_star = []
    theta_PSI = []
    
    # Trace (Position)
    ptra_pred_x = np.full((len(SUBJECTS), 20, 600), np.nan)
    ptra_pred_z = np.full((len(SUBJECTS), 20, 600), np.nan)
    ptra_pred_r = np.full((len(SUBJECTS), 20, 600), np.nan)
    ptra_prey_x = np.full((len(SUBJECTS), 20, 600), np.nan)
    ptra_prey_z = np.full((len(SUBJECTS), 20, 600), np.nan)
    ptra_prey_r = np.full((len(SUBJECTS), 20, 600), np.nan)
    
    for idx, subj in enumerate(SUBJECTS):
        with open(f'{dir_beh}/{subj}/fitting_{TASK_NAME}/log_fitting.json', 'r') as f:
            fdat_fit.append(json.load(f)['cac'])
        with open(f'{dir_beh}/{subj}/fitting_{TASK_NAME}/log_gameplay.json', 'r') as f:
            fdat_play.append(json.load(f)['cac'])
        with open(f'{dir_beh}/{subj}/{TASK_NAME}/log_survey.json', 'r') as f:
            dat_suv.append(json.load(f)['cac'])
        with open(f'{dir_beh}/{subj}/{TASK_NAME}/log_gameplay.json', 'r') as f:
            dat_play.append(json.load(f)['cac'])
        with open(f'{dir_beh}/{subj}/{TASK_NAME}/log_surrender.json', 'r') as f:
            dat_sur.append(json.load(f)['cac'])
        with open(f'{dir_beh}/{subj}/{TASK_NAME}/log_position.json', 'r') as f:
            dat_pos.append(json.load(f)['cac'])
        
        ftra_wl.append(np.array(fdat_play[-1]['winlose']))
        ftra_t.append(np.round(np.array(fdat_play[-1]['time'])/TPS, 1)) # sec
        ftra_length.append(ftra_t[-1].shape[0]) # number of trials (fit ses)
        ftra_rho.append(np.array([fdat_fit[-1][f'trial_{i}']['continuous']['rho_best'] for i in range(ftra_length[-1])]))
        ftra_H.append(np.array([fdat_fit[-1][f'trial_{i}']['continuous']['entropy'] for i in range(ftra_length[-1])]))
        ftra_maxEIG.append(np.array([max(fdat_fit[-1][f'trial_{i}']['continuous']['EIGs']) for i in range(ftra_length[-1])]))
        ftra_theta.append(np.array([fdat_fit[-1][f'trial_{i}']['continuous']['param_best'] for i in range(ftra_length[-1])]))
        ftra_rho_wl.append(np.vstack((ftra_rho[-1], ftra_wl[-1]))) # data sort
        ftra_rho_wl_sorted.append(ftra_rho_wl[-1][:, np.argsort(ftra_rho_wl[-1])[0]])
        
        tra_wl.append(np.array(dat_play[-1]['winlose']))
        tra_t.append(np.round(np.array(dat_play[-1]['time'])/TPS, 1))
        tra_rho.append(np.array(dat_play[-1]['difficulty']))
        tra_rho_wl.append(np.vstack((tra_rho[-1], tra_wl[-1]))) # data sort
        tra_rho_wl_sorted.append(tra_rho_wl[-1][:, np.argsort(tra_rho_wl[-1])[0]])
        tra_win_temp, tra_lose_temp = sort_winlose(tra_wl[-1])
        tra_win_range.append(tra_win_temp.copy())
        tra_lose_range.append(tra_lose_temp.copy())
        
        stra_score.append(np.array([dat_suv[-1][f'trial_{i}']['answer'] for i in range(20)]))
        stra_score_t.append(np.array([dat_suv[-1][f'trial_{i}']['time'] for i in range(20)]))
        stra_vote.append(np.array([dat_sur[-1][f'trial_{i}']['answer'] for i in range(20)]))
        stra_vote_t.append(np.array([dat_sur[-1][f'trial_{i}']['time'] for i in range(20)]))
        stra_vote[-1][stra_vote_t[-1]<=5] = 1 # mistake adjust
        stra_vote_range.append(sort_surrender(stra_vote[-1]))
        
        theta_star_idx = np.where(np.all(theta_con_idx == ftra_theta[-1][-1], axis=1))[0][0]
        theta_star.append(theta_con[theta_star_idx])
        theta_PSI.append(P_con[:, theta_star_idx])
        
        for j in range(20):
            pos_pred = dat_pos[-1][f'trial_{j}']['gameplay']['predator']
            pos_prey = dat_pos[-1][f'trial_{j}']['gameplay']['prey']
            ptra_pred_x[idx,j,:len(pos_pred['x'])] = pos_pred['x']
            ptra_pred_z[idx,j,:len(pos_pred['z'])] = pos_pred['z']
            ptra_pred_r[idx,j,:len(pos_pred['r'])] = pos_pred['r']
            ptra_prey_x[idx,j,:len(pos_prey['x'])] = pos_prey['x']
            ptra_prey_z[idx,j,:len(pos_prey['z'])] = pos_prey['z']
            ptra_prey_r[idx,j,:len(pos_prey['r'])] = pos_prey['r']
    
    # Plot
    # Difficulty - Win rate (fit)
    fig_rho_p_fit = meowfig(nrows=1, ncols=len(SUBJECTS),
                            xlabel=r'$\rho$'+' (Difficulty)', 
                            ylabel=r'$\Psi$'+' (Win Rate)',
                            xlim=[0.7, 1.3], ylim=[0, 1],
                            yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1],
                            yoffset=0)
    for i, ax in enumerate(fig_rho_p_fit.axes[0]):
        subj = SUBJECTS[i]
        ax.scatter(ftra_rho_wl[i][0], ftra_rho_wl[i][1],
                   s=1, color='gray', alpha=0.2, zorder=0)
        c_rho = np.unique(ftra_rho[i]) # duple remove
        c_p = np.array([np.mean(ftra_rho_wl_sorted[i][1], where=(ftra_rho_wl_sorted[i][0]==r)) for r in c_rho])
        ax.scatter(c_rho, c_p, s=1, color=COLOR_BLUE_C, zorder=1)
        ax.plot(RHO, theta_PSI[i], linewidth=1, color=COLOR_GOLD_C, zorder=2)
        ax.set_title(f'{subj}-{TASK_NAME} (fit)')
    
    # Difficulty - Time (fit)
    fig_rho_t_fit = meowfig(nrows=1, ncols=len(SUBJECTS),
                            xlabel=r'$\rho$'+' (Difficulty)', 
                            ylabel=r'$t$'+' (Time)',
                            xticks=[0, 10, 20, 30, 40, 50, 60], 
                            xticklabels=[0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3],
                            yticks=[99, 199, 299, 399, 499], 
                            yticklabels=[10, 20, 30, 40, 50])
    for i, ax in enumerate(fig_rho_t_fit.axes[0]):
        subj = SUBJECTS[i]
        z, c_rho, c_t = psi_heatmap(ftra_t[i], ftra_rho[i], theta_star[i], TASK_NAME)
        ax.imshow(z, origin='lower', aspect='auto', cmap='YlGn')
        ax.axhline(299, linewidth=0.3, linestyle='-.', color=COLOR_RED_C, zorder=1)
        ax.scatter(c_rho, c_t, s=1, color='k', zorder=2)
        ax.set_title(f'{subj}-{TASK_NAME} (fit)')
    
    # Trial - maxEIG (fit)
    max_trial_fit = 25
    fig_maxEIG_fit = meowfig(nrows=1, ncols=len(SUBJECTS),
                             xlabel=r'$N$'+' (Trial)',
                             ylabel='max expected information gain',
                             xlim=[0, max_trial_fit], ylim=[0, 0.4],
                             xticks=[0, 5, 10, 15, 20, 25],
                             yticks=[0, 0.1, 0.2, 0.3, 0.4],
                             yoffset=0)
    for i, ax in enumerate(fig_maxEIG_fit.axes[0]):
        subj = SUBJECTS[i]
        ax.plot(np.arange(ftra_maxEIG[i].shape[0]), ftra_maxEIG[i], color=COLOR_BLUE_C, zorder=1)
        ax.axhline(0.05, linewidth=0.3, linestyle='-.', color=COLOR_RED_C, zorder=2)
        ax.set_title(f'{subj}-{TASK_NAME} (fit)')
    
    # Trial - Entropy (fit)
    '''
    max_H_fit = np.log(theta_con.shape[0])
    fig_H_fit = meowfig(nrows=1, ncols=len(SUBJECTS),
                        xlabel=r'$N$'+' (Trial)',
                        ylabel=r'$log(H)$'+' (Entropy)',
                        xlim=[0, max_trial_fit], ylim=[8, max_H_fit],
                        xticks=[0, 5, 10, 15, 20, 25],
                        yticks=[8, 10, 12])
    for i, ax in enumerate(fig_H_fit.axes[0]):
        subj = SUBJECTS[i]
        ax.plot(np.arange(ftra_H[i].shape[0]), ftra_H[i], color=COLOR_BLUE_C, zorder=1)
        ax.set_title(f'{subj}-{TASK_NAME} (fit)')
    '''
    
    # Trial - Score (survey & surrender)
    max_trial = 20
    fig_score = meowfig(nrows=1, ncols=len(SUBJECTS),
                        xlabel=r'$N$'+' (Trial)',
                        ylabel='Survey Answer',
                        xlim=[0, max_trial], ylim=[0, 100],
                        xticks=[0, 5, 10, 15, 20],
                        yticks=[0, 50, 100], yticklabels=['No', '', 'Yes'])
    for i, ax in enumerate(fig_score.axes[0]):
        subj = SUBJECTS[i]
        ax.plot(np.arange(max_trial), stra_score[i], linewidth=0.5,
                label=['is hard?','stress','willing','can win?'])
        if (len(tra_win_range[i]) != 0):
            for j in tra_win_range[i]:
                ax.fill_between(j, [0,0], [100,100], color=COLOR_GREEN_C, alpha=0.2, edgecolor='none')
        if (len(tra_lose_range[i]) != 0):
            for j in tra_lose_range[i]:
                ax.fill_between(j, [0,0], [100,100], color=COLOR_RED_C, alpha=0.2, edgecolor='none')
        stra_vote_index = np.where(stra_vote[i]==0)[0]
        if (stra_vote_index.shape[0] != 0):
            ax.scatter(stra_vote_index+0.5, np.ones(stra_vote_index.shape[0])*100, 
                       s=10, marker='^', color=COLOR_PURPLE_C)
        # if (len(stra_vote_range[i]) != 0):
        #     for j in stra_vote_range[i]:
        #         ax.fill_between(j, [0,0], [100,100], color=COLOR_RED_C, alpha=0.2, edgecolor='none')
        ax.set_title(f'{subj}-{TASK_NAME}')
    fig_score.axes[0][-1].legend()
    
    # Difficulty - Win rate
    fig_rho_p = meowfig(nrows=1, ncols=len(SUBJECTS),
                        xlabel=r'$\rho$'+' (Difficulty)', 
                        ylabel=r'$\Psi$'+' (Win Rate)',
                        xlim=[0.7, 1.3], ylim=[0, 1],
                        yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1],
                        yoffset=0)
    for i, ax in enumerate(fig_rho_p.axes[0]):
        subj = SUBJECTS[i]
        ax.scatter(tra_rho_wl[i][0], tra_rho_wl[i][1],
                   s=1, color='gray', alpha=0.2, zorder=0)
        c_rho = np.unique(tra_rho[i]) # duple remove
        c_p = np.array([np.mean(tra_rho_wl_sorted[i][1], where=(tra_rho_wl_sorted[i][0]==r)) for r in c_rho])
        ax.scatter(c_rho, c_p, s=1, color=COLOR_BLUE_C, zorder=1)
        ax.plot(RHO, theta_PSI[i], linewidth=1, color=COLOR_GOLD_C, zorder=2)
        ax.set_title(f'{subj}-{TASK_NAME}')
    
    # Difficulty - Time
    fig_rho_t = meowfig(nrows=1, ncols=len(SUBJECTS),
                        xlabel=r'$\rho$'+' (Difficulty)', 
                        ylabel=r'$t$'+' (Time)',
                        xticks=[0, 10, 20, 30, 40, 50, 60], 
                        xticklabels=[0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3],
                        yticks=[99, 199, 299, 399, 499], 
                        yticklabels=[10, 20, 30, 40, 50])
    for i, ax in enumerate(fig_rho_t.axes[0]):
        subj = SUBJECTS[i]
        z, c_rho, c_t = psi_heatmap(tra_t[i], np.round(tra_rho[i], 2), theta_star[i], TASK_NAME)
        ax.imshow(z, origin='lower', aspect='auto', cmap='YlGn')
        ax.axhline(299, linewidth=0.3, linestyle='-.', color=COLOR_RED_C, zorder=1)
        ax.scatter(c_rho, c_t, s=1, color='k', zorder=2)
        ax.set_title(f'{subj}-{TASK_NAME}')
        
    # Position
    play_time = np.linspace(0.05, 600, 12000)
    fig_pos = meowfig(nrows=1, ncols=len(SUBJECTS), grid=True, 
                      xlabel=r'$x$', ylabel=r'$z$',
                      xlim=[border_start[0], border_end[0]],
                      ylim=[border_start[2], border_end[2]],
                      xticks=np.arange(border_start[0],border_end[0]+1,8).tolist(),
                      yticks=np.arange(border_start[2],border_end[2]+1,8).tolist())
    for i, ax in enumerate(fig_pos.axes[0]):
        subj = SUBJECTS[i]
        sca_tom = ax.scatter(ptra_pred_x[i], ptra_pred_z[i], s=0.1, 
                             c=play_time, cmap=COLOR_TOM)
        sca_jerry = ax.scatter(ptra_prey_x[i], ptra_prey_z[i], s=0.1, 
                               c=play_time, cmap=COLOR_JERRY)
        ax.scatter(point_wall[0]+0.5, point_wall[1]+0.5, s=10, color='k', marker='s', alpha=0.5)
        ax.scatter(point_obs[0]+0.5, point_obs[1]+0.5, s=10, color='k', marker='s', alpha=0.5)
        ax.set_title(f'{subj}-{TASK_NAME}')
    #fig_pos.fig.colorbar(sca_tom, ax=fig_pos.axes[0][-1])
    #fig_pos.fig.colorbar(sca_jerry, ax=fig_pos.axes[0][-1])
    
    plt.show()