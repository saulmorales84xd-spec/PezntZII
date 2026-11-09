#version 150

// PezntZ: copia de vanilla 1.20.1 con la luz de la linterna sumada al sombreado.
// Las lineas nuevas llevan el prefijo pz. Sin linterna encendida es identico a vanilla.

#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in vec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat3 IViewRotMat;
uniform int FogShape;

uniform vec3 Light0_Direction;
uniform vec3 Light1_Direction;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec2 texCoord1;
out vec4 normal;

out vec3 pzPos;
out vec3 pzNormal;
out vec4 pzColorBase;
out vec4 pzMapa;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(ModelViewMat, Position, FogShape);
    vec4 pzSinLuz = minecraft_mix_light(Light0_Direction, Light1_Direction, Normal, Color);
    vec4 pzLuzVertice = texelFetch(Sampler2, UV2 / 16, 0);
    vertexColor = pzSinLuz * pzLuzVertice;

    pzPos = IViewRotMat * Position;
    pzNormal = IViewRotMat * Normal;
    pzColorBase = pzSinLuz;
    pzMapa = pzLuzVertice;
    texCoord0 = UV0;
    texCoord1 = UV1;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
