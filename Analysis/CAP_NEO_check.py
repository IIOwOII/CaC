import numpy as np


C_PRE = 1
T= 30

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

GRID_NEO = 150000
R_NEO = np.linspace(-0.18, 0.7, 45)
NEO_K = np.linspace(0.5, 20, 40)
NEO_M = np.linspace(0.1, 0.3, 5)
NEO_H = np.linspace(0.6, 1.08, 25)
NEO_W = np.linspace(1.1, 4, 30)

THETA = np.meshgrid(NEO_K, NEO_M, NEO_H, NEO_W, indexing='ij')
K = THETA[0].flatten()
M = THETA[1].flatten()
H = THETA[2].flatten()
W = THETA[3].flatten()

P_neo = np.zeros((45, GRID_NEO))
NEO_LAM = np.zeros((45, GRID_NEO))
for i in range(GRID_NEO):
    for j in range(45):
        NEO_LAM[j,i] = calGammaLambda(R_NEO[j], K[i], M[i], H[i], W[i])

for i in range(GRID_NEO):
    for j in range(45):
        P_neo[j,i] = calGammaCDF(K[i], NEO_LAM[j,i])