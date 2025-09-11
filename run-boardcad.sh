#!/bin/sh
exec /app/openjdk17/bin/java --add-exports java.desktop/sun.awt=ALL-UNNAMED -jar /app/BoardCAD.jar "$@"
