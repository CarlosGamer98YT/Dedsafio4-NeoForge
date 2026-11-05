package com.eric.revill.f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.neoforged.fml.loading.FMLPaths;

/**
 * Gestor y modelo de configuración de DEDSAFIO 4 Mechanics (archivo {@code config/revill.json}).
 * 
 * Contiene todas las opciones configurables del mod:
 * - Duración del evento REVILL.
 * - Habilitación de meteoritos y destrucción de bloques.
 * - Dimensiones afectadas por el evento.
 * - Música personalizada durante el evento.
 * - Mensajes específicos por tarea de REVILL.
 */
public final class a {
   /** Instancia de Gson configurada con formateo legible (pretty printing). */
   public static final Gson a = new GsonBuilder().setPrettyPrinting().create();

   /** Ruta absoluta del archivo config/revill.json. */
   public static final Path b = FMLPaths.CONFIGDIR.get().resolve("revill.json");

   /** Indica si el sistema REVILL está activo. */
   public boolean c = true;

   /** Duración del evento REVILL en segundos (por defecto 600 s = 10 minutos). */
   public int d = 600;

   /** Si la lluvia de meteoritos está activada durante el evento. */
   public boolean e = true;

   /** Si los meteoritos causan daño por explosión y destruyen bloques del mundo. */
   public boolean f = false;

   /** Lista de dimensiones afectadas por el evento (ej. "minecraft:overworld"). */
   public List<String> g = new ArrayList<>();

   /** Si los anuncios en pantalla están activados. */
   public boolean h = true;

   public int i = 10;
   public boolean j = true;
   public boolean k = true;
   public double l = 1.5;
   public boolean m = true;
   public boolean n = true;
   public int o = 20;
   public int p = 80;
   public int q = 180;
   public boolean r = true;
   public double s = 6.0;
   public boolean t = true;
   public boolean u = false;
   public int v = 12;
   public boolean w = true;

   /** Si la música personalizada de REVILL suena durante el evento. */
   public boolean revillMusicEnabled = true;

   /** Mensajes personalizados asociados a cada tipo de tarea del evento. */
   public Map<String, String> revillTaskMessages = new HashMap<>();

   /**
    * Carga la configuración desde config/revill.json o genera una nueva si no existe.
    * 
    * @return Instancia cargada de la configuración.
    */
   public static a a() {
      if (!Files.exists(b)) {
         a config = new a();
         config.b();
         return config;
      } else {
         try {
            a var2;
            try (BufferedReader reader = Files.newBufferedReader(b)) {
               a config = (a)a.fromJson(reader, a.class);
               if (config == null) {
                  config = new a();
               }

               config.b();
               var2 = config;
            }

            return var2;
         } catch (IOException var5) {
            var5.printStackTrace();
            a config = new a();
            config.b();
            return config;
         }
      }
   }

   /**
    * Guarda la configuración actual en el archivo config/revill.json.
    */
   public void b() {
      try {
         Files.createDirectories(b.getParent());

         try (BufferedWriter writer = Files.newBufferedWriter(b)) {
            a.toJson(this, writer);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }
   }
}
