#version 150

// ============================================================================
//  PEZNTZ - LUZ DE LA LINTERNA DENTRO DEL SOMBREADO DE MINECRAFT
//
//  Antes la linterna se pintaba DESPUES, sobre la imagen ya terminada. Con el mundo
//  a oscuras esa imagen llegaba casi negra y en 8 bits: el color real de la textura
//  ya se habia perdido, y por mas que se multiplicara solo salian bandas, grano y
//  grises. No habia forma de recuperar la textura.
//
//  Aqui la linterna es una luz mas, igual que una antorcha: se suma a la del mapa
//  de luz en el mismo momento en que Minecraft pinta cada bloque, cuando todavia
//  tiene la textura entera. Por eso:
//
//    - la textura se ve exactamente igual que con luz normal,
//    - nunca pasa de 1: una pared al sol se queda como esta, no se pone gris,
//    - no tine nada: suma luz blanca, no multiplica el color que ya habia.
//
//  Todo en coordenadas de mundo relativas a la camara.
//  Con PzLuces = 0 y PzRelleno = 0 esto no hace NADA: el sombreado es el de vanilla.
// ============================================================================

uniform int   PzLuces;

uniform vec3  PzOrigen0;
uniform vec3  PzOrigen1;
uniform vec3  PzOrigen2;
uniform vec3  PzOrigen3;

uniform vec3  PzDir0;
uniform vec3  PzDir1;
uniform vec3  PzDir2;
uniform vec3  PzDir3;

uniform float PzCosCono;

// Alcance de cada luz. Las de otros jugadores llegan recortadas desde Java donde
// chocan con una pared, para que no atraviesen al cuarto de al lado.
uniform float PzAlcance0;
uniform float PzAlcance1;
uniform float PzAlcance2;
uniform float PzAlcance3;

uniform vec3  PzColorLuz;
uniform float PzIntensidad;
uniform float PzCaida;
uniform float PzCerca;
uniform float PzLambert;

uniform float PzRelleno;

vec3 pz_normal(vec3 pzN) {
    float largo = length(pzN);
    if (largo < 0.0001) {
        return vec3(0.0, 1.0, 0.0);
    }
    return pzN / largo;
}

float pz_foco(vec3 pzP, vec3 pzN, float pzUsarNormal, vec3 pzO, vec3 pzD, float pzA) {
    vec3  haciaPunto = pzP - pzO;
    float dist = length(haciaPunto);

    if (dist >= pzA || dist < 0.0001) {
        return 0.0;
    }

    vec3  rayo = haciaPunto / dist;
    float c = dot(rayo, pzD);

    if (c <= PzCosCono) {
        return 0.0;
    }

    float halo   = smoothstep(PzCosCono, mix(PzCosCono, 1.0, 0.55), c);
    float nucleo = smoothstep(mix(PzCosCono, 1.0, 0.45), mix(PzCosCono, 1.0, 0.85), c);
    float forma  = halo * 0.35 + nucleo * 0.65;

    float caida = pow(clamp(1.0 - dist / pzA, 0.0001, 1.0), max(PzCaida, 0.1));
    float cerca = smoothstep(0.0, max(PzCerca, 0.001), dist);

    float difuso = 1.0;

    if (pzUsarNormal > 0.5) {
        float ndl = dot(pzN, -rayo);

        // La cara mira al otro lado: la luz esta detras de la pared, no la toca.
        if (ndl <= 0.0) {
            return 0.0;
        }

        difuso = mix(PzLambert, 1.0, ndl);
    }

    return forma * caida * cerca * difuso;
}

vec3 pz_luz_linterna(vec3 pzP, vec3 pzN, float pzUsarNormal) {
    float focos = 0.0;

    if (PzLuces > 0) focos += pz_foco(pzP, pzN, pzUsarNormal, PzOrigen0, PzDir0, PzAlcance0);
    if (PzLuces > 1) focos += pz_foco(pzP, pzN, pzUsarNormal, PzOrigen1, PzDir1, PzAlcance1);
    if (PzLuces > 2) focos += pz_foco(pzP, pzN, pzUsarNormal, PzOrigen2, PzDir2, PzAlcance2);
    if (PzLuces > 3) focos += pz_foco(pzP, pzN, pzUsarNormal, PzOrigen3, PzDir3, PzAlcance3);

    return PzColorLuz * (focos * PzIntensidad) + vec3(PzRelleno);
}

// Luz total de un pixel: la del mapa de luz mas la linterna, con tope en 1.
vec3 pz_mezclar_luz(vec3 pzMapaLuz, vec3 pzLuzLinterna) {
    return min(pzMapaLuz + pzLuzLinterna, vec3(1.0));
}

// Lo que hay que SUMAR a un color que ya trae el mapa de luz multiplicado dentro.
// Es exactamente lo que le falta para llegar al tope, ni un poco mas.
vec3 pz_extra(vec3 pzMapaLuz, vec3 pzLuzLinterna) {
    return min(pzLuzLinterna, max(vec3(1.0) - pzMapaLuz, vec3(0.0)));
}
