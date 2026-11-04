package com.eric.revill.j;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class b extends SavedData {
   public static final SavedData.Factory<com.eric.revill.j.b> FACTORY = new SavedData.Factory<>(com.eric.revill.j.b::new, com.eric.revill.j.b::a);
   private boolean a;
   private int b;
   private boolean c;
   private boolean d;
   private String e = com.eric.revill.j.c.a.b();
   private com.eric.revill.b.a f = new com.eric.revill.b.a();
   private int g;
   private String h = "";

   public static b a(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
      b state = new b();
      state.a = nbt.getBoolean("active");
      state.b = nbt.getInt("remainingSeconds");
      state.c = nbt.getBoolean("skyActive");
      state.d = nbt.getBoolean("announcementActive");
      state.e = nbt.contains("taskId") ? nbt.getString("taskId") : com.eric.revill.j.c.a.b();
      state.g = nbt.getInt("meteoritesActive");
      state.h = nbt.getString("worldKey");
      com.eric.revill.b.a bunker = new com.eric.revill.b.a();
      bunker.a(nbt.getString("bunkerDimension"));
      if (nbt.contains("bunkerPos1")) {
         CompoundTag pos1 = nbt.getCompound("bunkerPos1");
         bunker.b(new BlockPos(pos1.getInt("x"), pos1.getInt("y"), pos1.getInt("z")));
      }

      if (nbt.contains("bunkerPos2")) {
         CompoundTag pos2 = nbt.getCompound("bunkerPos2");
         bunker.c(new BlockPos(pos2.getInt("x"), pos2.getInt("y"), pos2.getInt("z")));
      }

      state.f = bunker;
      return state;
   }

   @Override
   public CompoundTag save(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
      nbt.putBoolean("active", this.a);
      nbt.putInt("remainingSeconds", this.b);
      nbt.putBoolean("skyActive", this.c);
      nbt.putBoolean("announcementActive", this.d);
      nbt.putString("taskId", this.e);
      nbt.putInt("meteoritesActive", this.g);
      nbt.putString("worldKey", this.h);
      nbt.putString("bunkerDimension", this.f.b());
      if (this.f.c() != null) {
         BlockPos pos = this.f.c();
         CompoundTag pos1 = new CompoundTag();
         pos1.putInt("x", pos.getX());
         pos1.putInt("y", pos.getY());
         pos1.putInt("z", pos.getZ());
         nbt.put("bunkerPos1", pos1);
      }

      if (this.f.d() != null) {
         BlockPos pos = this.f.d();
         CompoundTag pos2 = new CompoundTag();
         pos2.putInt("x", pos.getX());
         pos2.putInt("y", pos.getY());
         pos2.putInt("z", pos.getZ());
         nbt.put("bunkerPos2", pos2);
      }

      return nbt;
   }

   public boolean a() {
      return this.a;
   }

   public void a(boolean active) {
      this.a = active;
      this.setDirty();
   }

   public int b() {
      return this.b;
   }

   public void a(int remainingSeconds) {
      this.b = remainingSeconds;
      this.setDirty();
   }

   public boolean c() {
      return this.c;
   }

   public void b(boolean skyActive) {
      this.c = skyActive;
      this.setDirty();
   }

   public boolean d() {
      return this.d;
   }

   public void c(boolean announcementActive) {
      this.d = announcementActive;
      this.setDirty();
   }

   public String e() {
      return this.e;
   }

   public void a(String taskId) {
      this.e = com.eric.revill.j.c.d(taskId).b();
      this.setDirty();
   }

   public com.eric.revill.b.a f() {
      return this.f;
   }

   public void a(com.eric.revill.b.a bunker) {
      this.f = bunker;
      this.setDirty();
   }

   public int g() {
      return this.g;
   }

   public void b(int meteoritesActive) {
      this.g = meteoritesActive;
      this.setDirty();
   }

   public String h() {
      return this.h;
   }

   public void b(String worldKey) {
      this.h = worldKey;
      this.setDirty();
   }
}
