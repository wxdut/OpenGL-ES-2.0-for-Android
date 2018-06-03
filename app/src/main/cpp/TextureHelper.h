//
// Created by 王肖 on 2018/6/2.
//

#ifndef OPENGL_ES_2_FOR_ANDROID_TEXTUREHELPER_H
#define OPENGL_ES_2_FOR_ANDROID_TEXTUREHELPER_H

#include <list>
#include "LogUtils.h"
#include <GLES2/gl2.h>

using namespace std;

class TextureHelper {
public:
    static list<int> sTextureUnits;
    static int obtain() {
        GLuint texture;
        glGenTextures(1, &texture);
        if (texture == 0) {
            LogUtils::e("obtain, Could not generate a new OpenGL texture object.");
            return 0;
        }
        sTextureUnits.push_back(texture);
        return texture;
    }

    static int bitmap2texture(string path) {
        int texture = obtain();
        glBindTexture(GL_TEXTURE_2D, texture);
//        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, bmp);
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        return 0;
    }
};


#endif //OPENGL_ES_2_FOR_ANDROID_TEXTUREHELPER_H
