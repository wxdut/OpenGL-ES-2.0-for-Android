//
// Created by 王肖 on 2018/6/8.
//

#include <GLES2/gl2.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "Shader.h"
#include "LogUtils.h"
#include "Model.h"
#include "string"

extern jobject assetManager;
extern JNIEnv *env;

Shader::Shader() {
    // Compile the shaders and link the program.

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);
    AAsset *asset = AAssetManager_open(mgr, "gl_06_model.vs", AASSET_MODE_BUFFER);
    long len = AAsset_getLength64(asset);
    char *buffer = (char *) malloc(sizeof(char) * len);
    AAsset_read(asset, buffer, len);
    string vsSource(buffer);
    AAsset_close(asset);

    asset = AAssetManager_open(mgr, "gl_06_model.fs", AASSET_MODE_BUFFER);
    len = AAsset_getLength64(asset);
    buffer = (char *) malloc(sizeof(char) * len);
    AAsset_read(asset, buffer, len);
    string fsSource(buffer);
    AAsset_close(asset);

    program = buildProgram(vsSource, fsSource);

    uMatrixHandle = glGetUniformLocation(program, "u_Matrix");

    uTextureUnitHandle = glGetUniformLocation(program, "u_TextureUnit");

    aPositionHandle = glGetAttribLocation(program, "a_Position");
    aTextureCoordinatesHandle = glGetAttribLocation(program, "a_TextureCoordinates");
}

int Shader::compileVertexShader(string shaderCode) {
    return compileShader(GL_VERTEX_SHADER, shaderCode);
}

int Shader::compileFragmentShader(string shaderCode) {
    return compileShader(GL_FRAGMENT_SHADER, shaderCode);
}

int Shader::compileShader(int type, string shaderCode) {
    // Create a new shader object.
    const int shaderObjectId = glCreateShader(type);

    if (shaderObjectId == 0) {
        LogUtils::e("Could not create new shader.");
        return 0;
    }

    // Pass in the shader source.
    const char *c_str = shaderCode.c_str();
    const int len = shaderCode.length();
    glShaderSource(shaderObjectId, 1, &c_str, &len);

    // Compile the shader.
    glCompileShader(shaderObjectId);

    // Get the compilation status.
    int compileStatus[1];
    glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus);

    char *buffer = (char *) malloc(sizeof(char) * 200);
    string log;
    glGetShaderInfoLog(shaderObjectId, 200, NULL, buffer);
    log.append("Results of compiling source: ").append(shaderCode).append(buffer);
    LogUtils::d(log);

    // Verify the compile status.
    if (compileStatus[0] == 0) {
        // If it failed, delete the shader object.
        glDeleteShader(shaderObjectId);
        LogUtils::e("Compilation of shader failed.");
        return 0;
    }

    // Return the shader object ID.
    return shaderObjectId;
}

int Shader::linkProgram(int vertexShaderId, int fragmentShaderId) {
    // Create a new program object.
    const int programObjectId = glCreateProgram();

    if (programObjectId == 0) {
        LogUtils::e("Could not create new program");
        return 0;
    }

    // Attach the vertex shader to the program.
    glAttachShader(programObjectId, vertexShaderId);

    // Attach the fragment shader to the program.
    glAttachShader(programObjectId, fragmentShaderId);

    // Link the two shaders together into a program.
    glLinkProgram(programObjectId);

    // Get the link status.
    int linkStatus[1];
    glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus);

    LogUtils::e("Results of linking program: " + to_string(linkStatus[0]));

    // Verify the link status.
    if (linkStatus[0] == 0) {
        // If it failed, delete the program object.
        glDeleteProgram(programObjectId);

        LogUtils::e("Linking of program failed.");

        return 0;
    }

    // Return the program object ID.
    return programObjectId;
}

bool Shader::validateProgram(int programObjectId) {
    glValidateProgram(programObjectId);
    int validateStatus[1];
    glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus);
    LogUtils::e("Results of validating program: " + to_string(validateStatus[0]));

    return validateStatus[0] != 0;
}

int Shader::buildProgram(string vertexShaderSource, string fragmentShaderSource) {
    int program;

    // Compile the shaders.
    int vertexShader = compileVertexShader(vertexShaderSource);
    int fragmentShader = compileFragmentShader(fragmentShaderSource);

    // Link them into a shader program.
    program = linkProgram(vertexShader, fragmentShader);

    validateProgram(program);

    return program;
}


