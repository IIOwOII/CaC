import math
import numpy as np

M = 100

def cal_PSI(rho, theta):
    k, m, h, w = theta
    if (TASK == 0):
        rho_hat = 1/rho
    elif (TASK == 1):
        rho_hat = rho
    if (rho_hat >= (h-w) + w/(M-m)):
        X = k/(m+(1/(1+((rho_hat-h)/w))))
    else:
        X = k/M
        rho_hat = (h-w) + w/(M-m)
        if (TASK == 0):
            rho = 1/rho_hat
        elif (TASK == 1):
            rho = rho_hat
        print(f'rho out of range! adjusted rho: {rho}')
    series = 0
    for i in range(k):
        series += (X**i)/math.gamma(i+1)
    P_hat = math.exp(-X)*series
    if (TASK == 0):
        P = 1-P_hat
    elif (TASK == 1):
        P = P_hat
    P = np.round(P, 4)
    print(f'Difficulty: {rho} -> Win rate: {P}')
    return P

def cal_PSI_inverse(P, theta):
    k, m, h, w = theta
    if (TASK == 0):
        P_hat = 1-P
    elif (TASK == 1):
        P_hat = P
    num_iter = 10
    X = k
    for j in range(num_iter):
        series = 0
        for i in range(k):
            series += (X**i)/math.gamma(i+1)
        series -= math.exp(X) * P_hat
        X = X + (math.gamma(k)/(X**(k-1))) * series
    rho_hat = h+w*((1/((k/X)-m))-1)
    if (TASK == 0):
        rho = 1/rho_hat
    elif (TASK == 1):
        rho = rho_hat
    if (rho > 1.3):
        print(f'max rho! origin: {rho}')
        rho = 1.3
    elif (rho < 0.7):
        print(f'min rho! origin: {rho}')
        rho = 0.7
    rho = np.round(rho, 4)
    print(f'Win rate: {P} -> Difficulty: {rho}')
    return rho
        
TASK = 1
p = 0.25
theta_star = [10, 0.1, 1.05, 0.16]

rho = cal_PSI_inverse(p, theta_star)
P = cal_PSI(rho, theta_star)
