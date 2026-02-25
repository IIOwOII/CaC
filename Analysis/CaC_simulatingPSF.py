#%% Library
import numpy as np
import matplotlib.pyplot as plt
import json
import itertools

#%% Functions
def func_sigmoid(x, m, w, gam, lam):
    return gam + (1-lam-gam)/(1+19**(2*(x-m)/w))


#%% Param load
dir_comp = '../MCmod/run/cacutil/components'

with open(f'{dir_comp}/pool_psychometric.json', 'r') as f_psy:
    pool_psy = json.load(f_psy)
psy_bin = pool_psy['binary']['parameter']
psy_con = pool_psy['continuous']['parameter']

theta_bin = []
theta_con = []
for i in range(4):
    theta_bin.append(np.linspace(psy_bin['min'][i], psy_bin['max'][i]-psy_bin['step'][i], psy_bin['shape'][i]))
    theta_con.append(np.linspace(psy_con['min'][i], psy_con['max'][i]-psy_con['step'][i], psy_con['shape'][i]))


#%%
def rs(sgrid, idx_param):
    a = 20
    b = 20
    c = 5
    d = 5
    if (idx_param==3):
        idx = sgrid % d
    elif (idx_param==2):
        idx = (sgrid // d) % c
    elif (idx_param==1):
        idx = ((sgrid // d) // c) % b
    elif (idx_param==0):
        idx = (((sgrid // d) // c)) // b
    return idx


#%%


itertools.product(theta_bin)