precision mediump float;

uniform sampler2D u_TextureUnit1;
uniform sampler2D u_TextureUnit2;

varying vec2 v_TextureCoordinates;

void main()
{
    gl_FragColor = mix(texture2D(u_TextureUnit1, v_TextureCoordinates), texture2D(u_TextureUnit2, v_TextureCoordinates), 0.2);

}
