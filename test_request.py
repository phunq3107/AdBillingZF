import requests
import json
from concurrent.futures import ThreadPoolExecutor
import random

port_ls = [8100, 8101, 8102]
uid_ls = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
typ_ls = ['get', 'credit', 'debit']


def send_req(typ):
    # print(typ)
    if typ == 'get':
        url = f"http://localhost:{random.choice(port_ls)}/api/v1/adbilling/balance/{random.choice(uid_ls)}"
        payload = {}
        headers = {}
        response = requests.request("GET", url, headers=headers, data=payload)
        return response.status_code
    elif typ == 'credit':
        url = f"http://localhost:{random.choice(port_ls)}/api/v1/adbilling/credit"
        payload = json.dumps({
            "userId": random.choice(uid_ls),
            "amount": random.randint(1, 10)
        })
        headers = {
            'Content-Type': 'application/json'
        }

        response = requests.request("POST", url, headers=headers, data=payload)
        return response.status_code
    elif typ == 'debit':
        url = f"http://localhost:{random.choice(port_ls)}/api/v1/adbilling/debit"

        payload = json.dumps({
            "fromUser": random.choice(uid_ls),
            "toUser": random.choice(uid_ls),
            "amount": random.randint(1, 10)
        })
        headers = {
            'Content-Type': 'application/json'
        }

        response = requests.request("POST", url, headers=headers, data=payload)
        return response.status_code


if __name__ == '__main__':
    with ThreadPoolExecutor(5) as pool:
        random_type = typ_ls * 100
        random.shuffle(random_type)
        print(list(pool.map(send_req, random_type)))
