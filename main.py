import sys
from Adafruit_IO import MQTTClient
from physical import ActuatorController
# from simple_ai import *
import time

AIO_FEED_ID = ["actuator1", "actuator2"]
AIO_USERNAME = ""
AIO_KEY = ""


def connected(client):
    print("Ket noi thanh cong...")
    for topic in AIO_FEED_ID:
        client.subscribe(topic)

def subscribe(client, userdata, mid, granted_qos):
    print("Subscribe thanh cong...")

def disconnected(client):
    print("Ngat ket noi...")
    sys.exit(1)

def message(client, feed_id, payload):

    print("Nhan du lieu " + feed_id + ":" + payload)
    state = True if payload=="1"  else False
    if feed_id == "actuator1":
        controller.switch_actuator_1(state)
    elif feed_id == "actuator2":
        controller.switch_actuator_2(state)
    else:
        print("TODO!")

controller = ActuatorController()

client = MQTTClient(AIO_USERNAME, AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()

while True:
    client.publish("sensor1", controller.readTemperature()/100)
    client.publish("sensor2", controller.readMoisture()/100)
    time.sleep(0.1)
    # image_capture()
    # ai_result = image_detector()
    # client.publish("visiondetection", ai_result)
