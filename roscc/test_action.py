import requests

r = requests.get("http://127.0.0.1:8080/status", params="id=0&pos")
data = r.json()

x = int(data['pos']['x']) 
y = int(data['pos']['y']) + 1
z = int(data['pos']['z'])
print(x,y,z)
r = requests.get("http://127.0.0.1:8080/action", params=f"id=0&tp={x},{y},{z}")
print(f"Path: {r.request.path_url}")
print(f"Res: {r.text}")
