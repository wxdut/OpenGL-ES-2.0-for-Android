//
// Created by hiwang on 2018/5/25.
//

#include <GLES2/gl2.h>
#include "Mesh.h"
#include "Model.h"

Mesh::Mesh(vector<Vertex> vertices, vector<unsigned int> indices, vector<Texture> textures) {
    this->vertices = vertices;
    this->indices = indices;
    this->textures = textures;
    // now that we have all the required data, set the vertex buffers and its attribute pointers.
    setupMesh();
}

void Mesh::setupMesh() {
    glUseProgram(Model::shader.program);

    glEnableVertexAttribArray(Model::shader.aPositionHandle);
    glVertexAttribPointer(Model::shader.aPositionHandle, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex),
                          &vertices[0].normal);

    glEnableVertexAttribArray(Model::shader.aPositionHandle);
    glVertexAttribPointer(Model::shader.aTextureCoordinatesHandle, 3, GL_FLOAT, GL_FALSE,
                          sizeof(Vertex), &vertices[0].texCoords);

    glUniformMatrix4fv(Model::shader.uMatrixHandle, 1, GL_FALSE,
                       glm::value_ptr(Model::projectionMatrix));
}

void Mesh::draw() {
    glActiveTexture(GL_TEXTURE0);
    glUniform1i(Model::shader.uTextureUnitHandle, 0);
    glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, &indices);
}