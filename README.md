## Explanation

Script to pull optimal route from a to b. Great to spin into a script for getting data on well known paths.  
eg. daily commute.  

This way, you can run it without opening your phone/ leaving your work station.  

## Run 
```
mvn clean package exec:java -Dexec.mainClass=com.kpsl.java_google_api.Main -Dmaps.key=[#key] -Do=[#origin] -Dd=[#destination]
```
where:  
origin is       - a lat,lng for your starting point  
destination is  - a lat,lng for your end point  
key is          - a google maps api key. [Get for free at](https://console.cloud.google.com/project/_/google/maps-apis)
