#version 150

uniform sampler2D uSourceSampler;
uniform vec2 uScreenSize;

out vec4 fragColor;

void main() {
    vec2 uv = gl_FragCoord.xy / uScreenSize;
    fragColor = texture(uSourceSampler, uv);
}
