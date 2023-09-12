# TerraTile Proxy Server

## Installation
1. Ensure you have Node.js
2. Run `npm install`
3. Run `node index.js` to start the server

## How it works
XYZ tile servers often have 3 servers, named a, b and c to ensure reliability. 
Clients typically also cache the png images for efficiency. When TerraTile loads
the map, it requests 12 tiles to this proxy server. Also, I don't know how to program
timeouts since Fabric doesn't support multithreading.