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

gnome-terminal -- bash -c "cd miembros && ./mvnw spring-boot:run -Dspring-boot.run.arguments='--server.port=9081'"
gnome-terminal -- bash -c "cd equipos && ./mvnw spring-boot:run -Dspring-boot.run.arguments='--server.port=9082'"

echo "All services have been launched"

