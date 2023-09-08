import requests
import rospy
from rktl_msgs.msg import TurtleStatus, Entity, Item, Block

def publish():
  publisher = rospy.Publisher('roscc.status', TurtleStatus, queue_size=10)
  rospy.init_node('roscc_to_ros', anonymous=True)
  rate = rospy.Rate(10)
  while not rospy.is_shutdown():
    r = requests.get("http://127.0.0.1:8080/status", params="id=0&inv&pos&blocknbt&blocks=1&itemnbt&entities=5&entitynbt")
    data = r.json()

    status = TurtleStatus()

    status.id = 0

    status.x = data['pos']['x']
    status.y = data['pos']['y']
    status.z = data['pos']['z']

    items = list()
    for i in range(0,16):
      item = Item()
      item.count = data['inv'][i]['count']
      item.id = data['inv'][i]['id']
      item.nbt = data['inv'][i].get('nbt', '')
      items.append(item)
    status.inv = items

    blocks = list()
    for blk in data['blocks']:
      block = Block()
      block.id = blk['id']
      block.nbt = blk.get('nbt', '')
      block.x = blk['x']
      block.y = blk['y']
      block.z = blk['z']
      blocks.append(block)
    status.blocks = blocks

    entities = list()
    for ent in data['entities']:
      entity = Entity()
      entity.type = ent['type']
      entity.nbt = ent.get('nbt', '')
      entity.x = ent['x']
      entity.y = ent['y']
      entity.z = ent['z']
      entities.append(entity)
    status.entities = entities
    
    publisher.publish(status)
    rate.sleep()

if __name__ == '__main__':
  try:
    publish()
  except rospy.ROSInterruptException:
    pass
