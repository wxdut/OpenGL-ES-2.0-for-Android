uniform mat4 u_Matrix;
uniform mat4 u_ProjectionMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ModelMatrix;


attribute vec3 a_Position;
attribute vec3 a_Normal;

varying vec3 v_Normal;
varying vec3 v_Position;

void main() {

    v_Normal = mat3(transpose(inverse(u_ModelMatrix))) * a_Normal;
    v_Position = vec3(u_ModelMatrix * vec4(a_Position, 1.0));
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);

}
