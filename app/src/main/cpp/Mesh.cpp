//
// Created by hiwang on 2018/5/25.
//

#include <GLES2/gl2.h>
#include "Mesh.h"
#include "Model.h"

extern Shader *shader;
extern glm::mat4 projectionMatrix;

Mesh::Mesh(vector<Vertex> vertices, vector<unsigned int> indices, vector<Texture> textures) {
    this->vertices = vertices;
    this->indices = indices;
    this->textures = textures;
    // now that we have all the required data, set the vertex buffers and its attribute pointers.
    setupMesh();
}

void Mesh::setupMesh() {
    glUseProgram(shader->program);

    glEnableVertexAttribArray(shader->aPositionHandle);
    glVertexAttribPointer(shader->aPositionHandle, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex),
                          &vertices[0].normal);

    glEnableVertexAttribArray(shader->aPositionHandle);
    glVertexAttribPointer(shader->aTextureCoordinatesHandle, 3, GL_FLOAT, GL_FALSE,
                          sizeof(Vertex), &vertices[0].texCoords);

    glUniformMatrix4fv(shader->uMatrixHandle, 1, GL_FALSE,
                       glm::value_ptr(projectionMatrix));
}

void Mesh::draw() {
    glActiveTexture(GL_TEXTURE0);
    glUniform1i(shader->uTextureUnitHandle, 0);
    glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, &indices);
}