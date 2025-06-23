#%% Library
import numpy as np
import matplotlib.pyplot as plt


#%% Functions
def func_sigmoid(x, m, w, gam, lam):
    return gam + (1-lam-gam)/(1+19**(2*(x-m)/w))


#%% Variable
M = np.linspace(0.9, 1.1, 11)
W = np.linspace(0.2, 0.4, 6)
GAM = np.linspace(0, 0.1, 6)
LAM = np.linspace(0, 0.1, 6)


#%%
X = np.linspace(0.8, 1.2, 100)

# Optimal subject's function (rho-p)
PSI = func_sigmoid(X, 0.93, 0.27, 0.03, 0.06)


np.random.choice()


#%% Plot
fig, ax = plt.subplots(1, 1, figsize=(8,6), dpi=300)

ax.set_xlabel(r'$\rho$ (difficulty)')
ax.set_ylabel(r'$t$ (win rate)')
ax.set_xlim([0.79, 1.21])
ax.set_ylim([-0.05, 1.05])
ax.set_xticks(np.linspace(0.8, 1.2, 9))
ax.set_yticks(np.linspace(0.0, 1.0, 6))

ax.plot(X, PSI)

plt.show()