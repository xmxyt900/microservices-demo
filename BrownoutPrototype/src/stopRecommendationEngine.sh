
#!/bin/bash
sudo docker stop recommendation_engine
sudo docker rm recommendation_engine
sudo docker volume rm DataVolume
