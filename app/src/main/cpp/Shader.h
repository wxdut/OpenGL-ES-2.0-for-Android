//
// Created by 王肖 on 2018/6/8.
//

#ifndef OPENGL_ES_2_FOR_ANDROID_SHADER_H
#define OPENGL_ES_2_FOR_ANDROID_SHADER_H

#include <string>

using namespace std;

class Shader {
public:
    int program;


    Shader();

    int compileVertexShader(string shaderCode);

    int compileFragmentShader(string shaderCode);

    int compileShader(int type, string shaderCode);

    int linkProgram(int vertexShaderId, int fragmentShaderId);

    bool validateProgram(int programObjectId);

    int buildProgram(string vertexShaderSource, string fragmentShaderSource);

    int uMatrixHandle;
    int uTextureUnitHandle;

    int aPositionHandle;
    int aTextureCoordinatesHandle;
};


#endif //OPENGL_ES_2_FOR_ANDROID_SHADER_H
