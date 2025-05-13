const fury = require('@furyjs/fury');
const fs = require('fs');
const path = require('path');

console.log("JavaScript Fury Implementation");

// Use the default export
const Fury = fury.default;
console.log("Using Fury version:", require('@furyjs/fury/package.json').version);

// Simple serialization of primitive types
try {
    // Create a new instance
    const furyInstance = new Fury();
    console.log("Created Fury instance");

    // Serialize a string
    const message = "Hello Fury from JavaScript";
    const serializedString = furyInstance.serialize(message);
    console.log(`Serialized string length: ${serializedString.length} bytes`);
    console.log(`Deserialized string: ${furyInstance.deserialize(serializedString)}`);

    // Serialize a number
    const number = 12345;
    const serializedNumber = furyInstance.serialize(number);
    console.log(`Serialized number: ${furyInstance.deserialize(serializedNumber)}`);

    // Create output directory
    const outputDir = path.join('..', 'hello-fury-io');
    if (!fs.existsSync(outputDir)) {
        fs.mkdirSync(outputDir, { recursive: true });
        console.log(`Created directory: ${outputDir}`);
    }

    // Write the serialized string to a file
    const outputFile = path.join(outputDir, 'message_js.fury');
    fs.writeFileSync(outputFile, Buffer.from(serializedString));
    console.log(`String data serialized to ${outputFile}`);

    console.log("\nJavaScript Fury example completed successfully");

} catch (error) {
    console.error("Error:", error);
}