use fory::{Error, Fory};
use fory::ForyObject;
use std::collections::HashMap;
use std::fs;
use std::path::Path;
use std::time::{SystemTime, UNIX_EPOCH};

#[derive(ForyObject, Debug, Clone)]
struct UserInfo {
    user_id: Option<i64>,
    name: Option<String>,
    age: Option<i32>,
    emails: Option<Vec<String>>,
    properties: Option<HashMap<String, String>>,
    scores: Option<Vec<i32>>,
    active: Option<bool>,
    created_at: Option<i64>,
    avatar: Option<Vec<i8>>,
}

fn create_sample_user_info() -> UserInfo {
    let mut properties = HashMap::new();
    properties.insert("city".to_string(), "San Francisco".to_string());
    properties.insert("country".to_string(), "USA".to_string());
    properties.insert("role".to_string(), "Developer".to_string());

    let created_at = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .unwrap()
        .as_secs() as i64;

    UserInfo {
        user_id: Some(12345),
        name: Some("John Doe".to_string()),
        age: Some(30),
        emails: Some(vec![
            "john.doe@example.com".to_string(),
            "johndoe@gmail.com".to_string(),
        ]),
        properties: Some(properties),
        scores: Some(vec![85, 90, 78, 92]),
        active: Some(true),
        created_at: Some(created_at),
        avatar: Some(vec![0i8, 1, 2, 3, 4, 5]),
    }
}

fn deserialize_user(fory: &Fory, file_path: &Path) -> Result<UserInfo, Error> {
    let serialized = fs::read(file_path).expect("Failed to read file");
    let result = std::panic::catch_unwind(std::panic::AssertUnwindSafe(|| {
        fory.deserialize::<UserInfo>(&serialized)
    }));
    
    match result {
        Ok(Ok(user_info)) => Ok(user_info),
        Ok(Err(e)) => Err(e),
        Err(_) => Err(Error::InvalidData("Panic during deserialization".into())),
    }
}

fn main() -> Result<(), Error> {
    // Create output directory if it doesn't exist
    let output_dir = Path::new("../workspace");
    fs::create_dir_all(output_dir).expect("Failed to create output directory");

    // Create a sample UserInfo object
    let user_info = create_sample_user_info();
    println!("Created user: {:?}", user_info);

    // Create fory instance configured for cross-language compatibility
    let mut fory = Fory::default()
        .xlang(true)
        .compatible(true);
    fory.register_by_namespace::<UserInfo>("org.feuyeux.fory", "UserInfo")?;

    // Serialize the user to bytes
    let serialized = fory.serialize(&user_info)?;

    // Write the serialized data to a file
    let output_file = output_dir.join("userinfo_rust.fory");
    fs::write(&output_file, &serialized).expect("Failed to write file");
    println!("Serialized to {:?}", output_file);

    // Try to read serialized data from other languages
    let langs = ["java", "go", "js", "python", "rust"];
    for lang in &langs {
        let lang_file = output_dir.join(format!("userinfo_{}.fory", lang));
        if lang_file.exists() {
            match deserialize_user(&fory, &lang_file) {
                Ok(user_info_from_file) => {
                    println!("Deserialized [{}] user info from {:?}:{:?}", lang, lang_file, user_info_from_file);
                },
                Err(e) => println!("Failed to deserialize [{}]: {:?}", lang, e),
            }
        }
    }

    Ok(())
}