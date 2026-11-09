#version 150

// PezntZ: copia de vanilla 1.20.1 con la luz de la linterna sumada al sombreado.
// Las lineas nuevas llevan el prefijo pz. Sin linterna encendida es identico a vanilla.

#moj_import <fog.glsl>
#moj_import <pezntz_linterna.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

in vec3 pzPos;
in vec3 pzNormal;
in vec4 pzColorBase;
in vec4 pzMapa;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.1) {
        discard;
    }
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    vec3 pzBase = color.rgb;
    color *= vertexColor * ColorModulator;
    vec3 pzLinterna = pz_luz_linterna(pzPos, pz_normal(pzNormal), 1.0);
    color.rgb += pzBase * pzColorBase.rgb * ColorModulator.rgb * pz_extra(pzMapa.rgb, pzLinterna);

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
