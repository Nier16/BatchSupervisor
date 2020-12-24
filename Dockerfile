ARG FROM_VERSION=latest
FROM harbor.krec/base/springboot_openjre-11:${FROM_VERSION}
# Redefine ARG after FROM to use it (in a label) https://docs.docker.com/engine/reference/builder/#understand-how-arg-and-from-interact
ARG FROM_VERSION

# Components versions and constants

# Labels	
LABEL maintainer="Espace Tiers" \
    code_cassini="A1618" \
    app_name="xib espace tiers backend" \
    from_springboot_openjdk-11_image_version="$FROM_VERSION"

# Si on ne veut pas le nom par defaut app.jar
ENV ARTIFACT_NAME=xib.jar

# HEALTHCHECK Warning : url must answer http 200, else the orchestrator will kill your container to start a new one
HEALTHCHECK --start-period=30s --interval=30s CMD wget --quiet --tries=1 -S --spider http://localhost:8080/ || exit 1

# Copy application jar
COPY ["target/xib.jar", "$APP_HOME/$ARTIFACT_NAME" ]
