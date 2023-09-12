# TerraTile

A simple Minecraft mod for BuildTheEarth.

## What does it do?

TerraTile renders map tile images onto the Minecraft world to speed up
outlining buildings. This effectively reduces the need for to tpll corners
of buildings, which may be inaccurate. This mod is very similar to BTETerraRenderer,
but it is made for 1.20+ Fabric and very simple to use.

## What map provider does it use?

Due to TOS, Google Maps is not permitted to be used with this mod. By default, Singapore's OneMap
is used. However, it is only limited to the Singapore Region. You can try OpenStreetMap, but do note
its inaccuracy in outlines. Fork this repo to change the map provider.

## Usage

### Start the proxy server
Ensure you have Node.js installed. Follow the instructions in the `/proxy` folder to
start the server. Do note it caches the map images, which may take up space.

### Setup location
First, you need to set the location of where you are going to build.
Pick the LatLng in Google Maps and enter `/terratile select $LAT $LNG`, 
where $LAT and $LNG are the coordinates.

### Controls

Note: The map has already been calibrated to BTE scale and rotation. Adjust if necessary.

- P
  - Toggle rendering of map
- U, H, J, K
  - Move the map on the X/Z plane
- Y, I
  - Move the map on the Y plane
- G, B
  - Scales the map
- N, M
  - Rotates the map


