const Fory = require('@apache-fory/fory').default;
const { Type } = require('@apache-fory/fory');
const fs = require('fs');
const path = require('path');

// Define UserInfo type using Type.struct
const UserInfoType = Type.struct("org.feuyeux.fory.UserInfo", {
    userId: Type.int64(),
    name: Type.string(),
    age: Type.int32(),
    emails: Type.array(Type.string()),
    properties: Type.map(Type.string(), Type.string()),
    scores: Type.array(Type.int32()),
    active: Type.bool(),
    createdAt: Type.int64(),
    avatar: Type.binary()
});

function createSampleUserInfo() {
    return {
        userId: 12345,
        name: "John Doe",
        age: 30,
        emails: ["john.doe@example.com", "johndoe@gmail.com"],
        properties: new Map([
            ["city", "San Francisco"],
            ["country", "USA"],
            ["role", "Developer"]
        ]),
        scores: [85, 90, 78, 92],
        active: true,
        createdAt: Math.floor(Date.now() / 1000),
        avatar: new Uint8Array([0, 1, 2, 3, 4, 5])
    };
}

function deserializeUser(deserialize, filePath) {
    const serialized = fs.readFileSync(filePath);
    const userInfo = deserialize(serialized);
    return userInfo;
}

function main() {
    try {
        // Create output directory if it doesn't exist
        const outputDir = path.join('..', 'workspace');
        if (!fs.existsSync(outputDir)) {
            fs.mkdirSync(outputDir, { recursive: true });
        }

        // Create a sample UserInfo object
        const userInfo = createSampleUserInfo();
        console.log("Created user:", userInfo);

        // Create fory instance configured for cross-language compatibility
        const fory = new Fory({ refTracking: true });
        const { serialize, deserialize } = fory.registerSerializer(UserInfoType);

        // Serialize the user to bytes
        const serialized = serialize(userInfo);

        // Write the serialized data to a file
        const outputFile = path.join(outputDir, 'userinfo_js.fory');
        fs.writeFileSync(outputFile, Buffer.from(serialized));
        console.log(`Serialized to ${outputFile}`);

        // Try to read serialized data from other languages
        const langs = ["java", "go", "js", "python", "rust"];
        for (const lang of langs) {
            const langFile = path.join(outputDir, `userinfo_${lang}.fory`);
            if (fs.existsSync(langFile)) {
                try {
                    const userInfoFromFile = deserializeUser(deserialize, langFile);
                    console.log(`Deserialized [${lang}] user info from ${langFile}:`, userInfoFromFile);
                } catch (error) {
                    console.log(`Failed to deserialize [${lang}]: ${error.message}`);
                }
            }
        }

    } catch (error) {
        console.error("Error:", error);
        console.error(error.stack);
        process.exit(1);
    }
}

main();