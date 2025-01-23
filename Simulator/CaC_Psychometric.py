import numpy as np
import math as m
import matplotlib.pyplot as plt

def f_sig(x,a,b):
    val = (1+m.exp(-b*(x-a)))**(-1)
    return val

def g_nu(p):
    if (p > np.random.rand(1)):
        val = 1
    else:
        val = 0
    return val

alpha = 0.52
beta = -11.4

A = np.arange(0.2,1,0.01)
B = np.arange(-15,-5,0.1)

L = np.zeros((A.shape[0], B.shape[0]))
N = 100
S = []
NU = []

for n in range(N):
    if (n == 0):
        s = 0.4
    else:
        s_idx = np.where(abs(L)==np.min(abs(L)))
        s += (A[s_idx[0][0]] - s)/300
    nu = g_nu(f_sig(s,alpha,beta))
    for i,a in enumerate(A):
        for j,b in enumerate(B):
            L[i,j] += nu - np.log(1+m.exp(-b*(s-a)))
    S.append(s)
    NU.append(nu)
            
plt.imshow(abs(L))