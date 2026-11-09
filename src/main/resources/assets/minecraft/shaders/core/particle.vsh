#version 150

// PezntZ: copia de vanilla 1.20.1 con la luz de la linterna sumada al sombreado.
// Las lineas nuevas llevan el prefijo pz. Sin linterna encendida es identico a vanilla.

#moj_import <fog.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in ivec2 UV2;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;

out float vertexDistance;
out vec2 texCoord0;
out vec4 vertexColor;

out vec3 pzPos;
out vec4 pzColorBase;
out vec4 pzMapa;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(ModelViewMat, Position, FogShape);
    texCoord0 = UV0;
    vec4 pzLuzVertice = texelFetch(Sampler2, UV2 / 16, 0);
    vertexColor = Color * pzLuzVertice;

    pzPos = Position;
    pzColorBase = Color;
    pzMapa = pzLuzVertice;
}
