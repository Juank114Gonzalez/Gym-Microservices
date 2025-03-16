#!/bin/bash

SPRINGBOOT=("entrenadores" "equipos" "miembros" "clases")
for PROYECT in "${SPRINGBOOT[@]}"
do
if [ -d "$PROYECT" ]; then
echo "Starting service $PROYECT..."
gnome-terminal -- bash -c "cd $PROYECT && ./mvnw spring-boot:run; exec bash"
else
echo "$PROYECT not found..."
fi
done
echo "All services have been launched"

