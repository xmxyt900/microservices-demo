

#!/bin/sh
REQUEST_NUM=10000
CONCURRENT_NUM=100
ab -n $REQUEST_NUM -c $CONCURRENT_NUM http://52.243.83.52:8080/
