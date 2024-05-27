# File: led_ON.py
import RPi.GPIO as GPIO

# Configurazione dei pin
GPIO.setmode(GPIO.BCM)  # Usa la numerazione BCM
GPIO.setwarnings(False)
LED_PIN = 25

# Imposta il pin GPIO 25 come output
GPIO.setup(LED_PIN, GPIO.OUT)

# Accendi il LED
print("Accendendo il LED")
GPIO.output(LED_PIN, GPIO.HIGH)


