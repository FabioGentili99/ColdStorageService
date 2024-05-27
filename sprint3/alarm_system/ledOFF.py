# File: led_OFF.py
import RPi.GPIO as GPIO

# Configurazione dei pin
GPIO.setmode(GPIO.BCM)  # Usa la numerazione BCM
GPIO.setwarnings(False)
LED_PIN = 25

# Imposta il pin GPIO 25 come output
GPIO.setup(LED_PIN, GPIO.OUT)

# Spegni il LED
print("Spegnendo il LED")
GPIO.output(LED_PIN, GPIO.LOW)

# Pulisci la configurazione dei GPIO
GPIO.cleanup()
