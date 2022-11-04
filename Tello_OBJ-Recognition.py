import cv2
from djitellopy import tello
import cvzone

thres = 0.45
nmsThres = 0.2

classNames = []
classFile = 'ss.names'  # Contains a totoal of 91 different objects which can be recognized by the code
with open(classFile, 'rt') as f:
    classNames = f.read().split('\n')

configPath = 'ssd_mobilenet_v3_large_coco_2020_01_14.pbtxt'
weightsPath = "frozen_inference_graph.pb"

net = cv2.dnn_DetectionModel(weightsPath, configPath)
net.setInputSize(320, 320)
net.setInputScale(1.0 / 127.5)
net.setInputMean((127.5, 127.5, 127.5))
net.setInputSwapRB(True)

me = tello.Tello()
me.connect()
print(me.get_battery())
me.streamoff()
me.streamon()

me.takeoff()
me.move_up(20)

setting = me.get_frame_read().frame
img_x, img_y, img_c = setting.shape
img_center_w = img_x / 2
img_center_h = img_y / 2

height, width, _ = setting.shape
fourcc = cv2.VideoWriter_fourcc(*'DIVX')
video = cv2.VideoWriter('video.avi', fourcc, 10, (width, height))

while True:
    # success, img = cap.read()
    img = me.get_frame_read().frame
    classIds, confs, bbox = net.detect(img, confThreshold=thres,
                                       nmsThreshold=nmsThres)  # To remove duplicates / declare accuracy
    try:
        for classId, conf, box in zip(classIds.flatten(), confs.flatten(), bbox):
            if classNames[classId - 1] == 'dog':
                print(box)
                x, y, w, h = box
                center_x = x + w / 2
                center_y = y + h / 2

                # cvzone.cornerRect(img, box)
                # cv2.putText(img, f'{classNames[classId - 1].upper()} {round(conf * 100, 2)} {box}',
                #             (box[0] + 10, box[1] + 30), cv2.FONT_HERSHEY_COMPLEX_SMALL,
                #             1, (0, 255, 0), 2)

                if center_x < img_center_w - 50:
                    me.rotate_counter_clockwise(10)
                elif center_x > img_center_w + 50:
                    me.rotate_clockwise(10)

                if center_y < img_center_h - 100:
                    me.move_up(10)
                elif center_y > img_center_h + 100:
                    me.move_down(10)

                if w < 200 or h < 200:
                    me.move_forward(20)
                elif w > 400 or h > 400:
                    me.move_back(20)

    except:
        pass

    video.write(img)
    # cv2.imshow("Image", img)
    cv2.waitKey(1)
