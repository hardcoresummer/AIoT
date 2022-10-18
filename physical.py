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
        print(port_list)
        if len(port_list)==0:
            raise Exception("No port found!")

        which_os = platform.system()
        print(which_os)
        if which_os == "Linux":
<<<<<<< Updated upstream
        
=======
            portName = "/dev/"+ port_list[1].name
>>>>>>> Stashed changes
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
    
    def serial_read_data(self):
        ser = self.ser
        bytesToRead = ser.inWaiting()
        if bytesToRead > 0:
            out = ser.read(bytesToRead)
            data_array = [b for b in out]
            print(data_array)
            if len(data_array) >= 7:
                array_size = len(data_array)
                value = data_array[array_size - 4] * 256 + data_array[array_size - 3]
                return value
            else:
                return -1
        return 0
    
    def readTemperature(self):
        soil_temperature = [1, 3, 0, 6, 0, 1, 100, 11]
        self.serial_read_data()
        self.ser.write(soil_temperature)
        time.sleep(1)
        return self.serial_read_data()

    def readMoisture(self):
        soil_moisture = [1, 3, 0, 7, 0, 1, 53, 203]
        self.serial_read_data()
        self.ser.write(soil_moisture)
        time.sleep(1)
        return self.serial_read_data()

    
    

    def serial_read_data(self):
        ser = self.ser
        bytesToRead = ser.inWaiting()
        if bytesToRead > 0:
            out = ser.read(bytesToRead)
            data_array = [b for b in out]
            print(data_array)
            if len(data_array) >= 7:
                array_size = len(data_array)
                value = data_array[array_size - 4] * 256 + data_array[array_size - 3]
                return value
            else:
                return -1
        return 0
    def readTemperature(self):
        soil_temperature =[1, 3, 0, 6, 0, 1, 100, 11]
        self.serial_read_data()
        self.ser.write(soil_temperature)
        time.sleep(1)
        return self.serial_read_data()






if __name__ == "__main__":
    pass
    # while True:
    #     ser.write(RELAY1_ON)
    #     time.sleep(2)
    #     ser.write(RELAY1_OFF)
    #     time.sleep(10)

