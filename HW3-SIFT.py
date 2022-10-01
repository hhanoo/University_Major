# 201835526 정현우
import numpy as np
import cv2 as cv


def contrast(gray, grad, inter):
    rest_img = np.clip(grad * gray + inter, 0, 255).astype(np.uint8)
    return rest_img


def feature_matching(obj, scene, i, j):
    detector = sift = cv.xfeatures2d.SIFT_create()
    keypoint1, descriptors1 = detector.detectAndCompute(obj, None)
    keypoint2, descriptors2 = detector.detectAndCompute(scene, None)

    # img_res_detect1 = cv.drawKeypoints(obj, keypoint1, None)
    # img_res_detect2 = cv.drawKeypoints(scene, keypoint2, None)

    matcher = cv.DescriptorMatcher_create(cv.DescriptorMatcher_FLANNBASED)
    knn_matches = matcher.knnMatch(descriptors1, descriptors2, 2)

    ratio_thresh = [0.3, 0.5, 0.6]
    good_matches = []
    for m, n in knn_matches:
        if m.distance < ratio_thresh[i] * n.distance:
            good_matches.append(m)
    img_matches = np.empty((max(obj.shape[0], scene.shape[0]), obj.shape[1] + scene.shape[1], 3), dtype=np.uint8)
    cv.drawMatches(obj, keypoint1, scene, keypoint2, good_matches, img_matches,
                   flags=cv.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS)

    # cv.imshow('img1', obj)
    # cv.imshow('img2', scene)
    # cv.imshow('img_res_detect1', img_res_detect1)
    # cv.imshow('img_res_detect2', img_res_detect2)
    title = "img_matches obj[" + str(i + 1) + "]-scene[" + str(j + 1) + "]"
    cv.imshow(title, img_matches)
    cv.waitKey(0)
    cv.destroyAllWindows()


if __name__ == '__main__':
    obj1 = cv.imread('Image_db/object1.jpg', cv.IMREAD_GRAYSCALE)
    obj2 = cv.imread('Image_db/object2.jpg', cv.IMREAD_GRAYSCALE)
    obj3 = cv.imread('Image_db/object3.jpg', cv.IMREAD_GRAYSCALE)
    scene1 = cv.imread('Image_db/scene1.jpg', cv.IMREAD_GRAYSCALE)
    scene2 = cv.imread('Image_db/scene2.jpg', cv.IMREAD_GRAYSCALE)
    scene3 = cv.imread('Image_db/scene3.jpg', cv.IMREAD_GRAYSCALE)
    scene4 = cv.imread('Image_db/scene4.jpg', cv.IMREAD_GRAYSCALE)
    scene5 = cv.imread('Image_db/scene5.jpg', cv.IMREAD_GRAYSCALE)
    scene6 = cv.imread('Image_db/scene6.jpg', cv.IMREAD_GRAYSCALE)
    scene7 = cv.imread('Image_db/scene7.jpg', cv.IMREAD_GRAYSCALE)
    scene8 = cv.imread('Image_db/scene8.jpg', cv.IMREAD_GRAYSCALE)

    objList = [obj1, obj2, obj3]
    augmentObjectList = [
        contrast(obj1, 1.3, -70),
        contrast(obj2, 1.4, -93),
        contrast(obj3, 1.5, -70)
    ]
    sceneList = [scene1, scene2, scene3, scene4, scene5, scene6, scene7, scene8]
    augmentSceneList = [contrast(scene1, 1.6, -40), contrast(scene2, 1.6, -40), contrast(scene3, 1.5, -20),
                        contrast(scene4, 1.5, -20), contrast(scene5, 1.7, -90), contrast(scene6, 1.5, -70),
                        contrast(scene7, 1.7, -65), contrast(scene8, 1.7, -85)]

    for i in range(len(augmentObjectList)):
        for j in range(len(augmentSceneList)):
            feature_matching(augmentObjectList[i], augmentSceneList[j], i, j)
