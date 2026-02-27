#%% Library
import numpy as np
import matplotlib.pyplot as plt

import json
import itertools
import math
import copy

#%% Functions
def func_logistic(rho, m, w):
    y = 1.0/(1 + (9**((rho-m)/w)))
    return y


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


def cal_PSI(rho, m, w, gam, lam):
    F = func_logistic(rho, m, w)
    P = gam + (1-gam-lam)*F
    return P


def cal_Polyexp_PSI(rho, k, m, h, w):
    if (TASK == 0):
        rho_hat = 1/rho
    elif (TASK == 1):
        rho_hat = rho
        
    X = cal_X(T, rho_hat, k, m, h, w)
    series = 0
    
    for i in range(np.round(k).astype(int)):
        series += (X**i)/math.factorial(i)
    P_hit = 1 - np.exp(-X)*series
    
    if (TASK == 0):
        PSI = P_hit
    elif (TASK == 1):
        PSI = 1-P_hit
    if (PSI < P_MIN):
        PSI = P_MIN
    elif (PSI > P_MAX):
        PSI = P_MAX
    return PSI
    

def cal_Mu(rho_hat, k, m, h, w):
    if (rho_hat >= (h-w)+(w/(10.0-m))):
        mu = T*(m+(1.0/(1+((rho_hat-h)/w))))
    else:
        mu = 10.0*T
    return mu


def cal_X(t, rho_hat, k, m, h, w):
    x = (k*t)/cal_Mu(rho_hat, k, m, h, w)
    return x


def cal_X_coef(rho_hat, k, m, h, w):
    x_coef = k/cal_Mu(rho_hat, k, m, h, w)
    return x_coef


def thres_P(P):
    P[P<P_MIN] = P_MIN
    P[P>P_MAX] = P_MAX
    return P


#%% Param load
TASK = 0
T = 30
P_MIN = 1.0E-12
P_MAX = 1 - 1.0E-12

dir_comp = '../MCmod/run/cacutil/components'

with open(f'{dir_comp}/pool_psychometric.json', 'r') as f_psy:
    pool_psy = json.load(f_psy)
psy_bin = pool_psy['binary']['parameter']
psy_con = pool_psy['continuous']['parameter']
f_psy.close()


#%%
# Constant
RHO = np.round(np.linspace(0.9, 1.09, 20), 2)
RHO_SIZE = 20
GRID_SIZE_bin = psy_bin['shape']
GRID_SIZE_con = psy_con['shape']


# Current
P_bin = np.zeros((RHO_SIZE, *GRID_SIZE_bin)) # [diff][grid]
L_bin = np.zeros(GRID_SIZE_bin) # [grid]
H_bin = 0

P_con = np.zeros((RHO_SIZE, *GRID_SIZE_con)) # [diff][grid]
L_con = np.zeros(GRID_SIZE_con) # [grid]
H_con = 0


# Expected
ExP_bin = np.zeros(RHO_SIZE) # [diff]
ExL_bin = np.zeros((2, RHO_SIZE, *GRID_SIZE_bin)) # [win/lose][diff][grid]
ExH_bin = np.zeros((2, RHO_SIZE, *GRID_SIZE_bin)) # [win/lose][diff]
EIG_bin = np.zeros(RHO_SIZE) # [diff]

ExP_con = np.zeros(RHO_SIZE) # [diff]
ExL_con = np.zeros((2, RHO_SIZE, *GRID_SIZE_con)) # [win/lose][diff][grid]
ExH_con = np.zeros((2, RHO_SIZE, *GRID_SIZE_con)) # [win/lose][diff]
EIG_con = np.zeros(RHO_SIZE) # [diff]


#%% Initialize
# init Param
theta_bin = []
theta_con = []
for i in range(4):
    theta_bin.append(np.round(np.linspace(psy_bin['min'][i], psy_bin['max'][i]-psy_bin['step'][i], psy_bin['shape'][i]), 3))
    theta_con.append(np.round(np.linspace(psy_con['min'][i], psy_con['max'][i]-psy_con['step'][i], psy_con['shape'][i]), 3))
G_bin = np.meshgrid(RHO, *theta_bin, indexing='ij')
G_con = np.meshgrid(RHO, *theta_con, indexing='ij')
theta_idx_bin = np.moveaxis(np.array(np.meshgrid(*[np.arange(psy_bin['shape'][i]) for i in range(4)], indexing='ij')), 0, -1)
theta_idx_con = np.moveaxis(np.array(np.meshgrid(*[np.arange(psy_con['shape'][i]) for i in range(4)], indexing='ij')), 0, -1)

# theta_bin = np.array(list(itertools.product(*theta_bin)))
# theta_con = np.array(list(itertools.product(*theta_con)))
# theta_idx_bin = np.array(list(itertools.product(*[np.arange(psy_bin['shape'][i]) for i in range(4)])))
# theta_idx_con = np.array(list(itertools.product(*[np.arange(psy_con['shape'][i]) for i in range(4)])))


# init Prior
idx_bin_prior = np.array(psy_bin['prior'])
idx_con_prior = np.array(psy_con['prior'])

L_bin = np.log(0.5+0.5*np.exp(-np.sum((theta_idx_bin - idx_bin_prior)**2, axis=-1)/4.0))
L_con = np.log(0.5+0.5*np.exp(-np.sum((theta_idx_con - idx_con_prior)**2, axis=-1)/4.0))
L_bin = norm_L(L_bin)
L_con = norm_L(L_con)


# init P
vec_PSI = np.vectorize(cal_PSI)
P_bin = vec_PSI(*G_bin)
vec_Polyexp_PSI = np.vectorize(cal_Polyexp_PSI)
P_con = vec_Polyexp_PSI(*G_con)

# for k, theta in enumerate(theta_bin):
#     for r, rho in enumerate(RHO):
#         P_bin[r][k] = cal_PSI(rho, *theta)
# for k, theta in enumerate(theta_con):
#     for r, rho in enumerate(RHO):
#         P_con[r][k] = cal_Polyexp_PSI(rho, *theta)


# # init X coef
# X_COEF = np.zeros((RHO_SIZE, GRID_CON)) # [diff][grid]
# for r, rho in enumerate(RHO):
#     if (TASK == 0):
#         rho_hat = 1/rho
#     elif (TASK == 1):
#         rho_hat = rho
#     for k, theta in enumerate(theta_con):
#         X_COEF[r][k] = cal_X_coef(rho_hat, *theta)
    
    
#%%
def cal_ExP(P, L):
    ExP = np.sum(P * np.exp(L), axis=1)
    return ExP


def cal_ExL(P, L, P_ex):
    P_ex = thres_P(P_ex)
    P = thres_P(P)
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


def update_conL(L, rho_curr, winlose):
    if (winlose == 1):
        wl = 0
    elif (winlose == 0):
        wl = 1
    if (TASK == 0 and winlose == 0) or (TASK == 1 and winlose == 1):
        L_update = copy.deepcopy(ExL_con[wl][rho_curr])
    else:
        L_update = L + reshape_index()