#%% Library
import numpy as np
import matplotlib.pyplot as plt

import json
import itertools
import math
import copy

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


def cal_PSI(rho, theta):
    # theta = [m, w, gamma, lambda]
    rho = np.repeat(rho.reshape(-1,1), theta.shape[0], axis=-1)
    m = theta[:,0]
    w = theta[:,1]
    gam = theta[:,2]
    lam = theta[:,3]
    
    P = gam + (1-gam-lam)/(1+(9**((rho-m)/w)))
    P[P<P_MIN] = P_MIN
    P[P>P_MAX] = P_MAX
    return P


def cal_Polyexp_PSI(rho_hat, theta):
    # theta = [k, m, h, w]
    rho_hat = np.repeat(rho_hat.reshape(-1,1), theta.shape[0], axis=-1)
    k = theta[:,0].astype(int)
    
    # 1 - e^(-X) * (X^0/0! + X^1/1! + ... + X^(k-1)/(k-1)!)
    X_T = X_COEF * T
    k_max = max(k)
    K_arange = np.tile(np.arange(k_max), reps=[theta.shape[0], 1]).T
    K_mask = K_arange < k
    vec_fact = np.vectorize(math.factorial)
    K_fact = 1/vec_fact(K_arange)
    SERIES = np.sum((X_T**K_arange)*(K_fact*K_mask), axis=0)

    P_hit = 1 - np.exp(-X_T)*SERIES
    P_hit[P_hit<P_MIN] = P_MIN
    P_hit[P_hit>P_MAX] = P_MAX
    
    if (TASK == 0):
        PSI = P_hit
    elif (TASK == 1):
        PSI = 1-P_hit
    return PSI


def cal_Mu(rho_hat, theta):
    # theta = [k, m, h, w]
    rho_hat = np.repeat(rho_hat.reshape(-1,1), theta.shape[0], axis=-1)
    m = theta[:,1]
    h = theta[:,2]
    w = theta[:,3]
    
    mu = np.where(rho_hat >= (h-w)+w/(10.0-m), T*(m+(1.0/(1+((rho_hat-h)/w)))), 10.0*T)
    return mu


def cal_X_coef(theta):
    # theta = [k, m, h, w]
    # X = k*t/mu
    # coef : k/mu
    k = theta[:,0]
    x_coef = k/MU
    return x_coef


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
if (TASK == 0):
    RHO_HAT = 1.0/RHO
elif (TASK == 1):
    RHO_HAT = RHO
RHO_SIZE = 20
GRID_BIN = np.product(psy_bin['shape']) # flatten
GRID_CON = np.product(psy_con['shape']) # flatten


# Current
P_bin = np.zeros((GRID_BIN, RHO_SIZE)) # [diff][grid]
L_bin = np.zeros(GRID_BIN) # [grid]
H_bin = 0

P_con = np.zeros((GRID_CON, RHO_SIZE)) # [diff][grid]
L_con = np.zeros(GRID_CON) # [grid]
H_con = 0


# Expected
ExP_bin = np.zeros(RHO_SIZE) # [diff]
ExL_bin = np.zeros((2, RHO_SIZE, GRID_BIN)) # [win/lose][diff][grid]
ExH_bin = np.zeros((2, RHO_SIZE, GRID_BIN)) # [win/lose][diff]
EIG_bin = np.zeros(RHO_SIZE) # [diff]

ExP_con = np.zeros(RHO_SIZE) # [diff]
ExL_con = np.zeros((2, RHO_SIZE, GRID_CON)) # [win/lose][diff][grid]
ExH_con = np.zeros((2, RHO_SIZE, GRID_CON)) # [win/lose][diff]
EIG_con = np.zeros(RHO_SIZE) # [diff]


#%% Initialize
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

L_bin = np.log(0.5+0.5*np.exp(-np.sum((theta_bin_idx - theta_bin_prior)**2, axis=-1)/4.0))
L_con = np.log(0.5+0.5*np.exp(-np.sum((theta_con_idx - theta_con_prior)**2, axis=-1)/4.0))
L_bin = norm_L(L_bin)
L_con = norm_L(L_con)


# init P, X coef
MU = cal_Mu(RHO_HAT, theta_con)
X_COEF = cal_X_coef(theta_con)
P_bin = cal_PSI(RHO, theta_bin)
P_con = cal_Polyexp_PSI(RHO_HAT, theta_con)



    
#%%
def cal_ExP(P, L):
    ExP = np.sum(P * np.exp(L), axis=1)
    return ExP


def cal_ExL(P, L, P_ex):
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