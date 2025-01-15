import numpy as np
import matplotlib.pyplot as plt

A = np.random.randint(-1,2,15)
plt.plot([-1,0,1],[i for i in map(sum, [A==-1,A==0,A==1])])
plt.ylim(0,15)