import rclpy
from rclpy.node import Node
import requests

from rktl_msgs.msg import Action


class MinimalSubscriber(Node):

    def __init__(self):
        super().__init__('minimal_subscriber')
        self.subscription = self.create_subscription(
            Action,
            'ros_to_roscc',
            self.listener_callback,
            10)
        self.subscription  # prevent unused variable warning

    def listener_callback(self, msg):
        r = requests.get("http://127.0.0.1:8080/action", params=f"id={msg.id}&{msg.params}")
        if r.status_code != 200:
            rclpy.get_logger().warn(f'bad request. status code {r.status_code}')



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