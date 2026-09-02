#%% Library
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from scipy.signal import hilbert

import mmap
import json

from BCI2kReader import BCI2kReader as b2k


#%% info
subj = 'P012'
task = ['chasing', 'main']

dir_bci = f'../../CaC_SEEG/{subj}/{subj}_{task[0]}_{task[1]}.dat'
if (task[1]=='fitting'):
    name_beh = f'fitting_{task[0]}'
else:
    name_beh = task[0]
dir_beh = f'../../CaC_Data/{subj}/{name_beh}'


#%%
with open(f'{dir_beh}/../info_timestamp.json', 'r') as f:
    info_timestamp = json.load(f)['cac']
ses_start = info_timestamp[f'{name_beh}_start'].split(' ')[3] # hh:mm:ss

with open(f'{dir_beh}/log_event.json', 'r') as f:
    log_ev = json.load(f)['cac']
ev_time = np.array(log_ev['absolute'])/1000
ev_content = np.array(log_ev['content'])

with b2k.BCI2kReader(dir_bci) as f:
    bci_sig = f.signals
    bci_state = f.states
    bci_param = f.parameters

FPS = bci_param['SamplingRate']
bci_start = bci_param['StorageTime'].split('T')[1] # hh:mm:ss
bci_start_sec = (int(ses_start.split(':')[2]) - int(bci_start.split(':')[2]))*FPS

#%%
ch_trig = 261
bci_trig_raw = bci_sig[ch_trig]
bci_trig = np.round((bci_trig_raw-min(bci_trig_raw))/(max(bci_trig_raw)-min(bci_trig_raw)))


#%%

# (.[0]: 10 tick rest, ㅡ[1]: 10 tick toggle, =[2]: 10 tick flicker)
# count down(start): ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ..==
# preparation [10]: ㅡ.ㅡ
# gameplay [20]: ㅡ.=
# gameplay end [25]: ㅡ.ㅡ
# survey blank [33]: =
# survey wait [35]: ㅡ
# *4
# surrender blank [39]: =
# surrender wait: 
# interval [40]: =
# (end): ==..ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

pat_start = [1,1,1,1,1,1,1,1,1,1,0,0,2,2]
pat_preparation = [1,0,1]
pat_gameplay = [1,0,2]
pat_gameplay_end = [1,0,1]
pat_survey_blank = [2]
pat_survey_wait = [1]
pat_surrender_blank = [2]
pat_interval = [2]
pat_end = [2,2,0,0,1,1,1,1,1,1,1,1,1,1]




#%% Hilbert Transform
bci_sig_mask = ~np.isnan(bci_sig)
bci_sig_mask_index = np.where(bci_sig_mask[0])[0]
bci_sig_clean = np.interp()


hil_sig = hilbert(bci_sig[:, bci_sig_mask])
#hil_A = np.abs(hil_sig)  # 포락선(진폭)
#hil_phase = np.angle(hil_sig)  # 순간 위상