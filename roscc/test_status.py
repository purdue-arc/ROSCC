import requests
import pprint

query_params = {'id': '0', 'blocks': '1', 'entities': '2', 'inv': None, 'pos': None, 'blocknbt': None, 'itemnbt': None, 'entitynbt': None}
query_params_str = '&'.join([key if value == None else f'{key}={value}' for key, value in query_params.items()])
r = requests.get("http://127.0.0.1:8080/status", params=query_params_str)
data = r.json()
pprint.pp(data)