# Hello fory (JavaScript)

This example demonstrates using Apache fory with JavaScript.

## Prerequisites

- Node.js 20+
- npm 6+

## Setup Instructions

Apache Fory™ is NOT yet available on npm, please install from source code:

```bash
# Clone and build Fory from source (if not already done)
cd ..
git clone --depth 1 https://github.com/apache/fory.git fory-source
cd fory-source/javascript
npm install
npm run build

# Install dependencies in the example project
cd ../../hello-fory-js
npm install
```

## Run Instructions

```bash
# Run the example
node main.js
```

## Package Information

- Source: [Apache Fory JavaScript](https://github.com/apache/fory/tree/main/javascript)
- Package: @apache-fory/fory (installed from local source)
