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

    glGenBuffers(1, &vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, vertices.size() * sizeof(Vertex), &vertices[0].position.x,
                 GL_STATIC_DRAW);

    glEnableVertexAttribArray(shader->aPositionHandle);
    glVertexAttribPointer(shader->aPositionHandle, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex),
                          (void *) offsetof(Vertex, position));

    glEnableVertexAttribArray(shader->aTextureCoordinatesHandle);
    glVertexAttribPointer(shader->aTextureCoordinatesHandle, 2, GL_FLOAT, GL_FALSE,
                          sizeof(Vertex), (void *) offsetof(Vertex, texCoords));

    glGenBuffers(1, &ibo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size() * sizeof(unsigned int), &indices[0],
                 GL_STATIC_DRAW);
}

void Mesh::draw() {

    glClearColor(1, 1, 1, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glEnable(GL_DEPTH_TEST);

    glUniformMatrix4fv(shader->uMatrixHandle, 1, GL_FALSE,
                       glm::value_ptr(projectionMatrix));

    glActiveTexture(GL_TEXTURE0 + textures[0].id);
    glUniform1i(shader->uTextureUnitHandle, textures[0].id);
    glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);
}