#%%
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import LinearSegmentedColormap


#%% Figure
class meowfig:
    def __init__(self, nrows=1, ncols=1, figsize=(4,3), dpi=300, 
                 design=True, grid=False, **kwargs):
        """
        Parameters
        ----------
        nrows, ncols : int
            The default is 1.
        size : tuple
            The default is (4,3).
        dpi : int
            The default is 300.
        **kwargs : optional
            title, xlabel, ylabel, xlim, ylim, xticks, yticks, xticklabels, yticklabels...
        """
        # essential
        self.dpi = dpi
        self.design = design
        self.grid = grid
        self.nrows = nrows
        self.ncols = ncols
        self.figsize = (figsize[0]*ncols, figsize[1]*nrows)
        
        # optional
        self.title = kwargs.get('title')
        self.xlabel = kwargs.get('xlabel')
        self.ylabel = kwargs.get('ylabel')
        self.xlim = kwargs.get('xlim')
        self.ylim = kwargs.get('ylim')
        self.xticks = kwargs.get('xticks')
        self.yticks = kwargs.get('yticks')
        self.xticklabels = kwargs.get('xticklabels')
        self.yticklabels = kwargs.get('yticklabels')
        self.xoffset = kwargs.get('xoffset')
        self.yoffset = kwargs.get('yoffset')
        
        # figure setting
        self.fig, self.axes = plt.subplots(nrows=self.nrows, ncols=self.ncols, 
                                           figsize=self.figsize, dpi=self.dpi)
        if self.nrows==1 and self.ncols==1: 
            self.axes = np.array([self.axes], dtype=object)
        self.axes = self.axes.reshape(self.nrows, self.ncols)
        
        for rax in self.axes:
            for ax in rax:
                for side in ['right', 'top', 'bottom']: 
                    ax.spines[side].set_visible(False)
        self.optional_setting()
    
    # additional setting
    def optional_setting(self):
        self.axes[0,0].set_ylabel(self.ylabel)
        for rax in self.axes:
            for ax in rax:
                ax.set_title(self.title)
                ax.set_xlabel(self.xlabel)
                if (self.xlim != None):
                    xlim = self.xlim
                    xrange = xlim[1] - xlim[0]
                    ax.set_xlim([xlim[0]-0.02*xrange, xlim[1]+0.02*xrange])
                if (self.ylim != None):
                    ylim = self.ylim
                    yrange = ylim[1] - ylim[0]
                    ax.set_ylim([ylim[0]-0.02*yrange, ylim[1]+0.02*yrange])
                if (self.xticks != None): ax.set_xticks(self.xticks)
                if (self.yticks != None): ax.set_yticks(self.yticks)
                if (self.xticklabels != None): ax.set_xticklabels(self.xticklabels)
                if (self.yticklabels != None): ax.set_yticklabels(self.yticklabels)
                if (self.design): self.optional_design()
    
    # figure design
    def optional_design(self):
        for rax in self.axes:
            for ax in rax:
                if (self.yoffset != None):
                    ax.axhline(self.yoffset, linewidth=0.6, linestyle='-', color='gray', zorder=-1)
                if (self.yticks != None):
                    for ytick in self.yticks:
                        ax.axhline(ytick, linewidth=0.3, linestyle='-.', color='gray', alpha=0.3, zorder=-1)
                if (self.xticks != None) and (self.grid):
                    for xtick in self.xticks:
                        ax.axvline(xtick, linewidth=0.3, linestyle='-.', color='gray', alpha=0.3, zorder=-1)


#%% Color (MANIM)
COLOR_RED_C = '#FC6255'
COLOR_BLUE_C = '#58C4DD'
COLOR_GREEN_C = '#83C167'
COLOR_YELLOW_C = '#F7D96F'
COLOR_PURPLE_C = '#9A72AC'
COLOR_GOLD_C = '#F0AC5F'
COLOR_JERRY = LinearSegmentedColormap.from_list('jerry', ['#A46E24','#CA8628','#E9A547'])
COLOR_TOM = LinearSegmentedColormap.from_list('tom', ['#6D6C6D','#998999','#CAC4C4'])
    