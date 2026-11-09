#version 150

// PezntZ: copia de vanilla 1.20.1 con la luz de la linterna sumada al sombreado.
// Las lineas nuevas llevan el prefijo pz. Sin linterna encendida es identico a vanilla.

#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 ChunkOffset;
uniform int FogShape;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;

out vec3 pzPos;
out vec3 pzNormal;
out vec4 pzColorBase;
out vec4 pzMapa;

void main() {
    vec3 pos = Position + ChunkOffset;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    vertexDistance = fog_distance(ModelViewMat, pos, FogShape);
    vec4 pzLuzVertice = minecraft_sample_lightmap(Sampler2, UV2);
    vertexColor = Color * pzLuzVertice;

    pzPos = pos;
    pzNormal = Normal;
    pzColorBase = Color;
    pzMapa = pzLuzVertice;
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
