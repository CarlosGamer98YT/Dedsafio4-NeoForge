package com.eric.revill.d.e;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class AdminGuideScreen extends Screen {
   private static final String[][] PAGES = new String[][]{
      {
            "PRIMEROS PASOS",
            "Esta guia resume la configuracion",
            "del servidor Dedsafio4.",
            "",
            "Solo operadores nivel 4 pueden",
            "usar las herramientas administrativas.",
            "",
            "/dedsafio4 abre el panel principal.",
            "Pulsa M para abrir Misiones.",
            "",
            "El libro se entrega al entrar como",
            "OP4. Tambien esta en el creativo",
            "del mod y puedes descartarlo."
      },
      {
            "CREAR Y EDITAR MISIONES",
            "Abre Misiones con M. En la pantalla",
            "de misiones usa Crear o Editar.",
            "",
            "ID: identificador unico (ej. matar_1).",
            "Tipo: KILL, COLLECT o BREAK.",
            "Objetivo: ID, ej. minecraft:zombie.",
            "Cantidad: objetivo necesario.",
            "Recompensa: deditas azules.",
            "Dia: numero 1-99 de mision.",
            "Esencial: entrega una dedita rosa",
            "marcada con ese dia al completarla.",
            "Puedes dar tambien un objeto."
      },
      {
            "PROGRESO Y PREMIOS",
            "El servidor cuenta automaticamente",
            "muertes, objetos recogidos y bloques",
            "rotos que coincidan con el objetivo.",
            "",
            "La mision se completa al alcanzar",
            "la cantidad requerida y sus premios",
            "se entregan una sola vez.",
            "",
            "En la pestana Jugadores puedes",
            "consultar y reiniciar el progreso.",
            "Los cambios se guardan en el servidor",
            "y se sincronizan con los jugadores."
      },
      {
            "PEDESTAL DIARIO",
            "Coloca el pedestal en creativo como",
            "OP4 para configurarlo. Asigna el dia",
            "(1-99) y coloca hasta 21 objetos",
            "de premio en sus ranuras.",
            "",
            "Guarda el dia con el boton OK.",
            "Al completar una mision esencial,",
            "el jugador recibe una dedita rosa",
            "con el dia configurado en la mision.",
            "",
            "Al usarla en un pedestal del mismo",
            "dia, los premios llegan por correo."
      },
      {
            "EVENTOS Y PANEL",
            "/revill start | stop | status",
            "/dedsafio4 start | stop | status",
            "/dedsafio4 announce <mensaje>",
            "/dedsafio4 color <color|random>",
            "Colores: roja, verde, naranja, rosada.",
            "/dedsafio4 fusion [entrar|salir]",
            "",
            "El panel de /dedsafio4 permite",
            "administrar cambios, ruletas y",
            "ajustes de REVILL. Los permisos se",
            "validan tambien en el servidor."
      },
      {
            "UTILIDADES",
            "/dedsafio4 bunker set <x1> <y1> <z1>",
            "  <x2> <y2> <z2>",
            "/dedsafio4 bunker show | clear",
            "/dedsafio4 meteorites on | off",
            "/dedsafio4 config",
            "/dedsafio4 waypoint add <nombre>",
            "/dedsafio4 waypoint remove <id>",
            "/dedsafio4 revive [jugador] [modo]",
            "/dedsafio4 eon <jugador>",
            "",
            "El autocompletado del chat muestra",
            "los argumentos validos disponibles."
      },
      {
            "DEDITAS Y CONSEJOS",
            "/dedsafio4 coins see <jugador>",
            "/dedsafio4 coins add <jugador>",
            "  <azul|roja|verde> <cantidad>",
            "/dedsafio4 coins remove <jugador>",
            "  <azul|roja|verde> <cantidad>",
            "",
            "Usa minecraft:<objeto> como ID de",
            "objetivo, icono o recompensa.",
            "Revisa el progreso desde la UI",
            "y prueba cada dia del pedestal.",
            "",
            "Todos los cambios administrativos",
            "requieren nivel OP 4."
      }
   };
   private int page;

   public AdminGuideScreen() {
      super(Component.literal("Administrador de Dedsafio4"));
   }

   protected void init() {
      int left = this.width / 2 - 145;
      int bottom = this.height / 2 + 118;
      this.addRenderableWidget(
         Button.builder(Component.literal("Anterior"), button -> this.page = Math.max(0, this.page - 1)).bounds(left + 12, bottom, 76, 20).build()
      );
      this.addRenderableWidget(
         Button.builder(Component.literal("Siguiente"), button -> this.page = Math.min(PAGES.length - 1, this.page + 1))
            .bounds(left + 92, bottom, 86, 20)
            .build()
      );
      this.addRenderableWidget(Button.builder(Component.literal("Cerrar"), button -> this.onClose()).bounds(left + 205, bottom, 76, 20).build());
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      this.renderBackground(context, mouseX, mouseY, delta);
      int left = this.width / 2 - 145;
      int top = this.height / 2 - 125;
      context.fill(left, top, left + 290, top + 270, -12966374);
      context.fill(left + 5, top + 5, left + 285, top + 265, -993112);
      context.fill(left + 10, top + 10, left + 280, top + 33, -7576772);
      context.drawCenteredString(this.font, this.title, this.width / 2, top + 17, -1);
      String[] lines = PAGES[this.page];
      int y = top + 43;

      for (int index = 0; index < lines.length; index++) {
         String line = lines[index];
         if (line.isEmpty()) {
            y += 4;
         } else {
            int color = index == 0 ? -9419488 : -13753061;
            context.drawString(this.font, line, left + 14, y, color, false);
            y += 12;
         }
      }

      context.drawCenteredString(this.font, Component.literal(this.page + 1 + " / " + PAGES.length), this.width / 2, top + 247, -9419488);
      super.render(context, mouseX, mouseY, delta);
   }

   public boolean isPauseScreen() {
      return false;
   }
}
