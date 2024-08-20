# Getting Started

## build process
### on x86 or x64 
docker-compose up -d --build

### on arm
docker login registry.priv-arch.duckdns.org

docker buildx build . --platform linux/arm64 --push --tag registry.priv-arch.duckdns.org/tvchannel:v2






