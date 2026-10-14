import numpy as np
import matplotlib.pyplot as plt
from itertools import product
import json

from sisypuss.graph import *

# import multiprocessing as mp
# from functools import partial

def calGammaCDF(k, lam):
    k_isint = int(np.round(2*k))%2
    if (k_isint == 0):
        series = 0
        for i in range(0, int(k)):
            series += ((lam*T)**i)/factorial(i)
        P_nhit = np.exp(-lam*T)*series
    elif (k_isint == 1):
        if (k == 0.5):
            P_nhit = calErfcsqrt(lam*T)
        else:
            a = 1
            series = 0
            for i in range(0, int(k-0.5)):
                if (i != 0):
                    a = a * (2/(2*i+1))
                series += a*((lam*T)**i)
            P_nhit = calErfcsqrt(lam*T) + 2*np.exp(-lam*T)*np.sqrt(lam*T/np.pi)*series
    P = (1+C_PRE)/2 - C_PRE*P_nhit
    return P

def calGammaPDF(k, lam, t):
    if (t<=0): return 0
    p = ((lam*t)**k) * np.exp(-lam*t) / (t*np.exp(loggamma(k)))
    return p

def calGammaLambda(r, k, m, h, w):
    rho_hat = np.exp(r*C_PRE)
    h_hat = h ** (-C_PRE)
    w_hat = np.exp(-w)
    lam = (k/T)*((m+((k/(calGammaLambdaMedian(k)*T))-m)*np.exp(-((rho_hat-h_hat)/w_hat)))**(-1))
    return lam

def calGammaLambdaMedian(k):
    lam_med = (k-1/3)/T
    if (k < 1):
        lam_med = (2**(-1/k))*0.56147*(2.27611**k)/T
    return lam_med

def calErfcsqrt(x):
    y = 1 - np.sqrt(1-np.exp(-x*((1.27324+0.147*x)/(1+0.147*x))))
    return y

def loggamma(s):
    y = 0
    s_isint = int(np.round(2*s))%2
    if (s_isint == 0):
        if (s == 1):
            y = 0.0
        else:
            for i in range(1,int(s)):
                y += np.log(i)
    elif (s_isint == 1):
        if (s == 0.5):
            y = 0.5*np.log(np.pi)
        else:
            series = 0
            for i in range(1,int(s+0.5)):
                series += np.log(2*i-1)
            y = 0.5*np.log(2*np.pi) - s*np.log(2) + series
    return y

def factorial(s):
    value = 1
    if (s == 0): return value
    for i in range(1, s+1):
        value *= i
    return value

def RtoRho(r):
    rho = np.round(np.exp(-r), 4)
    return rho


#%% Variables and Constants

# 1. variables
subj = 'PBJ2'
task_num = 0

# 2. directory
dir_main = '../../CaC_Analysis'
dir_beh = f'{dir_main}/Beh'
dir_subj = f'{dir_beh}/{subj}'

# 3. constants
T = 30
TASKS = ['fitting_chasing', 'fitting_chased', 'chasing', 'chased'] # task list
task = TASKS[task_num]
if (task_num == 0) or (task_num == 2):
    C_PRE = 1
elif (task_num == 1) or (task_num == 3):
    C_PRE = -1

# 4. Data Load
with open(f'{dir_subj}/{task}/log_fitting.json', 'r') as f:
    log_fit = json.load(f)['cac']
fit_final = log_fit['final']['neo']



#%%
R = np.linspace(-0.28, 0.7, 50)
Rho = RtoRho(R)
THETA_STAR = np.array(fit_final['theta'])


LAM = np.zeros(50)
P = np.zeros(50)
for i, r in enumerate(R):
    LAM[i] = calGammaLambda(r, *THETA_STAR)
    P[i] = calGammaCDF(THETA_STAR[0], LAM[i])

fig_rho_p_fit = meowfig(xlabel=r'$\rho$'+' (Difficulty)', 
                        ylabel=r'$\Psi$'+' (Win Rate)',
                        xlim=[0.5, 1.3], ylim=[0, 1],
                        yticks=[0, 0.5, 1], yticklabels=[0, 0.5, 1],
                        yoffset=0)
ax = fig_rho_p_fit.axes[0,0]
ax.plot(Rho, P, linewidth=1, color=COLOR_GOLD_C, zorder=2)
ax.set_title(f'{subj}-{task}')


#%%
# GRIDSIZE = 180000
# R = np.linspace(-0.28, 0.7, 50)
# K = np.linspace(0.5, 20, 40)
# M = np.linspace(0.1, 0.3, 5)
# H = np.linspace(0.6, 1.18, 30)
# W = np.linspace(1.1, 4, 30)
# THETA = np.array(list(product(K, M, H, W))).T

# n_work = 25
# func = partial(np.vectorize(calGammaLambda), k=THETA[0], m=THETA[1], h=THETA[2], w=THETA[3])
# with mp.Pool(processes=n_work) as pool:
#     LAM = np.array(pool.map(func, R))

# LAM = np.zeros((50, GRIDSIZE))
# func = np.vectorize(calGammaLambda)
# for i, r in enumerate(R):
#     LAM[i] = func(r, *THETA)

# P = np.zeros((50, GRIDSIZE))
# func = np.vectorize(calGammaCDF)
# for i, r in enumerate(R):
#     P[i] = func(THETA[0], LAM[i])


