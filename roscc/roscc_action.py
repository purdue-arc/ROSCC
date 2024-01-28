import rclpy
from rclpy.node import Node
import requests

from roscc_msg.msg import Action


class MinimalSubscriber(Node):

    def __init__(self):
        super().__init__('ros_to_roscc')
        self.subscription = self.create_subscription(
            Action,
            'roscc_action',
            self.listener_callback,
            10)
        self.subscription  # prevent unused variable warning
        self.server_address = "10.9.0.8:8080" # You will probably want to change this
        self.get_logger().info(f"Starting action node")

    def listener_callback(self, msg):
        r = requests.get(f"http://{self.server_address}/action", params=f"id={msg.id}&{msg.params}")
        if r.status_code != 200:
            self.get_logger().warn(f'bad request. status code {r.status_code} {r.text}')



def main(args=None):
    rclpy.init(args=args)

    minimal_subscriber = MinimalSubscriber()

    rclpy.spin(minimal_subscriber)

    # Destroy the node explicitly
    # (optional - otherwise it will be done automatically
    # when the garbage collector destroys the node object)
    minimal_subscriber.destroy_node()
    rclpy.shutdown()


if __name__ == '__main__':
    main()
