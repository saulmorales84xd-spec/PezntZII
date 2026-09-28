package net.saullmc.pezntz.capability;

import net.minecraft.nbt.CompoundTag;

public class BodyHealthData {

    public static final float MAX_HEALTH = 6.0f;

    public static final int DOWNED_DURATION_TICKS = 2400;

    public static final float HEAL_QUICK = 2.0f;

    public static final float HEAL_GUI = 4.0f;

    private float head = MAX_HEALTH, body = MAX_HEALTH, arms = MAX_HEALTH, legs = MAX_HEALTH;

    private float bonusHead = 0.0f, bonusBody = 0.0f, bonusArms = 0.0f, bonusLegs = 0.0f;

    private boolean isDowned = false;
    private int downedTimer = 0;
    private int reviveProgress = 0;

    public float getHead() { return head; }
    public float getBody() { return body; }
    public float getArms() { return arms; }
    public float getLegs() { return legs; }

    public float getMaxHead() { return MAX_HEALTH + bonusHead; }
    public float getMaxBody() { return MAX_HEALTH + bonusBody; }
    public float getMaxArms() { return MAX_HEALTH + bonusArms; }
    public float getMaxLegs() { return MAX_HEALTH + bonusLegs; }

    public float getMax(String part) {
        return switch (part) {
            case "head" -> getMaxHead();
            case "body" -> getMaxBody();
            case "arms" -> getMaxArms();
            case "legs" -> getMaxLegs();
            default -> MAX_HEALTH;
        };
    }

    public boolean hayAlgunaHerida() {
        return head < getMaxHead() || body < getMaxBody() || arms < getMaxArms() || legs < getMaxLegs();
    }

    public void llenarTodo() {
        this.head = getMaxHead();
        this.body = getMaxBody();
        this.arms = getMaxArms();
        this.legs = getMaxLegs();
    }

    public boolean isDowned() { return isDowned; }
    public int getDownedTimer() { return downedTimer; }
    public int getReviveProgress() { return reviveProgress; }

    public void setHead(float health) { this.head = Math.max(0, Math.min(health, getMaxHead())); }
    public void setBody(float health) { this.body = Math.max(0, Math.min(health, getMaxBody())); }
    public void setArms(float health) { this.arms = Math.max(0, Math.min(health, getMaxArms())); }
    public void setLegs(float health) { this.legs = Math.max(0, Math.min(health, getMaxLegs())); }

    public boolean setBonuses(float cabeza, float cuerpo, float brazos, float piernas) {
        if (cabeza == bonusHead && cuerpo == bonusBody && brazos == bonusArms && piernas == bonusLegs) {
            return false;
        }

        this.bonusHead = cabeza;
        this.bonusBody = cuerpo;
        this.bonusArms = brazos;
        this.bonusLegs = piernas;

        this.head = Math.min(this.head, getMaxHead());
        this.body = Math.min(this.body, getMaxBody());
        this.arms = Math.min(this.arms, getMaxArms());
        this.legs = Math.min(this.legs, getMaxLegs());

        return true;
    }

    public void setDowned(boolean downed) { this.isDowned = downed; }
    public void setDownedTimer(int timer) { this.downedTimer = Math.max(0, timer); }
    public void setReviveProgress(int progress) { this.reviveProgress = Math.max(0, progress); }

    public float damagePart(String part, float amount) {
        float currentHealth = 0;
        switch (part) {
            case "head" -> currentHealth = this.head;
            case "body" -> currentHealth = this.body;
            case "arms" -> currentHealth = this.arms;
            case "legs" -> currentHealth = this.legs;
        }

        float absorbedDamage = Math.min(amount, currentHealth);
        float remainingDamage = amount - absorbedDamage;

        switch (part) {
            case "head" -> setHead(this.head - absorbedDamage);
            case "body" -> setBody(this.body - absorbedDamage);
            case "arms" -> setArms(this.arms - absorbedDamage);
            case "legs" -> setLegs(this.legs - absorbedDamage);
        }
        return remainingDamage;
    }

    public void copyFrom(BodyHealthData source) {
        this.head = source.head; this.body = source.body;
        this.arms = source.arms; this.legs = source.legs;
        this.isDowned = source.isDowned;
        this.downedTimer = source.downedTimer;
        this.reviveProgress = source.reviveProgress;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("head_health", head); nbt.putFloat("body_health", body);
        nbt.putFloat("arms_health", arms); nbt.putFloat("legs_health", legs);
        nbt.putBoolean("is_downed", isDowned);
        nbt.putInt("downed_timer", downedTimer);
        nbt.putInt("revive_progress", reviveProgress);
    }

    public void loadNBTData(CompoundTag nbt) {
        head = nbt.getFloat("head_health"); body = nbt.getFloat("body_health");
        arms = nbt.getFloat("arms_health"); legs = nbt.getFloat("legs_health");
        isDowned = nbt.getBoolean("is_downed");
        downedTimer = nbt.getInt("downed_timer");
        reviveProgress = nbt.getInt("revive_progress");
    }
}