# Getting Started

## env
in .env -> STREAM_STORAGE add the path where the videos will be saved

## build process
### on x86 or x64 
docker-compose up -d --build

### on arm
docker login registry.priv-arch.duckdns.org

docker buildx build . --platform linux/arm64 --push --tag registry.priv-arch.duckdns.org/tvchannel:v2


## Demo
### url: http://localhost:3000/
### Swagger url: http://localhost:3000/webjars/swagger-ui/index.html



