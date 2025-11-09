const fory = require('@foryjs/fory');
const fs = require('fs');
const path = require('path');

console.log("JavaScript fory Implementation");

// Use the default export
const fory = fory.default;
console.log("Using fory version:", require('@foryjs/fory/package.json').version);

// Simple serialization of primitive types
try {
    // Create a new instance
    const foryInstance = new fory();
    console.log("Created fory instance");

    // Serialize a string
    const message = "Hello fory from JavaScript";
    const serializedString = foryInstance.serialize(message);
    console.log(`Serialized string length: ${serializedString.length} bytes`);
    console.log(`Deserialized string: ${foryInstance.deserialize(serializedString)}`);

    // Serialize a number
    const number = 12345;
    const serializedNumber = foryInstance.serialize(number);
    console.log(`Serialized number: ${foryInstance.deserialize(serializedNumber)}`);

    // Create output directory
    const outputDir = path.join('..', 'hello-fory-io');
    if (!fs.existsSync(outputDir)) {
        fs.mkdirSync(outputDir, { recursive: true });
        console.log(`Created directory: ${outputDir}`);
    }

    // Write the serialized string to a file
    const outputFile = path.join(outputDir, 'message_js.fory');
    fs.writeFileSync(outputFile, Buffer.from(serializedString));
    console.log(`String data serialized to ${outputFile}`);

    console.log("\nJavaScript fory example completed successfully");

} catch (error) {
    console.error("Error:", error);
}