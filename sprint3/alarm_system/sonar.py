# File: sonar.py
import RPi.GPIO as GPIO
import time
import sys

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
TRIG = 17
ECHO = 27

GPIO.setup(TRIG, GPIO.OUT)
GPIO.setup(ECHO, GPIO.IN)

GPIO.output(TRIG, False)  # TRIG parte LOW
print('Waiting a few seconds for the sensor to settle')
time.sleep(2)

try:
    while True:
        GPIO.output(TRIG, True)  # invia impulso TRIG
        time.sleep(0.00001)
        GPIO.output(TRIG, False)

        # attendi che ECHO parta e memorizza tempo
        while GPIO.input(ECHO) == 0:
            pulse_start = time.time()

        # registra l'ultimo timestamp in cui il ricevitore rileva il segnale
        while GPIO.input(ECHO) == 1:
            pulse_end = time.time()

        pulse_duration = pulse_end - pulse_start
        distance = pulse_duration * 17165  # distance = vt/2
        distance = round(distance, 1)
        print(distance)
        sys.stdout.flush()  # Importante!
        time.sleep(0.25)

except KeyboardInterrupt:
    print("Measurement stopped by user")
    GPIO.cleanup()
