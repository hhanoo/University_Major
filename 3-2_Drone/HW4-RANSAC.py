# 201835526 정현우
import numpy as np
import cv2
import warnings

warnings.filterwarnings('ignore')


def feature_ransac(obj, scene, i, j):
    picture1 = obj
    picture2 = scene

    # SIFT
    detector = sift = cv2.xfeatures2d.SIFT_create(sigma=0.7)
    keypoint1, descriptors1 = detector.detectAndCompute(obj, None)
    keypoint2, descriptors2 = detector.detectAndCompute(scene, None)

    matcher = cv2.DescriptorMatcher_create(cv2.DescriptorMatcher_FLANNBASED)
    knn_matches = matcher.knnMatch(descriptors1, descriptors2, 2)

    ratio_thresh = [0.4, 0.55, 0.63]
    good_matches = []
    for m, n in knn_matches:
        if m.distance < ratio_thresh[i] * n.distance:
            good_matches.append(m)

    img_matches = np.empty((max(obj.shape[0], scene.shape[0]), obj.shape[1] + scene.shape[1], 3), dtype=np.uint8)
    cv2.drawMatches(obj, keypoint1, scene, keypoint2, good_matches, img_matches,
                    flags=cv2.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS)

    # RANSAC
    obj = np.empty((len(good_matches), 2), dtype=np.float32)
    scene = np.empty((len(good_matches), 2), dtype=np.float32)
    result = picture2
    if len(good_matches) < 4:
        title = "201835526 Chung Hyunwoo (obj[" + str(i + 1) + "]-scene[" + str(j + 1) + "]) Not found"
        cv2.imshow(title, result)
        cv2.waitKey(0)
        cv2.destroyAllWindows()
    else:
        for k in range(len(good_matches)):
            obj[k, 0] = keypoint1[good_matches[k].queryIdx].pt[0]
            obj[k, 1] = keypoint1[good_matches[k].queryIdx].pt[1]
            scene[k, 0] = keypoint2[good_matches[k].trainIdx].pt[0]
            scene[k, 1] = keypoint2[good_matches[k].trainIdx].pt[1]

        H, _ = cv2.findHomography(obj, scene, cv2.RANSAC)

        obj_corners = np.empty((4, 1, 2), dtype=np.float32)
        obj_corners[0, 0, 0] = 0
        obj_corners[0, 0, 1] = 0
        obj_corners[1, 0, 0] = picture1.shape[1]
        obj_corners[1, 0, 1] = 0
        obj_corners[2, 0, 0] = picture1.shape[1]
        obj_corners[2, 0, 1] = picture1.shape[0]
        obj_corners[3, 0, 0] = 0
        obj_corners[3, 0, 1] = picture1.shape[0]

        scene_corners = cv2.perspectiveTransform(obj_corners, H)

        img_obj_scene = cv2.cvtColor(result, cv2.COLOR_GRAY2BGR)
        cv2.line(img_obj_scene, (int(scene_corners[0, 0, 0]), int(scene_corners[0, 0, 1])),
                 (int(scene_corners[1, 0, 0]), int(scene_corners[1, 0, 1])), (255, 0, 0), 3)
        cv2.line(img_obj_scene, (int(scene_corners[1, 0, 0]), int(scene_corners[1, 0, 1])),
                 (int(scene_corners[2, 0, 0]), int(scene_corners[2, 0, 1])), (255, 0, 0), 3)
        cv2.line(img_obj_scene, (int(scene_corners[2, 0, 0]), int(scene_corners[2, 0, 1])),
                 (int(scene_corners[3, 0, 0]), int(scene_corners[3, 0, 1])), (255, 0, 0), 3)
        cv2.line(img_obj_scene, (int(scene_corners[3, 0, 0]), int(scene_corners[3, 0, 1])),
                 (int(scene_corners[0, 0, 0]), int(scene_corners[0, 0, 1])), (255, 0, 0), 3)

        title = "201835526 Chung Hyunwoo (obj[" + str(i + 1) + "]-scene[" + str(j + 1) + "])"
        cv2.imshow(title, img_obj_scene)
        cv2.waitKey(0)
        cv2.destroyAllWindows()


if __name__ == '__main__':
    obj1 = cv2.imread('Image_db/object1.jpg', cv2.IMREAD_GRAYSCALE)
    obj2 = cv2.imread('Image_db/object2.jpg', cv2.IMREAD_GRAYSCALE)
    obj3 = cv2.imread('Image_db/object3.jpg', cv2.IMREAD_GRAYSCALE)
    scene1 = cv2.imread('Image_db/scene1.jpg', cv2.IMREAD_GRAYSCALE)
    scene2 = cv2.imread('Image_db/scene2.jpg', cv2.IMREAD_GRAYSCALE)
    scene3 = cv2.imread('Image_db/scene3.jpg', cv2.IMREAD_GRAYSCALE)
    scene4 = cv2.imread('Image_db/scene4.jpg', cv2.IMREAD_GRAYSCALE)
    scene5 = cv2.imread('Image_db/scene5.jpg', cv2.IMREAD_GRAYSCALE)
    scene6 = cv2.imread('Image_db/scene6.jpg', cv2.IMREAD_GRAYSCALE)
    scene7 = cv2.imread('Image_db/scene7.jpg', cv2.IMREAD_GRAYSCALE)
    scene8 = cv2.imread('Image_db/scene8.jpg', cv2.IMREAD_GRAYSCALE)

    objList = [obj1, obj2, cv2.add(obj3, -75)]
    sceneList = [scene1, scene2, scene3, scene4, scene5, scene6, scene7, scene8]

    feature_ransac(obj1, scene1, 0, 0)
    feature_ransac(obj1, scene2, 0, 1)
    feature_ransac(obj1, scene3, 0, 2)
    feature_ransac(obj1, scene4, 0, 3)
    feature_ransac(obj1, scene5, 0, 4)
    feature_ransac(obj1, scene6, 0, 5)
    feature_ransac(obj1, scene7, 0, 6)
    feature_ransac(obj1, scene8, 0, 7)
    feature_ransac(obj2, scene1, 1, 0)
    feature_ransac(obj2, scene2, 1, 1)
    feature_ransac(obj2, scene3, 1, 2)
    feature_ransac(obj2, scene4, 1, 3)
    feature_ransac(obj2, scene5, 1, 4)
    feature_ransac(obj2, scene6, 1, 5)
    feature_ransac(obj2, scene7, 1, 6)
    feature_ransac(obj2, scene8, 1, 7)
    feature_ransac(obj3, scene1, 2, 0)
    feature_ransac(obj3, scene2, 2, 1)
    feature_ransac(obj3, scene3, 2, 2)
    feature_ransac(obj3, cv2.add(scene4, -50), 2, 3)
    feature_ransac(cv2.add(obj3, -70), cv2.add(scene5, -65), 2, 4)
    feature_ransac(cv2.add(obj3, -70), cv2.add(scene6, -65), 2, 5)
    feature_ransac(cv2.add(obj3, -73), cv2.add(scene7, -72), 2, 6)
    feature_ransac(cv2.add(obj3, -78), cv2.add(scene8, -65), 2, 7)