import numpy as np
import matplotlib.pyplot as plt

def func_normal(x, mu, sigma):
    return (1 / (sigma * np.sqrt(2 * np.pi))) * np.exp(-0.5 * ((x - mu) / sigma)**2)

def sample_pdf(x, f, size=100, window=1):
    S = np.random.choice(x, size=size, p=f/sum(f))
    S_num = np.zeros(x.size,)
    for idx, sx in enumerate(x):
        S_num[idx] = S[(S>sx-window/2)*(S<sx+window/2)].size
    return S_num/sum(S_num)

X = np.linspace(-3, 3, 100)
Y = func_normal(X, 0, 1)

S = sample_pdf(X, Y)

plt.plot(X, Y/sum(Y))
plt.plot(X, S)

plt.show()