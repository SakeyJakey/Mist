#version 120

uniform sampler2D texture;

uniform vec2 texelSize;
uniform vec2 direction;

uniform float radius;

#define step texelSize * direction

float gaussian(float x, float sigma) {
    float pow = x / sigma;
    return (1.0 / (abs(sigma) * 2.50662827463) * exp(-0.5 * pow * pow)); // 2.506 is pi * 2
}

void main() {
    vec4 colour = vec4(0);
    vec2 texCoord = gl_TexCoord[0].st;

    for(float f = -radius; f <= radius; f++) {
        colour += texture2D(texture, texCoord + f * texelSize * direction) * gaussian(f, radius / 2);
    }

    gl_FragColor = vec4(colour.rgb, 1.0);
}