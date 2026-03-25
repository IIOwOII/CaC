import io
import numpy as np
import matplotlib.pyplot as plt

dir_main = '../MCmod/run/cacutil/replays/replay.meow'

with open(dir_main, 'rb') as f:
    reader = io.BufferedReader(f)
    dat = reader.read()

point_time = 3.4 # [sec]



FPS = 24
# FRAMESIZE = 1080*1920*3
FRAMESIZE = 480*854*3
frame_num = int(point_time*FPS)*FRAMESIZE

img = np.frombuffer(dat[frame_num:frame_num+FRAMESIZE], dtype=np.uint8)
# img = img.reshape(1080,1920,3)
img = img.reshape(480,854,3)

fig, ax = plt.subplots(dpi=300)
ax.imshow(img)