import numpy as np
import matplotlib.pyplot as plt

# Color Map (MANIM)
COLOR_RED_C = '#FC6255'
COLOR_BLUE_C = '#58C4DD'
COLOR_GREEN_C = '#83C167'
COLOR_YELLOW_C = '#F7D96F'
COLOR_PURPLE_C = '#9A72AC'
COLOR_GOLD_C = '#F0AC5F'

dp_win = -0.04
dp_lose = 0.01

N = 20

p = 0.7
dp = 0

p_M = 0.8
p_m = 0.2

p_equ = 0.4
k_E = 0.6

epochs = 100
np.random.seed(42)

WL = []
DP = []
P = []
for i in range(epochs):
    WL_temp = []
    DP_temp = []
    P_temp = []
    print('----------')
    print(f'epoch: {i}')
    # trial 0
    p = 0.7
    dp = 0
    wl = np.random.choice([1, 0], 1, replace=True, p=[p, 1-p])[0]
    P_temp.append(p)
    DP_temp.append(dp)
    WL_temp.append(wl)
    print(f'p_0= {p}, dp_0= {dp}, wl_0= {wl}')
    for trial in range(1, N): # trial 1 ~
        p = P_temp[-1]
        dp = DP_temp[-1]
        wl = WL_temp[-1]
        # equ force apply
        x = p - p_equ
        F_weight = (trial+1)/N
        F_E = -k_E*x
        dp += (F_E*F_weight)
        if (wl==1):
            dp += dp_win
        elif (wl==0):
            dp += dp_lose
        dp = np.round(dp, 2)
        if (dp > 0.1):
            dp = 0.1
        elif (dp < -0.1):
            dp = -0.1
        p += dp
        p = np.round(p, 2)
        if (p > p_M):
            p = p_M
        elif (p < p_m):
            p = p_m
        P_temp.append(p)
        DP_temp.append(dp)
        wl = np.random.choice([1, 0], 1, replace=True, p=[p, 1-p])[0]
        WL_temp.append(wl)
        print(f'p_{trial}= {p}, dp_{trial}= {dp}, wl_{trial}= {wl}')
    WL.append(WL_temp.copy())
    DP.append(DP_temp.copy())
    P.append(P_temp.copy())

WL = np.array(WL)
DP = np.array(DP)
P = np.array(P)

fig, ax = plt.subplots(nrows=2, figsize=(6,6), dpi=300)
hist_P = ax[0].hist(P.flatten(), bins=12, color=COLOR_BLUE_C)
ax[0].set_xlim([0.15, 0.85])
ax[0].set_xlabel('P (%)')
ax[0].set_xticks([0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8])
ax[0].axvline(0.7, linewidth=1, linestyle='--', color='k')
ax[0].axvline(p_m, linewidth=1, linestyle='--', color=COLOR_RED_C)
ax[0].axvline(p_M, linewidth=1, linestyle='--', color=COLOR_RED_C)
ax[0].axvline(p_equ, linewidth=1, linestyle='-.', color=COLOR_PURPLE_C)

hist_DP = ax[1].hist(DP.flatten(), color=COLOR_PURPLE_C)
ax[1].set_xlabel(r'$\Delta$P (%)')
ax[1].set_xlim([-0.11, 0.11])
ax[1].set_xticks([-0.1, 0, 0.1])
ax[1].axvline(-0.1, linewidth=1, linestyle='--', color=COLOR_RED_C)
ax[1].axvline(0.1, linewidth=1, linestyle='--', color=COLOR_RED_C)
ax[1].axvline(0, linewidth=1, linestyle='--', color='k')

ax[0].set_title(f'{N} trials x {epochs} epochs')
for side in ['right', 'top']:
    ax[0].spines[side].set_visible(False)
    ax[1].spines[side].set_visible(False)

fig2, ax2 = plt.subplots(figsize=(4,3), dpi=300)
ax2.errorbar(np.arange(N), np.mean(P, axis=0), yerr=np.std(P, axis=0), 
             c=COLOR_BLUE_C, marker='o', markersize=4, label='Intended P')
ax2.plot(np.arange(N), np.mean(WL, axis=0), 
         c=COLOR_PURPLE_C, marker='o', markersize=4, label='Actual mean P')
ax2.set_xlim([-1,N])
ax2.set_xticks(np.arange(0, N+1, 5))
ax2.set_ylim([-0.02,1.02])
ax2.set_yticks(np.arange(0, 1.01, 0.2))
ax2.axhline(0.7, linewidth=0.5, linestyle='--', color='k', alpha=0.8)
ax2.axhline(p_M, linewidth=0.5, linestyle='--', color=COLOR_RED_C)
ax2.axhline(p_m, linewidth=0.5, linestyle='--', color=COLOR_RED_C)
ax2.set_xlabel('Trial')
ax2.set_ylabel('P (%)')
for side in ['right', 'top']:
    ax2.spines[side].set_visible(False)
ax2.set_title(f'{N} trials x {epochs} epochs')
ax2.legend()

plt.tight_layout()
plt.show()