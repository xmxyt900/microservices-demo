
#!/bin/bash
sudo docker run -ti -d --name=recommendation_engine -v DataVolume:/DataVolume ubuntu
sudo docker exec recommendation_engine /bin/sh -c "echo 'This file is shared between containers' > /DataVolume/data.txt; apt-get update; apt-get install stress; stress --cpu 1 --timeout 120"
