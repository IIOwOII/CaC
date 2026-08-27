#%% Library
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

import json
import math


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

# Param load
with open(f'{dir_comp}/pool_psychometric.json', 'r') as f_psy:
    pool_psy = json.load(f_psy)
psy_con = pool_psy['continuous']['parameter']
GRID_CON = np.prod(psy_con['shape']) # flatten


#%% fitting mu candidate
# param normalize to regression (0 to 1)
def normalize(x, _min, _max):
    return (x-_min)/(_max-_min)
def unnormalize(x_hat, _min, _max):
    return (_max-_min)*x_hat + _min

def mu_logit(rho, m, w, gam, lam):
    P = gam + (1-gam-lam)/(1+(9**((rho-m)/w)))
    P[P<P_MIN] = P_MIN
    P[P>P_MAX] = P_MAX
    return P

def mu_frac(rho_hat, m, h, w):
    theta = np.array([0, m, h, w])
    vec_unnorm = np.vectorize(unnormalize)
    _, m, h, w = vec_unnorm(theta, con_min, con_max)[:]
    
    mu_norm = np.where(rho_hat >= (h-w)+w/(M-m), (m+(1.0/(1+((rho_hat-h)/w)))), M)
    return mu_norm

def mu_exp(rho_hat, k, m, h, w):
    theta = np.array([k, m, h, w])
    vec_unnorm = np.vectorize(unnormalize)
    k, m, h, w = vec_unnorm(theta, con_min, con_max)[:]
    
    mu_norm = m+((3*k)/(3*k-1)-m)*np.exp(-(rho_hat-h)/w)
    return mu_norm

