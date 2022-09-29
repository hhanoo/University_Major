# 201835526 정현우
import cv2 as cv
import numpy as np

# Open image
img = cv.imread('house.jpeg')
gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

# Apply Harris Corner Detection
gray = np.float32(gray)
dst = cv.cornerHarris(gray, 3, 3, 0.04)
dst = cv.dilate(dst, None)
thresh = 0.01 * dst.max()
print(thresh)

coord = np.where(dst > thresh)
coord = np.stack((coord[1], coord[0]), axis=1)

img_copy = img.copy()
for x, y in coord:
    cv.circle(img_copy, (x, y), 5, (0, 0, 255), 1)

cv.imshow('Detect', img_copy)
cv.waitKey(0)
cv.destroyAllWindows()
