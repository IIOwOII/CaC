import numpy as np

def cal_Erfcsqrt(x):
    y = 1 - np.sqrt(1-np.exp(-x*((1.27324+0.147*x)/(1+0.147*x))))
    return y

def cal_factorial(m):
    y = np.prod(np.arange(1,m+1), dtype=np.int64)
    return y

def cal_loggamma(s):
    y = 0
    s_isint = np.round(2*s)%2
    if (s_isint == 0):
        if (s == 1):
            y = 0
        else:
            for i in range(1, s):
                y += np.log(i)
    elif (s_isint == 1):
        if (s == 0.5):
            y = 0.5 * np.log(np.pi)
        else:
            series = 0
            for i in range(1, int(s+0.5)):
                series += np.log(2*i-1)
            y = 0.5 * np.log(2*np.pi) - s * np.log(2) + series
    return y

def cal_Gamma_LambdaMedian(k):
    lambda_median = (k - 1.0/3.0)/T
    if (k<1): lambda_median = ((2**(-1/k))*0.56147*(2.27611**k))/T
    return lambda_median

def cal_Gamma_Lambda(r, k, m, h, w):
    rho_hat = np.exp(r*C_PRE)
    h_hat = h**(-C_PRE)
    w_hat = np.exp(-w)
    Lambda = (k/T)*((m+((k/(cal_Gamma_LambdaMedian(k)*T))-m)*np.exp(-((rho_hat-h_hat)/w_hat)))**(-1))
    return Lambda

def cal_Gamma_CDF(k, Lambda):
    P = 0
    P_nhit = 0
    k_isint = np.round(2*k)%2
    if (k_isint == 0):
        series = 0
        for i in range(k):
            series += ((Lambda*T)**i)/cal_factorial(i)
        P_nhit = np.exp(-Lambda*T)*series
    elif (k_isint == 1):
        if (k == 0.5):
            P_nhit = cal_Erfcsqrt(Lambda*T)
        else:
            a = 1
            series = 0
            for i in range(int(k-0.5)):
                if (i != 0):
                    a = a * (2.0/(2.0*i+1.0))
                series += a*((Lambda*T)**i)
            P_nhit = cal_Erfcsqrt(Lambda*T) + 2*np.exp(-Lambda*T)*np.sqrt(Lambda*T/np.pi)*series
    P = ((1.0+C_PRE)/2.0)-C_PRE*P_nhit
    if (P < P_min): P = P_min
    elif (P > P_max): P = P_max
    return P

def cal_Gamma_PDF(k, Lambda, t):
    if (t <= 0): return 0.0
    p = ((Lambda*t)**k) * np.exp(-Lambda*t) / (t*np.exp(cal_loggamma(k)))
    return p


def cal_PSI_inverse(P, theta):
    k, m, h, w = theta
    epsilon = 0.0001
    r_m = -0.3
    r_p = 0.7
    num_loop = 0
    print('--------------------')
    while True:
        num_loop += 1
        r_c = (r_m + r_p)/2.0
        Lambda = cal_Gamma_Lambda(r_c, k, m, h, w)
        PSI = cal_Gamma_CDF(k, Lambda)
        print(f'Iteration {num_loop}: residual = {PSI-P}')
        if (abs(PSI-P) <= epsilon):
            break
        elif (PSI < P):
            r_m = r_c
        elif (PSI > P):
            r_p = r_c
        if (num_loop > 100):
            print('weird! The loop is over than 100!')
            break
    rho = r_to_rho(r_c)
    print('----- Final -----')
    print(f'Total iteration: {num_loop}')
    print(f'Residual: {PSI-P}')
    print(f'r: {r_c}')
    print(f'rho: {rho}')
    print('--------------------')
    return rho
    
def r_to_rho(r):
    rho = np.round(np.exp(-r), 4)
    return rho

T = 30
C_PRE = -1
P_min = 1.0E-12
P_max = 1 - 1.0E-12

theta_star = [10, 0.2, 0.8, 2]
p_target = 0.1

r_target = cal_PSI_inverse(p_target, theta_star)