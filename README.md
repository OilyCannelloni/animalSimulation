# Animal Simulation

This repo contains the first project of AGH OOP course.
Wiki: https://github.com/apohllo/obiektowe-lab/blob/master/proj1/

## Installation

 - Clone this repository or download a release
 - Execute `./gradlew run` from the terminal

## Configuration
![input prompt](./img/inputPrompt.PNG?raw=true)
From the configuration screen you can:
 - Add up to 10 maps which are ran independently.
 - Each map can be one of two types: standard or with wrapped edges
 - Adjust map dimensions from (10, 10) to (100, 30)
 - Set jungle area as a percentage of map area. The shape of the jungle is the same as the map's, just smaller.
 - Set respawn rules, so that the simulation does not die out.
 - Set energy parameters 

## Simulation

![simulation](./img/simulation.PNG?raw=true)

 While the simulation is running, you can:
 - Toggle between all defined maps using the red bar on the left.
 - Pause each map independently.
 - When the simulation is paused, highlight all animals with dominant genome.
 - Save statistics to a .csv file.
 - Toggle between several plot types.
 - Select an animal and watch its statistics live.
 
