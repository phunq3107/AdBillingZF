import requests
from matplotlib import pyplot as plt
import numpy as np

port = 8102
url = f'http://localhost:{port}/api/v1/adbilling/statistical'
data = requests.get(url).json()
i = 1
plt.figure(figsize=(13,4))
for key in data:
    plt.subplot(1, 3, i)
    i += 1
    plt.title(key)
    print(key)
    print("Avg:", np.average(data[key]))
    plt.plot(data[key])

plt.show()
