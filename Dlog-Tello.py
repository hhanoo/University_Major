import cv2
from djitellopy import tello
import cvzone

thres = 0.45
nmsThres = 0.25

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
me.move_up(40)

img = me.get_frame_read().frame
img_h, img_w, img_c = img.shape
total_area = img_h * img_w
img_center_w = int(img_w / 2)
img_center_h = int(img_h / 2)
center_w = int(img_w * 0.6 / 2)
center_h = int(img_h * 0.6 / 2)
minimum_area = total_area * 0.08
maximum_area = total_area * 0.30

height, width, _ = img.shape
fourcc = cv2.VideoWriter_fourcc(*'DIVX')
video = cv2.VideoWriter('video.avi', fourcc, 10, (width, height))

num = 0
while True:
    img = me.get_frame_read().frame
    classIds, confs, bbox = net.detect(img, confThreshold=thres,
                                       nmsThreshold=nmsThres)  # To remove duplicates / declare accuracy
    try:
        for classId, conf, box in zip(classIds.flatten(), confs.flatten(), bbox):
            if classNames[classId - 1] == 'dog':
                print('find')
                x, y, w, h = box
                center_x = x + w / 2
                center_y = y + h / 2

                cvzone.cornerRect(img, box)
                cv2.putText(img, 'KKAKKUNG', (box[0] + 10, box[1] + 30), cv2.FONT_HERSHEY_COMPLEX_SMALL,
                            1, (0, 255, 0), 2)

                if num == 0 and ((center_x < img_center_w - center_w) or (x == 0)):
                    me.rotate_counter_clockwise(20)
                    print('반시계')
                elif num == 0 and ((center_x > img_center_w + center_w) or (x + w == img_w)):
                    me.rotate_clockwise(20)
                    print('시계')

                if num == 1 and ((center_y < img_center_h - center_h) or (y == 0)):
                    me.move_up(20)
                    print('위로')
                elif num == 1 and ((center_y > img_center_h + center_h) or (y + h == img_h)):
                    me.move_down(10)
                    print('아래로')

                if num == 2 and ((w * h < minimum_area) and (x != 0)):
                    me.move_forward(20)
                    print('전진')
                elif num == 0 and (w * h > maximum_area):
                    me.move_back(25)
                    print('후진')

            # num에 따라 동작 종류를 선택
            if num == 2:
                num = 0
            else:
                num += 1

    except:
        pass

    video.write(img)
    cv2.waitKey(1)
