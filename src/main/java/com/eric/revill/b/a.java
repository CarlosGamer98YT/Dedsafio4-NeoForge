package com.eric.revill.b;

import net.minecraft.core.BlockPos;

/**
 * Representa una zona cúbica delimitada (caja de colisión 3D / AABB) para el Búnker.
 * 
 * Guarda la dimensión donde se encuentra y las coordenadas de dos esquinas opuestas
 * (esquina 1 y esquina 2). Ofrece métodos para comprobar si una posición o un jugador
 * se encuentran dentro del área protegida del búnker.
 */
public class a {
   /** Identificador de la dimensión del búnker (ej. "minecraft:overworld"). */
   private String a = "";

   /** Primera esquina opuesta de la zona del búnker. */
   private BlockPos b;

   /** Segunda esquina opuesta de la zona del búnker. */
   private BlockPos c;

   /**
    * Comprueba si el búnker está debidamente configurado con una dimensión válida
    * y ambas esquinas asignadas.
    * 
    * @return true si el búnker está listo para usarse.
    */
   public boolean a() {
      return this.a != null && !this.a.isEmpty() && this.b != null && this.c != null;
   }

   /**
    * Verifica si una coordenada cúbica dada está dentro del volumen del búnker.
    * 
    * @param pos Posición de bloque a comprobar.
    * @return true si la posición está contenida dentro de los límites del búnker.
    */
   public boolean a(BlockPos pos) {
      if (this.a() && pos != null) {
         int minX = Math.min(this.b.getX(), this.c.getX());
         int maxX = Math.max(this.b.getX(), this.c.getX());
         int minY = Math.min(this.b.getY(), this.c.getY());
         int maxY = Math.max(this.b.getY(), this.c.getY());
         int minZ = Math.min(this.b.getZ(), this.c.getZ());
         int maxZ = Math.max(this.b.getZ(), this.c.getZ());
         return pos.getX() >= minX
            && pos.getX() <= maxX
            && pos.getY() >= minY
            && pos.getY() <= maxY
            && pos.getZ() >= minZ
            && pos.getZ() <= maxZ;
      } else {
         return false;
      }
   }

   /** Obtiene la dimensión del búnker. */
   public String b() {
      return this.a;
   }

   /** Establece la dimensión del búnker. */
   public void a(String dimension) {
      this.a = dimension;
   }

   /** Obtiene la primera esquina del búnker. */
   public BlockPos c() {
      return this.b;
   }

   /** Establece la primera esquina del búnker. */
   public void b(BlockPos firstCorner) {
      this.b = firstCorner;
   }

   /** Obtiene la segunda esquina del búnker. */
   public BlockPos d() {
      return this.c;
   }

   /** Establece la segunda esquina del búnker. */
   public void c(BlockPos secondCorner) {
      this.c = secondCorner;
   }
}
