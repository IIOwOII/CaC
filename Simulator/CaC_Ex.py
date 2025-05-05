import numpy as np
import matplotlib.pyplot as plt
from typing import Union

class test:
    def __init__(self):
        print('hi')
    
    def __add__(self, other):
        if type(other)==test:
            print('1')
        elif type(other)==Union[int,float]:
            print('2')
            
