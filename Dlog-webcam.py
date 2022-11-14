import cv2
import cvzone

thres = 0.55
nmsThres = 0.4

classNames = []
# Contains a totoal of 91 different objects which can be recognized by the code
classFile = 'ss.names'
with open(classFile, 'rt') as f:
    classNames = f.read().split('\n')

configPath = 'ssd_mobilenet_v3_large_coco_2020_01_14.pbtxt'
weightsPath = "frozen_inference_graph.pb"

net = cv2.dnn_DetectionModel(weightsPath, configPath)
net.setInputSize(320, 320)
net.setInputScale(1.0 / 127.5)
net.setInputMean((127.5, 127.5, 127.5))
net.setInputSwapRB(True)

webcam = cv2.VideoCapture(0)

rect, img = webcam.read()
img_h, img_w, img_c = img.shape
total_area = img_h * img_w
img_center_w = int(img_w / 2)
img_center_h = int(img_h / 2)
center_w = int(img_w * 0.4 / 2)
center_h = int(img_h * 0.4 / 2)
minimum_area = total_area * 0.08
maximum_area = total_area * 0.30

height, width, _ = img.shape
fourcc = cv2.VideoWriter_fourcc(*'DIVX')
video = cv2.VideoWriter('video.avi', fourcc, 10, (width, height))

num = 0
while True:
    rect, img = webcam.read()
    classIds, confs, bbox = net.detect(img, confThreshold=thres,
                                       nmsThreshold=nmsThres)  # To remove duplicates / declare accuracy
    try:
        for classId, conf, box in zip(classIds.flatten(), confs.flatten(), bbox):
            if classNames[classId - 1] == 'dog':
                x, y, w, h = box
                center_x = x + w / 2
                center_y = y + h / 2

                cvzone.cornerRect(img, box)
                cv2.putText(img, 'KKAKKUNG', (box[0] + 10, box[1] + 30), cv2.FONT_HERSHEY_COMPLEX_SMALL,
                            1, (0, 255, 0), 2)

                if num == 0 and ((center_x < img_center_w - center_w) or (x == 0)):
                    cv2.putText(img, 'CCW', (10, 50), cv2.FONT_ITALIC,
                                2, (255, 0, 255), cv2.LINE_8, 2)
                elif num == 0 and ((center_x > img_center_w + center_w) or (x + w == img_w)):
                    cv2.putText(img, 'CW', (10, 50), cv2.FONT_ITALIC,
                                2, (255, 0, 255), cv2.LINE_8, 2)

                if num == 1 and ((center_y < img_center_h - center_h) or (y == 0)):
                    cv2.putText(img, 'UP', (10, 50), cv2.FONT_ITALIC,
                                2, (255, 0, 255), cv2.LINE_8, 2)
                elif num == 1 and ((center_y > img_center_h + center_h) or (y + h == img_h)):
                    cv2.putText(img, 'DOWN', (10, 50), cv2.FONT_ITALIC,
                                2, (255, 0, 255), cv2.LINE_8, 2)

                if num == 2 and ((w * h < minimum_area) and (x != 0)):
                    cv2.putText(img, 'FORWARD', (10, 50),
                                cv2.FONT_ITALIC, 2, (255, 0, 255), cv2.LINE_8, 2)
                elif num == 0 and (w * h > maximum_area):
                    cv2.putText(img, 'BACKWARD', (10, 50),
                                cv2.FONT_ITALIC, 2, (255, 0, 255), cv2.LINE_8, 2)

    except:
        pass

    # num에 따라 동작 종류를 선택
    if num == 2:
        num = 0
    else:
        num += 1

    # object의 중심점의 위치에 따라 드론이 움직일 여부를 판단하는 경계박스
    img = cv2.rectangle(img, (img_center_w - center_w, img_center_h - center_h),
                        (img_center_w + center_w, img_center_h + center_h),
                        (0, 255, 255), 2)
    video.write(img)
    cv2.imshow("Image", img)
    cv2.waitKey(1)
