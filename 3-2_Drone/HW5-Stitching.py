# 201835526 정현우
import numpy as np
import cv2
import warnings

warnings.filterwarnings('ignore')


def ransac(img_1, img_2):
    # SIFT
    detector = shift = cv2.xfeatures2d.SIFT_create()
    keypoint1, descriptors1 = detector.detectAndCompute(img_1, None)
    keypoint2, descriptors2 = detector.detectAndCompute(img_2, None)

    matcher = cv2.DescriptorMatcher_create(cv2.DescriptorMatcher_BRUTEFORCE)
    knn_matches = matcher.knnMatch(descriptors1, descriptors2, 2)

    ratio_thresh = 0.60
    good_matches = []
    for m, n in knn_matches:
        if m.distance < ratio_thresh * n.distance:
            good_matches.append(m)

    # RANSAC
    picture1_empty = np.empty((len(good_matches), 2), dtype=np.float32)
    picture2_empty = np.empty((len(good_matches), 2), dtype=np.float32)

    for k in range(len(good_matches)):
        picture1_empty[k, 0] = keypoint1[good_matches[k].queryIdx].pt[0]
        picture1_empty[k, 1] = keypoint1[good_matches[k].queryIdx].pt[1]
        picture2_empty[k, 0] = keypoint2[good_matches[k].trainIdx].pt[0]
        picture2_empty[k, 1] = keypoint2[good_matches[k].trainIdx].pt[1]

    H, _ = cv2.findHomography(picture2_empty, picture1_empty, cv2.RANSAC)

    return H


def stitching(img_1, img_2):
    H = ransac(img_1, img_2)
    panorama_width = img_1.shape[1] + img_2.shape[1]
    panorama_height = img_1.shape[0]

    result = cv2.warpPerspective(img_2, H, (panorama_width, panorama_height))
    result[0:img_1.shape[0], 0:img_1.shape[1]] = img_1
    return result


def trim(frame):
    h, w, c = frame.shape

    # cut remain long part
    if not np.sum(frame[:, -1]):
        return trim(frame[:, :-2])

    # trim right line
    if 3 * h - cv2.countNonZero(frame[:, -1, :]) > h * 0.15:
        right = frame[:, :-1]
        return trim(right)
    # trim top line
    if 3 * w - cv2.countNonZero(frame[0, :, :]) > w * 0.15:
        return trim(frame[1:])
    # trim bottom line
    if 3 * w - cv2.countNonZero(frame[-1, :, :]) > w * 0.15:
        return trim(frame[:-1])
    # trim left line
    if 3 * h - cv2.countNonZero(frame[:, 0, :]) > h * 0.15:
        return trim(frame[:, 1:])

    return frame


if __name__ == '__main__':
    img1 = cv2.imread('./Image_db/panorama/01.png')
    img2 = cv2.imread('./Image_db/panorama/02.png')
    img3 = cv2.imread('./Image_db/panorama/03.png')
    img4 = cv2.imread('./Image_db/panorama/04.png')
    img6 = cv2.imread('./Image_db/panorama/06.png')
    img7 = cv2.imread('./Image_db/panorama/07.png')
    img8 = cv2.imread('./Image_db/panorama/08.png')

    city1 = trim(stitching(img1, img2))
    city2 = trim(stitching(city1, img3))
    city3 = trim(stitching(city2, img4))

    gachon1 = trim(stitching(img6, img7))
    gachon2 = trim(stitching(gachon1, img8))

    cv2.imshow("city_201835526_Chung Hyunwoo", city3)
    cv2.imshow("gachon_201835526_Chung Hyunwoo", gachon2)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
