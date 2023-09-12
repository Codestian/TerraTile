const express = require('express');
const request = require('request');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = 3000;
const timeoutDuration = 5000;

const cacheFolder = 'cache';

// Ensure the cache folder exists
fs.mkdirSync(cacheFolder, { recursive: true });

app.get('/', (clientReq, clientRes) => {
    return 'Proxy server online.';
});

app.get('/:zoom/:x/:y', (clientReq, clientRes) => {

  // Retrieve parameters.
  const zoom = clientReq.params.zoom;
  const x = clientReq.params.x;
  const y = clientReq.params.y;

  // Generate cache name.
  const cacheKey = `${zoom}-${x}-${y}`;
  const cacheFilePath = path.join(cacheFolder, `${cacheKey}.png`);

  // Check if the cached image file exists on the local filesystem.
  if (fs.existsSync(cacheFilePath)) {
    // Serve the image from the disk cache.
    console.log('Retrieved from disk cache:', cacheKey);
    const imageStream = fs.createReadStream(cacheFilePath);
    imageStream.pipe(clientRes);
  } else {

    // Change this to your map provider. Refer to their documentation for url, should be something under 'XYZ tiles'.
    // XYZ tiles typically have 3 servers, a, b and c for reliability.
    const mapUrlA = `https://maps-a.onemap.sg/v3/Satellite/${zoom}/${x}/${y}.png`;
    const mapUrlB = `https://maps-b.onemap.sg/v3/Satellite/${zoom}/${x}/${y}.png`;
    const mapUrlC = `https://maps-c.onemap.sg/v3/Satellite/${zoom}/${x}/${y}.png`;

    console.log('Attempting to retrieve from A server:', mapUrlA);

    // Attempt to retrieve tile images from the urls. Will move on to next server if no response after 5 seconds.
    request.get(mapUrlA, { timeout: timeoutDuration, encoding: null }, (errorA, responseA, bodyA) => {
      if (!errorA && responseA.statusCode === 200) {

        // Cache the image data to disk.
        fs.writeFileSync(cacheFilePath, bodyA);
        console.log('Cached:', cacheKey);

        // Set the Content-Type header to indicate it's a PNG image.
        clientRes.set('Content-Type', 'image/png');

        // Send the image data as the response
        clientRes.status(200).send(bodyA);

      } else {

        // If A server fails, attempt to retrieve from B server.
        console.log('Failed to retrieve from A server. Attempting B server:', mapUrlB);

        request.get(mapUrlB, { timeout: timeoutDuration, encoding: null }, (errorB, responseB, bodyB) => {
          if (!errorB && responseB.statusCode === 200) {

            // Cache the image data to disk.
            fs.writeFileSync(cacheFilePath, bodyB);
            console.log('Cached:', cacheKey);

            // Set the Content-Type header to indicate it's a PNG image.
            clientRes.set('Content-Type', 'image/png');

            // Send the image data as the response.
            clientRes.status(200).send(bodyB);

          } else {
            // If maps-b.onemap.sg fails, attempt to retrieve from C server.
            console.log('Failed to retrieve from B server. Attempting C server:', mapUrlC);
            request.get(mapUrlC, { timeout: timeoutDuration, encoding: null }, (errorC, responseC, bodyC) => {
              if (!errorC && responseC.statusCode === 200) {

                // Cache the image data to disk.
                fs.writeFileSync(cacheFilePath, bodyC);
                console.log('Cached:', cacheKey);

                // Set the Content-Type header to indicate it's a PNG image.
                clientRes.set('Content-Type', 'image/png');

                // Send the image data as the response.
                clientRes.status(200).send(bodyC);

              } else {

                // If all attempts fail, return an error response.
                console.log('Failed to retrieve from C server. All attempts failed. GG.');
                clientRes.status(500).send('Failed to retrieve map tile! Please try again.');

              }
            });
          }
        });
      }
    });
  }
});

// Start the server.
app.listen(PORT, () => {
  console.log(`Proxy server is running on port ${PORT}`);
});
