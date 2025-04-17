# 201835526 정현우
import cv2 as cv
import numpy as np

img = cv.imread('./Image_db/house.jpeg')
gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
img_copy = img.copy()

gray = np.float32(gray)
dst = cv.cornerHarris(gray, 3, 3, 0.04)
threshold = 0.05 * dst.max()

for y in range(dst.shape[0]):
    for x in range(dst.shape[1]):
        if dst[y, x] > threshold:
            cv.circle(img_copy, (x, y), 7, (0, 0, 255), 2)

cv.imshow('201835526 Chung Hyunwoo', img_copy)
cv.waitKey(0)
cv.destroyAllWindows()
