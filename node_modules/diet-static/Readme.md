# diet-static
**Static file server for diet based on send.** A streaming static file server supporting partial responses (Ranges), conditional-GET negotiation, high test coverage, and granular events which may be leveraged to take appropriate actions in your application or framework.


## **Install**
```
npm install diet-static
```

## **Usage**

```js

// Initialize Server
var server = require('diet') // Require Diet
var app = server()           // Create App
app.listen(8000)             // Configure Domain

// Require diet-static
var static = require('diet-static')({ path: app.path+'/static' })

// Attach static as a global footer
app.footer(static);
```

Now your app will serve every URL with a file that has a mimeType and it's NOT associated with a custom route by `app.get` or `app.post` relative to the path configration.

```js
// http://localhost:8000/people.json		--> `/path/to/your_app/static/people.json`
// http://localhost:8000/favicon.ico		--> `/path/to/your_app/static/favicon.ico`
// http://localhost:8000/scripts/global.js	--> `/path/to/your_app/static/scripts/global.js`
// http://localhost:8000/styles/global.css	--> `/path/to/your_app/static/styles/global.css`
// http://localhost:8000/images/logo.png	--> `/path/to/your_app/static/images/logo.png`
```

## **Config**
Configs are optional for `diet-static`.
```js
require('diet-static')({
    path: app.path+'/static' // the folder to server files from
})
```

## **License**

(The MIT License)

Copyright (c) 2014 Halász Ádám <mail@adamhalasz.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.