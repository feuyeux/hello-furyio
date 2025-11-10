from dataclasses import dataclass, field
import pyfory
import time
from pathlib import Path
from typing import Dict, List, Optional


@dataclass
class UserInfo:
    user_id: Optional[int] = None
    name: Optional[str] = None
    age: Optional[int] = None
    emails: Optional[List[str]] = None
    properties: Optional[Dict[str, str]] = None
    scores: Optional[List[int]] = None
    active: Optional[bool] = None
    created_at: Optional[int] = None
    avatar: Optional[bytes] = None


def create_sample_user_info() -> UserInfo:
    """Create a sample UserInfo object matching Java implementation"""
    return UserInfo(
        user_id=12345,
        name="John Doe",
        age=30,
        emails=["john.doe@example.com", "johndoe@gmail.com"],
        properties={
            "city": "San Francisco",
            "country": "USA",
            "role": "Developer"
        },
        scores=[85, 90, 78, 92],
        active=True,
        created_at=int(time.time()),
        avatar=bytes([0, 1, 2, 3, 4, 5])
    )


def deserialize_user(fory: pyfory.Fory, file_path: Path) -> UserInfo:
    """Deserialize user from file"""
    serialized = file_path.read_bytes()
    user_info = fory.deserialize(serialized)
    return user_info


def main():
    # Create output directory if it doesn't exist
    output_dir = Path("../workspace")
    output_dir.mkdir(parents=True, exist_ok=True)

    # Create a sample UserInfo object
    user_info = create_sample_user_info()
    print(f"Created user: {user_info}")

    # Create fory instance configured for cross-language compatibility
    fory = pyfory.Fory(xlang=True, ref=True)
    fory.register(UserInfo, namespace="org.feuyeux.fory", typename="UserInfo")

    # Serialize the user to bytes
    serialized = fory.serialize(user_info)

    # Write the serialized data to a file
    output_file = output_dir / "userinfo_python.fory"
    output_file.write_bytes(serialized)
    print(f"Serialized to {output_file}")

    # Try to read serialized data from other languages
    langs = ["java", "go", "js", "python", "rust"]
    for lang in langs:
        lang_file = output_dir / f"userinfo_{lang}.fory"
        if lang_file.exists():
            try:
                user_info_from_file = deserialize_user(fory, lang_file)
                print(f"Deserialized [{lang}] user info from {lang_file}:{user_info_from_file}")
            except Exception as e:
                print(f"Failed to deserialize [{lang}]: {e}")


if __name__ == "__main__":
    main()