uniform mat4 u_ProjectionMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ModelMatrix;
uniform mat4 u_NormalMatrix;


attribute vec3 a_Position;
attribute vec3 a_Normal;

varying vec3 v_Normal;
varying vec3 v_FragmentPosition;
varying vec3 v_TexturePosition;

void main() {

//    v_Normal = mat3(u_NormalMatrix) * a_Normal;
    v_Normal = vec3(u_ModelMatrix * vec4(a_Position, 0.0));
    v_FragmentPosition = vec3(u_ModelMatrix * vec4(a_Position, 1.0));
    v_TexturePosition = a_Position;
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);

}