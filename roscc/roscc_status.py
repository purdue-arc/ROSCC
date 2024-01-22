import requests
import rclpy
from rclpy.node import Node
from roscc_msg.msg import TurtleStatus, Entity, Item, Block

class MinimalPublisher(Node):

  def __init__(self):
    super().__init__('roscc_to_ros')
    self.publisher_ = self.create_publisher(TurtleStatus, 'roscc_status', 10)
    timer_period = 0.5  # seconds
    self.timer = self.create_timer(timer_period, self.timer_callback)
    self.declare_parameter('id', 255)
    self.id = self.get_parameter('id').value
    self.get_logger().info(f"ID {self.id}")

  def timer_callback(self):
    r = requests.get("http://10.9.0.8:8080/status", params=f"id={self.id}&inv&pos&blocknbt&blocks=1&itemnbt&entities=5&entitynbt")
    self.get_logger().info(str(r.status_code))
    data = r.json()

    status = TurtleStatus()

    status.id = self.id

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
    self.publisher_.publish(status)
    # self.get_logger().info('Publishing')


def main(args=None):
  rclpy.init(args=args)

  minimal_publisher = MinimalPublisher()

  rclpy.spin(minimal_publisher)

  # Destroy the node explicitly
  # (optional - otherwise it will be done automatically
  # when the garbage collector destroys the node object)
  minimal_publisher.destroy_node()
  rclpy.shutdown()
