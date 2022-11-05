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

# me.takeoff()
# me.move_up(20)

img = me.get_frame_read().frame
img_h, img_w, img_c = img.shape
total_area = img_h * img_w
img_center_w = int(img_w / 2)
img_center_h = int(img_h / 2)
center_w = int(img_w * 0.4 / 2)
center_h = int(img_h * 0.4 / 2)
minimum_area = total_area * 0.20
maximum_area = total_area * 0.60

height, width, _ = img.shape
fourcc = cv2.VideoWriter_fourcc(*'DIVX')
video = cv2.VideoWriter('video.avi', fourcc, 10, (width, height))

num = 0
while True:
    # success, img = cap.read()
    img = me.get_frame_read().frame
    classIds, confs, bbox = net.detect(img, confThreshold=thres,
                                       nmsThreshold=nmsThres)  # To remove duplicates / declare accuracy
    try:
        for classId, conf, box in zip(classIds.flatten(), confs.flatten(), bbox):
            if classNames[classId - 1] == 'dog':
                x, y, w, h = box
                center_x = x + w / 2
                center_y = y + h / 2

                cvzone.cornerRect(img, box)
                cv2.putText(img, f'{classNames[classId - 1].upper()} {round(conf * 100, 2)} {box}',
                            (box[0] + 10, box[1] + 30), cv2.FONT_HERSHEY_COMPLEX_SMALL,
                            1, (0, 255, 0), 2)

                if num == 0 and (center_x < img_center_w - center_w):
                    me.rotate_counter_clockwise(5)
                    print('반시계')
                elif num == 0 and (center_x > img_center_w + center_w):
                    me.rotate_clockwise(5)
                    print('시계')

                if num == 1 and (center_y < img_center_h - center_h*2):
                    me.move_up(20)
                    cv2.waitKey(10)
                    print('위로')
                elif num == 1 and (center_y > img_center_h + center_h*2):
                    me.move_down(10)
                    cv2.waitKey(10)
                    print('아래로')

                if num == 2 and (w * h < minimum_area):
                    me.move_forward(15)
                    cv2.waitKey(10)
                    print('전진')
                elif num == 0 and (w * h > maximum_area):
                    me.move_back(20)
                    cv2.waitKey(10)
                    print('후진')

    except:
        pass

    # num에 따라 동작 종류를 선택
    if num == 2:
        num = 0
    else:
        num += 1

    # object의 중심점의 위치에 따라 드론이 움직일 여부를 판단하는 경계박스
    # img = cv2.rectangle(img, (img_center_w - center_w, img_center_h - center_h),
    #                     (img_center_w + center_w, img_center_h + center_h),
    #                     (255, 0, 255), 2)
    video.write(img)
    cv2.imshow("Image", img)
    cv2.waitKey(1)
