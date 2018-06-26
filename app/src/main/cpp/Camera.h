//
// Created by hiwang(王肖) on 2018/6/23.
//

#ifndef OPENGL_ES_2_0_FOR_ANDROID_CAMERA_H
#define OPENGL_ES_2_0_FOR_ANDROID_CAMERA_H

#include <jni.h>
#include <glm/glm.hpp>

using namespace std;

class Camera {

public:
    glm::mat4 projectionMatrix;
    glm::mat4 viewMatrix;
    glm::mat4 modelMatrix;

};

#endif //OPENGL_ES_2_0_FOR_ANDROID_CAMERA_H