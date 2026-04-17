import math

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
        print(rho)
        
TASK = 0
theta_star = [10, 0.1, 0.95, 0.16]
cal_PSI_inverse(0.95, theta_star)