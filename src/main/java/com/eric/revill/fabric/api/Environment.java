package com.eric.revill.fabric.api;

/**
 * Anotación emulada de entorno de ejecución de Fabric API.
 * 
 * Permite marcar tipos, métodos y campos restringidos a un entorno concreto ({@link EnvType#CLIENT}).
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Environment {
    EnvType value();
}
