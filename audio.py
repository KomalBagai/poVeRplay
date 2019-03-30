import librosa
import pyaudio
import wave
import numpy as np
import matplotlib.pyplot as plt
import librosa.display
import numpy as np
import datetime;
import time
import cv2
CHUNK = 1024
FORMAT = pyaudio.paInt16
CHANNELS = 2
RATE = 44100
RECORD_SECONDS = 3
WAVE_OUTPUT_FILENAME = "output.wav"

def aaa():
    p = pyaudio.PyAudio()

    stream = p.open(format=FORMAT,
                    channels=CHANNELS,
                    rate=RATE,
                    input=True,
                    frames_per_buffer=CHUNK)

    print("* recording")

    frames = []

    for i in range(0, int(RATE / CHUNK * RECORD_SECONDS)):
        data = stream.read(CHUNK)
        frames.append(data)

    print("* done recording")

    stream.stop_stream()
    stream.close()
    p.terminate()

    wf = wave.open(WAVE_OUTPUT_FILENAME, 'wb')
    wf.setnchannels(CHANNELS)
    wf.setsampwidth(p.get_sample_size(FORMAT))
    wf.setframerate(RATE)
    wf.writeframes(b''.join(frames))
    wf.close()

    y , sr =librosa.load(r'C:\Users\PIYUSH\Desktop\output.wav',sr=32050)
#import librosa
    s = np.abs(librosa.stft(y))
    pitches, magnitudes = librosa.piptrack(y=y, sr=sr)
    s_mean=(np.mean(s))
    s_pitch=(np.mean(magnitudes))
    result=(s_mean+s_pitch)/2
    return result

import pyrebase

outpath=r"C:\Users\harsh\Desktop\cctv.jpeg"
config = {
    "apiKey": "apiKey",
    "authDomain": ".firebaseapp.com",
    "databaseURL": "https://.firebaseio.com",
    "storageBucket": ".appspot.com",
    #"serviceAccount": "path/to/serviceAccountCredentials.json"
}


firebase = pyrebase.initialize_app(config)
storage = firebase.storage()
db = firebase.database()
   
def upload(outpath):
    mydate=(datetime.datetime.now())
    myaslidate=str(mydate.strftime('%b %d %Y'))+" "+str(mydate.hour)+" "+str(mydate.minute)+" "+str(mydate.second)+"-18.5513821,73.8230777.jpeg"
    storage.child(myaslidate).put(outpath)
    db.child("users").child("1").push(myaslidate)  #mydate.microsecond
    
result1=aaa()
print(result1)
    
cap = cv2.VideoCapture(1)


def karnapadega():
    while(True):
        ret,frame=cap.read()
        if ret== True:
            cv2.imshow("frame",frame)
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            if result1>0.01:
                #gray = cv2.cvtColor(frame)
                cv2.imwrite(outpath,gray)
                cv2.waitKey(1)        
                cap.release()
                cv2.destroyAllWindows()
            #return cv2.imread('harshil.jpg',0)
                upload(outpath)

                break
                         
            else:
                cv2.destroyAllWindows()
                break

while(True):
    time.sleep(5)
    result1=aaa()
    print(result1)
    cap = cv2.VideoCapture(0)
    karnapadega()
