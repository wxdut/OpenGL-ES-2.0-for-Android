precision mediump float;

uniform samplerCube u_TextureUnit;
uniform vec3 u_LightColor;
uniform vec3 u_LightPosition;
uniform vec3 u_ViewPosition;

varying vec3 v_Normal;
varying vec3 v_FragmentPosition;
varying vec3 v_TexturePosition;

void main()
{
	gl_FragColor = textureCube(u_TextureUnit, v_TexturePosition);
    vec3 fragColor = gl_FragColor.rgb;

    // Ambient
    float ambientStrength = 0.2;
    vec3 ambient = ambientStrength * u_LightColor;

    // Diffuse
    vec3 normal = normalize(v_Normal);
    vec3 lightDirection = normalize(v_FragmentPosition - u_LightPosition);
    float diffFactor = max(dot(normal, -lightDirection), 0.0);
    vec3 diffuse = diffFactor * u_LightColor;

    // Specular
    float specularStrength = 0.5;
    vec3 viewDirection = normalize(v_FragmentPosition - u_ViewPosition);
    vec3 reflectDirection = reflect(lightDirection, normal);
    float specularFactor = pow(max(dot(-viewDirection, reflectDirection), 0.0), 32.0);
    vec3 specular = specularStrength * specularFactor * u_LightColor;

    vec3 result = (diffuse + 0.0) * fragColor;
//    vec3 result = min(max(ambient + diffuse + specular, 0.0), 1.0) * fragColor;

    gl_FragColor = vec4(result, gl_FragColor.a);

}