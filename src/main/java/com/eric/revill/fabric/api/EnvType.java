package com.eric.revill.fabric.api;

/**
 * Enumerador emulado de entorno de ejecución de Fabric API.
 * 
 * Define si el código se ejecuta en el cliente ({@link #CLIENT}) o en un servidor dedicado ({@link #SERVER}).
 */

public enum EnvType {
    CLIENT,
    SERVER
}
