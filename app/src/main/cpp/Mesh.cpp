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

    glGenBuffers(1, &vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, vertices.size() * sizeof(Vertex), &vertices[0],
                 GL_STATIC_DRAW);

    glGenBuffers(1, &ibo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size() * sizeof(unsigned int), &indices[0],
                 GL_STATIC_DRAW);

    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
}

void Mesh::draw() {

    if (textures.size() != 1) {
        // 目前只处理了size==1的情况，不等于1先抛异常
        int size = textures.size();
//        throw "textures.size() = " + textures.size();
    }

    glActiveTexture(GL_TEXTURE0 + textures[0].id);
    glUniform1i(shader->uTextureUnitHandle, textures[0].id);

    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

    glVertexAttribPointer(shader->aPositionHandle, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex),
                          (void *) offsetof(Vertex, position));
    glEnableVertexAttribArray(shader->aPositionHandle);

    glVertexAttribPointer(shader->aTextureCoordinatesHandle, 2, GL_FLOAT, GL_FALSE,
                          sizeof(Vertex), (void *) offsetof(Vertex, texCoords));
    glEnableVertexAttribArray(shader->aTextureCoordinatesHandle);

    glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT , 0);
}