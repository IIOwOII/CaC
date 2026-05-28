#%% Library
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from mpl_toolkits.mplot3d import Axes3D

import json
import itertools
import math

#%% plotting
class meowfig:
    def __init__(self, nrows=1, ncols=1, figsize=(4,3), dpi=300, design=True, **kwargs):
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
dir_comp = '../MCmod/run/cacutil/components'
dir_beh = '../../CaC_Data'

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

# init Prior
theta_con_prior = np.array(psy_con['prior'])
theta_con_idx = np.array(list(itertools.product(*[np.arange(psy_con['shape'][i]) for i in range(4)])))


#%%
SUBJECTS = ['YSY', 'HES', 'PMJ']

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
    
    ftra_length = []
    ftra_wl = []
    ftra_t = []
    ftra_rho = []
    ftra_H = []
    ftra_maxEIG = []
    ftra_theta = []
    ftra_rho_wl = []
    ftra_rho_wl_sorted = []
    
    theta_star = []
    theta_PSI = []
    
    for subj in SUBJECTS:
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
        
        ftra_wl.append(np.array(fdat_play[-1]['winlose']))
        ftra_t.append(np.round(np.array(fdat_play[-1]['time'])/TPS, 1)) # sec
        ftra_length.append(ftra_t[-1].shape[0]) # number of trials (fit ses)
        ftra_rho.append(np.array([fdat_fit[-1][f'trial_{i}']['continuous']['rho_best'] for i in range(ftra_length[-1])]))
        ftra_H.append(np.array([fdat_fit[-1][f'trial_{i}']['continuous']['entropy'] for i in range(ftra_length[-1])]))
        ftra_maxEIG.append(np.array([max(fdat_fit[-1][f'trial_{i}']['continuous']['EIGs']) for i in range(ftra_length[-1])]))
        ftra_theta.append(np.array([fdat_fit[-1][f'trial_{i}']['continuous']['param_best'] for i in range(ftra_length[-1])]))
        ftra_rho_wl.append(np.vstack((ftra_rho[-1], ftra_wl[-1]))) # data sort
        ftra_rho_wl_sorted.append(ftra_rho_wl[-1][:, np.argsort(ftra_rho_wl[-1])[0]])
        
        theta_star_idx = np.where(np.all(theta_con_idx == ftra_theta[-1][-1], axis=1))[0][0]
        theta_star.append(theta_con[theta_star_idx])
        theta_PSI.append(P_con[:, theta_star_idx])
        
    # Plot
    # Difficulty - Win rate
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
        ax.set_title(f'{subj}-{TASK_NAME}')
    
    # Difficulty - Time
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
        ax.set_title(f'{subj}-{TASK_NAME}')
    
    
    
    #plot_trial_maxEIG(trace_maxEIG)
    #plt.title(f'{SUBJECT}-{TASK_NAME}')
    
    # for subj in SUBJECTS:
    #     fig, ax = plot_setting(xlabel='Trials', 
    #                            ylabel='Score',
    #                            xlim=[0, 20], ylim=[0, 100],
    #                            yticks=[0, 50, 100], yticklabels=[0, 50, 100]) # plot
    #     suv_ans = np.array([dat_suv[subj][suv]['answer'] for suv in dat_suv[subj]])
    #     ax.plot(np.arange(0,20), suv_ans, label=['perdiff','stress','willing','predwin'])
    #     plt.title(f'{SUBJECT}-{TASK_NAME}-Survey')
    #     ax.legend()
        
    #     fig, ax = plot_setting(xlabel='Trials', 
    #                            ylabel='Time(sec)',
    #                            xlim=[0, 20], ylim=[0, 30],
    #                            yticks=[0, 15, 30], yticklabels=[0, 15, 30]) # plot
    #     suv_t = np.array([dat_suv[subj][suv]['time'] for suv in dat_suv[subj]])/20
    #     ax.plot(np.arange(0,20), suv_t, label=['perdiff','stress','willing','predwin'])
    #     plt.title(f'{SUBJECT}-{TASK_NAME}-Survey')
    #     ax.legend()
    
    
    #     # surrender (ans)
    #     fig, ax = plot_setting(xlabel='Trials', 
    #                            xlim=[0, 20], ylim=[0, 1],
    #                            yticks=[0, 1], yticklabels=['quit', 'go']) # plot
    #     sur_ans = np.array([dat_sur[subj][sur]['answer'] for sur in dat_sur[subj]])
    #     line1 = ax.plot(np.arange(0,20), sur_ans, color='r', label='vote')
    #     plt.title(f'{subj}-{TASK_NAME}-Surrender')
    
    #     # surrender (t)
    #     ax2 = ax.twinx()
    #     sur_t = np.array([dat_sur[subj][sur]['time'] for sur in dat_sur[subj]])/20
    #     line2 = ax2.plot(np.arange(0,20), sur_t, color='b', label='time')
    #     for side in ['left', 'top', 'bottom']:
    #         ax2.spines[side].set_visible(False)
    #     plt.ylabel('time')
    #     lines = line1 + line2
    #     labels = [line.get_label() for line in lines]
    #     ax.legend(lines, labels)
    
    
    #     # surrender (ans)
    #     fig, ax = plot_setting(xlabel='Trials', 
    #                            xlim=[0, 20], ylim=[0, 100],
    #                            yticks=[0, 50, 100], yticklabels=[0, 50, 100]) # plot
    #     suv_ans = np.array([dat_suv[subj][suv]['answer'][0] for suv in dat_suv[subj]])
    #     line1 = ax.plot(np.arange(0,20), suv_ans, color='b', label='perdiff')
    #     plt.title(f'{subj}-{TASK_NAME}-perdiff and realdiff')
    #     ax.set_ylabel('survey answer score')
        
    #     ax2 = ax.twinx()
    #     ax2.set_ylim([0,100])
    #     ax2.set_yticks([0,50,100])
    #     ax2.set_yticklabels([100,50,0])
    #     ax2.set_ylabel('(%)')
    #     p_win = np.array(dat_play[subj]['predicted']['winrate'])*100
    #     line2 = ax2.plot(np.arange(0,20), 100-p_win, color='g', label='winrate by curve')
    #     for side in ['left', 'top', 'bottom']:
    #         ax2.spines[side].set_visible(False)
    #     lines = line1 + line2
    #     labels = [line.get_label() for line in lines]
    #     ax.legend(lines, labels)
    
    plt.show()