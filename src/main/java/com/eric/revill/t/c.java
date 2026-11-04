package com.eric.revill.t;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public final class c extends Entity {
   private static final EntityDataAccessor<String> a = SynchedEntityData.defineId(c.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<String> b = SynchedEntityData.defineId(c.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<String> c = SynchedEntityData.defineId(c.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<Integer> d = SynchedEntityData.defineId(c.class, EntityDataSerializers.INT);

   public c(EntityType<? extends c> type, Level world) {
      super(type, world);
      this.setNoGravity(true);
   }

   public void a(a waypoint) {
      this.entityData.set(a, waypoint.a());
      this.entityData.set(b, waypoint.b());
      this.entityData.set(c, waypoint.d());
      this.entityData.set(d, waypoint.e());
      this.setPos((double)waypoint.c().getX() + 0.5, (double)waypoint.c().getY() + 1.0, (double)waypoint.c().getZ() + 0.5);
   }

   public String a() {
      return (String)this.entityData.get(a);
   }

   public String b() {
      return (String)this.entityData.get(b);
   }

   public String c() {
      return (String)this.entityData.get(c);
   }

   public int d() {
      return (Integer)this.entityData.get(d);
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      builder.define(a, "");
      builder.define(b, "");
      builder.define(c, "");
      builder.define(d, -22016);
   }

   protected void readAdditionalSaveData(CompoundTag nbt) {
      this.entityData.set(a, nbt.getString("WaypointId"));
      this.entityData.set(b, nbt.getString("WaypointName"));
      this.entityData.set(c, nbt.getString("WaypointIcon"));
      this.entityData.set(d, nbt.getInt("WaypointColor"));
   }

   protected void addAdditionalSaveData(CompoundTag nbt) {
      nbt.putString("WaypointId", this.a());
      nbt.putString("WaypointName", this.b());
      nbt.putString("WaypointIcon", this.c());
      nbt.putInt("WaypointColor", this.d());
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public boolean isPushable() {
      return false;
   }
}
