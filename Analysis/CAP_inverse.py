import math
import numpy as np

M = 100
T = 30

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

def cal_PSI_inverse_t(P, rho, theta):
    k, m, h, w = theta
    num_iter = 10
    X = k
    for j in range(num_iter):
        series = 0
        for i in range(k):
            series += (X**i)/math.gamma(i+1)
        series -= math.exp(X) * (1-P)
        X = X + (math.gamma(k)/(X**(k-1))) * series
    if (TASK == 0):
        rho_hat = 1/rho
    elif (TASK == 1):
        rho_hat = rho
    if (rho_hat >= (h-w) + w/(M-m)):
        t = (X/k) * (m+(1/(1+((rho_hat-h)/w)))) * T
    else:
        t = (X/k) * M*T
        print('time become infinite! Adjusted by maximum mean time.')
    t = np.round(t, 4)
    print(f'Percentile: {P}, Difficulty: {rho} -> Until Time: {t}')
    return t

def cal_Percentile(rho, t, theta):
    k, m, h, w = theta
    if (TASK == 0):
        rho_hat = 1/rho
    elif (TASK == 1):
        rho_hat = rho
    x = (k*t/T)*(1.0/(m+(1.0/(1+((rho_hat-h)/w)))))
    series = 0
    for i in range(k):
        series += (x**i)/math.gamma(i+1)
    P = 1 - series * np.exp(-x)
    P = np.round(P, 4)
    print(f'Difficulty: {rho}, Time: {t} -> Percentile: {P}')
    return P

def cal_Percentile_Mu(rho, theta):
    k, m, h, w = theta
    if (TASK == 0):
        rho_hat = 1/rho
    elif (TASK == 1):
        rho_hat = rho
    series = 0
    for i in range(k):
        series += (k**i)/math.gamma(i+1)
    P = 1 - series * np.exp(-k)
    P = np.round(P, 4)
    Mu = (m+(1.0/(1+((rho_hat-h)/w)))) * T
    print(f'Difficulty: {rho}, Mean Time: {Mu} -> Percentile: {P}')
    return P


theta_star = [10, 0.1, 1.00, 0.15]
TASK = 0
p = 0.5
r = 0.9957


rho = cal_PSI_inverse(p, theta_star)
P = cal_PSI(rho, theta_star)

t = cal_PSI_inverse_t(p, r, theta_star)
per = cal_Percentile(r, 421/20, theta_star)
per_mu = cal_Percentile_Mu(r, theta_star)