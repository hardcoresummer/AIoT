print("Sensors and actuator")
import platform
import serial
from serial.tools import list_ports
import time



# 2 last number is crc
RELAY1_ON = [0,6,0,0,0,255,200,91]
RELAY1_OFF = [0,6,0,0,0,0,136,27]


RELAY2_ON = [15,6,0,0,0,255,200,91]
RELAY2_OFF = [15,6,0,0,0,0,136,27]

class ActuatorController():
    def __init__(self) -> None:
        port_list=  list_ports.comports()
        if len(port_list)==0:
            raise Exception("No port found!")

        which_os = platform.system()
        print(which_os)
        if which_os == "linux":
            portName = "/dev/"+ port_list[0].name
        else:
            portName="None"
            for port in port_list:
                strPort = str(port)
                if "USB Serial" in strPort:
                    splitPort = strPort.split(" ")
                    portName = (splitPort[0])

        self.ser = serial.Serial(portName)

    def switch_actuator_1(self,state):
        if state == True:
            self.ser.write(RELAY1_ON)
        else:
            self.ser.write(RELAY1_OFF)

    def switch_actuator_2(self,state):
        if state == True:
            self.ser.write(RELAY2_ON)
            return
        self.ser.write(RELAY2_OFF)
    
    def close(self):
        self.ser.close()

if __name__ == "__main__":
    pass
    # while True:
    #     ser.write(RELAY1_ON)
    #     time.sleep(2)
    #     ser.write(RELAY1_OFF)
    #     time.sleep(10)

