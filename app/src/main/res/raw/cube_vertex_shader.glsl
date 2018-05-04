uniform mat4 u_Matrix;
attribute vec3 a_Position;
varying vec3 v_Position;

void main()
{
    v_Position = a_Position;
    gl_Position = u_Matrix * vec4(a_Position, 1.0);
}
