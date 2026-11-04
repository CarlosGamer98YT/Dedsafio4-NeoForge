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

public final class a {
   public static final Gson a = new GsonBuilder().setPrettyPrinting().create();
   public static final Path b = FMLPaths.CONFIGDIR.get().resolve("revill.json");
   public boolean c = true;
   public int d = 600;
   public boolean e = true;
   public boolean f = false;
   public List<String> g = new ArrayList<>();
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
   public boolean revillMusicEnabled = true;
   public Map<String, String> revillTaskMessages = new HashMap<>();

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

   public void b() {
      try {
         Files.createDirectories(b.getParent());

         try (BufferedWriter writer = Files.newBufferedWriter(b)) {
            a.toJson(this, writer);
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }
}
