docker rm $(docker ps -a -q)
docker rmi $(docker images -a -q)
docker system prune -f
docker volume prune -f
docker network prune -f