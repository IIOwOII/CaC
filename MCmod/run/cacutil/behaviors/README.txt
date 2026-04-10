
jjy01, cju01, khg01
{
  "method": ["binary", "continuous"],
  "binary": {
    "function": ["logistic", "weibull"],
    "parameter": {
      "name": ["m", "w", "gamma", "lambda"],
      "min": [0.9, 0.005, 0.0, 0.0],
      "max": [1.1, 0.205, 0.1, 0.1],
      "step": [0.005, 0.005, 0.01, 0.01],
      "shape": [40, 40, 10, 10],
      "prior": [20, 20, 5, 5]
    }
  },
  "continuous": {
    "function": ["polyexp"],
    "parameter": {
      "name": ["k", "m", "h", "w"],
      "min": [1, 0.05, 0.90, 0.005],
      "max": [21, 0.15, 1.10, 0.205],
      "step": [1, 0.01, 0.01, 0.005],
      "shape": [20, 10, 20, 40],
      "prior": [10, 5, 10, 20]
    }
  }
}
