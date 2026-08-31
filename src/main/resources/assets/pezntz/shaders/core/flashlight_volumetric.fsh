#version 150

uniform sampler2D uColorSampler;
uniform sampler2D uDepthSampler;

uniform mat4  uInvMVP;
uniform vec2  uScreenSize;

uniform int   uLightCount;
uniform vec3  uLightColor;
uniform float uBrightnessMultiplier;

uniform float uSelfLitLow;
uniform float uSelfLitHigh;

uniform int   uVolumetricSteps;
uniform float uVolumetricIntensity;
uniform float uVolumetricNoise;
uniform float uGameTime;

uniform vec3  uLightOrigin0;
uniform vec3  uLightOrigin1;
uniform vec3  uLightOrigin2;
uniform vec3  uLightOrigin3;

uniform vec3  uLightDir0;
uniform vec3  uLightDir1;
uniform vec3  uLightDir2;
uniform vec3  uLightDir3;

uniform float uConeAngleCos0;
uniform float uConeAngleCos1;
uniform float uConeAngleCos2;
uniform float uConeAngleCos3;

uniform float uRange0;
uniform float uRange1;
uniform float uRange2;
uniform float uRange3;

uniform float uIntensity0;
uniform float uIntensity1;
uniform float uIntensity2;
uniform float uIntensity3;

out vec4 fragColor;

vec3 getLightOrigin(int i) {
    if (i == 0) return uLightOrigin0;
    if (i == 1) return uLightOrigin1;
    if (i == 2) return uLightOrigin2;
    return uLightOrigin3;
}
vec3 getLightDir(int i) {
    if (i == 0) return uLightDir0;
    if (i == 1) return uLightDir1;
    if (i == 2) return uLightDir2;
    return uLightDir3;
}
float getConeAngleCos(int i) {
    if (i == 0) return uConeAngleCos0;
    if (i == 1) return uConeAngleCos1;
    if (i == 2) return uConeAngleCos2;
    return uConeAngleCos3;
}
float getRange(int i) {
    if (i == 0) return uRange0;
    if (i == 1) return uRange1;
    if (i == 2) return uRange2;
    return uRange3;
}
float getIntensity(int i) {
    if (i == 0) return uIntensity0;
    if (i == 1) return uIntensity1;
    if (i == 2) return uIntensity2;
    return uIntensity3;
}

float interleavedGradientNoise(vec2 p) {
    return fract(52.9829189 * fract(dot(p, vec2(0.06711056, 0.00583715))));
}

float hash13(vec3 p) {
    p = fract(p * 0.3183099 + 0.1);
    p *= 17.0;
    return fract(p.x * p.y * p.z * (p.x + p.y + p.z));
}

float valueNoise3(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(
        mix(mix(hash13(i + vec3(0, 0, 0)), hash13(i + vec3(1, 0, 0)), f.x),
            mix(hash13(i + vec3(0, 1, 0)), hash13(i + vec3(1, 1, 0)), f.x), f.y),
        mix(mix(hash13(i + vec3(0, 0, 1)), hash13(i + vec3(1, 0, 1)), f.x),
            mix(hash13(i + vec3(0, 1, 1)), hash13(i + vec3(1, 1, 1)), f.x), f.y),
        f.z);
}

vec3 reconstructWorldPos(vec2 uv, float rawDepth) {
    vec4 clipPos = vec4(uv * 2.0 - 1.0, rawDepth * 2.0 - 1.0, 1.0);
    vec4 worldH  = uInvMVP * clipPos;
    float wSafe = abs(worldH.w) < 1e-5 ? 1e-5 : worldH.w;
    return worldH.xyz / wSafe;
}

void evaluateSurfaceLight(int i, vec3 worldPos, out float lit) {
    float range = getRange(i);
    if (range <= 0.0) { lit = 0.0; return; }

    vec3  toPoint = worldPos - getLightOrigin(i);
    float dist    = length(toPoint);
    if (dist > range) { lit = 0.0; return; }

    vec3  dirToPoint = toPoint / max(dist, 0.0001);
    float cosAngle   = dot(dirToPoint, getLightDir(i));
    float outerCos   = getConeAngleCos(i);
    if (cosAngle < outerCos) { lit = 0.0; return; }

    float edgeSoft  = smoothstep(outerCos, mix(outerCos, 1.0, 0.2), cosAngle);
    float distAtten = pow(clamp(1.0 - dist / range, 0.0, 1.0), 1.2);

    lit = edgeSoft * distAtten * getIntensity(i);
}

bool volumetricInterval(int i, vec3 ro, vec3 rd, float tMax, out float t0, out float t1) {
    vec3  A = getLightOrigin(i) - ro;
    vec3  d = getLightDir(i);
    float R = getRange(i);

    float bq   = -dot(rd, A);
    float cq   = dot(A, A) - R * R;
    float disc = bq * bq - cq;
    if (disc <= 0.0) return false;
    float sq = sqrt(disc);
    t0 = max(-bq - sq, 0.0);
    t1 = min(-bq + sq, tMax);
    if (t1 <= t0) return false;

    float vd  = dot(rd, d);
    float coneCos = getConeAngleCos(i);
    float c2  = coneCos * coneCos;
    float a   = vd * vd - c2;
    float cod = -dot(A, d);
    float b2  = vd * cod - c2 * bq;
    float cc  = cod * cod - c2 * dot(A, A);

    if (abs(vd) > 1e-6) {
        float tApex = -cod / vd;
        if (vd > 0.0) t0 = max(t0, tApex);
        else          t1 = min(t1, tApex);
        if (t1 <= t0) return false;
    } else if (cod < 0.0) {
        return false;
    }

    if (abs(a) > 1e-4) {
        float cdisc = b2 * b2 - a * cc;
        if (cdisc <= 0.0) {
            if (a < 0.0) return false;
        } else {
            float csq = sqrt(cdisc);
            float r0  = (-b2 - csq) / a;
            float r1  = (-b2 + csq) / a;
            float lo  = min(r0, r1), hi = max(r0, r1);
            if (a < 0.0) {
                t0 = max(t0, lo);
                t1 = min(t1, hi);
            } else if (vd < 0.0) {
                t1 = min(t1, lo);
            } else {
                t0 = max(t0, hi);
            }
            if (t1 <= t0) return false;
        }
    }

    return true;
}

vec3 sampleVolumetric(vec3 rayOrigin, vec3 worldPos, int i) {
    vec3  toSurface = worldPos - rayOrigin;
    float rayLen    = length(toSurface);
    vec3  rayDir    = toSurface / max(rayLen, 0.001);
    float lightRange = getRange(i);
    float marchCap  = min(rayLen, lightRange);
    if (marchCap <= 0.01) return vec3(0.0);

    float t0, t1;
    if (!volumetricInterval(i, rayOrigin, rayDir, rayLen, t0, t1)) return vec3(0.0);
    float segLen = t1 - t0;
    if (segLen <= 1e-4) return vec3(0.0);

    int   steps   = max(uVolumetricSteps, 1);
    float stepLen = segLen / float(steps);
    float jitter  = interleavedGradientNoise(gl_FragCoord.xy);

    vec3 lightOrigin = getLightOrigin(i);
    vec3 lightDir    = getLightDir(i);
    float outerCos   = getConeAngleCos(i);

    float accum = 0.0;
    for (int s = 0; s < steps; s++) {
        float t = t0 + (float(s) + jitter) * stepLen;
        vec3  samplePos = rayOrigin + rayDir * t;

        vec3  toSample = samplePos - lightOrigin;
        float dist     = length(toSample);
        if (dist < 0.05 || dist > lightRange) continue;

        vec3  sampleDir = toSample / dist;
        float cosSample = dot(sampleDir, lightDir);
        if (cosSample < outerCos) continue;

        float edgeSoft = smoothstep(outerCos, 1.0, cosSample);
        float proximityT = clamp(1.0 - dist / lightRange, 0.0, 1.0);
        float rangeAtten = pow(proximityT, 1.5);

        float density = 1.0;
        if (uVolumetricNoise > 0.0) {
            vec3  noiseCoord = samplePos * 1.2 + vec3(0.0, 0.0, uGameTime * 0.05);
            float n = valueNoise3(noiseCoord) * 0.6 + valueNoise3(noiseCoord * 2.3 + 11.0) * 0.4;
            density = clamp(mix(1.0, n * 1.3, clamp(uVolumetricNoise, 0.0, 1.0)), 0.2, 1.3);
        }

        accum += edgeSoft * rangeAtten * density;
    }

    vec3 result = uLightColor * getIntensity(i) * uVolumetricIntensity
    * (accum / float(steps)) * (segLen / marchCap);

    return result;
}

void main() {
    vec2 screenUV = gl_FragCoord.xy / uScreenSize;
    float depth = texture(uDepthSampler, screenUV).r;
    vec3 sceneColor = texture(uColorSampler, screenUV).rgb;

    vec3 worldPos  = reconstructWorldPos(screenUV, depth);
    vec3 rayOrigin = reconstructWorldPos(screenUV, 0.0);

    float totalLit = 0.0;
    vec3  glow = vec3(0.0);

    for (int i = 0; i < 4; i++) {
        if (i >= uLightCount) break;
        float lit;
        evaluateSurfaceLight(i, worldPos, lit);
        totalLit = max(totalLit, lit);
        if (uVolumetricIntensity > 0.0) {
            glow += sampleVolumetric(rayOrigin, worldPos, i);
        }
    }
    totalLit = clamp(totalLit, 0.0, 1.0);

    float luminance = dot(sceneColor, vec3(0.299, 0.587, 0.114));

    vec3 darkBase = sceneColor;

    float selfLit = smoothstep(uSelfLitLow, uSelfLitHigh, luminance);
    vec3 gain = vec3(1.0) + uBrightnessMultiplier * uLightColor * totalLit;
    gain = mix(gain, vec3(1.0), selfLit);
    vec3 litColor = sceneColor * gain;

    vec3 result = mix(darkBase, litColor, totalLit);

    result += glow * 0.35 * (1.0 - selfLit);

    vec3  tonemapped = result / (1.0 + result * 0.35);
    float tonemapAmount = clamp(totalLit + max(max(glow.r, glow.g), glow.b), 0.0, 1.0);
    result = mix(result, tonemapped, tonemapAmount);

    fragColor = vec4(result, 1.0);
}