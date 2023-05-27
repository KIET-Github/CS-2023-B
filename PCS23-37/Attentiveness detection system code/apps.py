from flask import Flask, render_template, Response
import cv2
# Numpy for array related functions
import numpy as np
# Dlib for deep learning based Modules and face landmark detection
import dlib
#face_utils for basic operations of conversion
from imutils import face_utils
import os
from pygame import mixer
import time

app = Flask(__name__)
mixer.init()
sound = mixer.Sound('alarm.wav')
detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor("shape_predictor_68_face_landmarks.dat")
def get_landmarks(im):
    rects = detector(im, 1)
    if len(rects) > 1:
        return "error"
    if len(rects) == 0:
        return "error"
    return np.matrix([[p.x, p.y] for p in predictor(im, rects[0]).parts()])
# This Function will return image with landmarks on the image
def annotate_landmarks(im, landmarks):
    im = im.copy()
    for idx, point in enumerate(landmarks):
        pos = (point[0, 0], point[0, 1])
        cv2.putText(im, str(idx), pos, fontFace= cv2.FONT_HERSHEY_SCRIPT_SIMPLEX, fontScale=0.4, color=(0, 0, 255))
        cv2.circle(im, pos, 3, color=(0, 255, 255))
    return im
def top_lip(landmarks):
    top_lip_pts = []
    for i in range(50,53):
        top_lip_pts.append( landmarks[i])
    for i in range(61,64):
        top_lip_pts.append( landmarks[i])
    top_lip_all_pts = np.squeeze( np.asarray( top_lip_pts))
    top_lip_mean = np.mean(top_lip_pts, axis=0)
    return int(top_lip_mean[:,1])
# This Function will obtain the mean points of bottom lip
def bottom_lip(landmarks):
    bottom_lip_pts = []
    for i in range(65,68):
        bottom_lip_pts.append( landmarks[i])
    for i in range(56,59):
        bottom_lip_pts.append( landmarks[i])
    bottom_lip_all_pts = np.squeeze(np.asarray( bottom_lip_pts))
    bottom_lip_mean = np.mean(bottom_lip_pts, axis=0)
    return int(bottom_lip_mean[:,1])
# This Function will return distance between landmark image and two lips
def mouth_open(image):
    landmarks = get_landmarks(image)
    if landmarks == "error":
        return image, 0
    face_frame = annotate_landmarks(image, landmarks)
    top_lip_center = top_lip(landmarks)
    bottom_lip_center = bottom_lip(landmarks)
    lip_distance = abs(top_lip_center - bottom_lip_center)
    return face_frame, lip_distance

path = os.getcwd()
cap = cv2.VideoCapture(0)
drowsy=0
color=(0,0,0)
status=""
score=0
count=0
thicc=0
yawns=0
yawn_status = False
def compute(ptA,ptB):
    dist = np.linalg.norm(ptA - ptB)
    return dist

def blinked(a,b,c,d,e,f):
    up = compute(b,d) + compute(c,e)
    down = compute(a,f)
    ratio = up/(2.0*down)

#Checking if it is blink
    if(ratio>0.25):
        return 2
    elif(ratio>0.21 and ratio<=0.25):
        return 1
    else:
        return 0
while True:
     # Read frames from webcam
    _,frame = cap.read()
    gray=cv2.cvtColor(frame,cv2.COLOR_BGR2GRAY)

    faces= detector(gray)
    #detected face in faces array
    for face in faces:
        x1 = face.left()
        y1 = face.top()
        x2 = face.right()
        y2 = face.bottom()

        face_frame=frame.copy()
        
        cv2.rectangle(face_frame, (x1, y1), (x2, y2), (0, 255, 0), 2)

        landmarks = predictor(gray, face)
        landmarks = face_utils.shape_to_np(landmarks)

        #The numbers are actually the landmarks which will show eye
        left_blink = blinked(landmarks[36],landmarks[37],landmarks[38], landmarks[41], landmarks[40], landmarks[39])
        right_blink = blinked(landmarks[42],landmarks[43],landmarks[44], landmarks[47], landmarks[46], landmarks[45])
        
        #Now judge what to do for the eye blinks
        if(left_blink==0 or right_blink==0):
            score=score+1
            drowsy=0
            status="Sleeping"

        elif(left_blink==1 or right_blink==1):
            sleep=0
            active=0
            drowsy=drowsy+1
            status="Drowsy "
            color = (0,0,255)

        else:
            drowsy=0
            score=score-1
            status="Active"
        
        cv2.putText(frame, status, (100,100), cv2.FONT_HERSHEY_SIMPLEX, 1, color,3)

        for n in range(0, 68):
            (x,y) = landmarks[n]
            cv2.circle(face_frame, (x, y), 1, (255, 255, 255), -1)
            
    # Obtain image_landmarks lip_distance from mouth_open function for current frame.
    face_frame, lip_distance = mouth_open(frame)
    # Store current yawn_status in prev_yawn_status
    prev_yawn_status = yawn_status
    # If the lips distance is more than 30 then display subject is yawning along with yawn count.
    if lip_distance > 30:
        yawn_status = True
        cv2.putText(frame, "Yawning", (250,250), cv2.FONT_HERSHEY_COMPLEX, 1,(0,0,255),2)
        output_text = " Yawn Count: " + str(yawns + 1)
        cv2.putText(frame, output_text, (50,50), cv2.FONT_HERSHEY_COMPLEX, 1,(0,255,127),2)
    # If not lips distance is less than 25 then set yawn status to False
    else:
        yawn_status = False
    # Increasing yawn count if subject was yawning in previous frame as well
    if prev_yawn_status == True and yawn_status == False:
        yawns += 1
    
    if(score<0):
        score=0   
    cv2.putText(frame,'Score:'+str(score), (100,40), cv2.FONT_HERSHEY_SIMPLEX, 1,(255,255,255),1,cv2.LINE_AA)
    if(score>30 or yawns >10):
        cv2.imwrite(os.path.join(path,'image.jpg'),frame)
        try:
            sound.play()
        except:  # isplaying = False
            pass
        if(thicc<25):
            thicc= thicc+2
        else:
            thicc=thicc-2
            if(thicc<2):
                thicc=2
        cv2.rectangle(frame,(0,0),(100,100),(0,0,255),thicc)

    if cv2.waitKey(1) & 0xFF == ord('q') and cv2.waitKey(1) == 13:
        break
     # Display live landmark of face
    cv2.imshow("Frame", frame)
    cv2.imshow("Result of detector", face_frame)
    ret, jpeg = cv2.imencode('.jpg', frame)
    frame = jpeg.tobytes()
cap.release()  # Release webcam
cv2.destroyAllWindows()  # Close OpenCV windows
        
@app.route('/')
def index():
    return render_template('index.html')

@app.route('/video_feed')
def video_feed():
    return Response(imetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == '__main__':
    app.run(debug=True)
