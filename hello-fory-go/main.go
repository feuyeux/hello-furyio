package main

import (
	"fmt"
	"os"
	"path/filepath"

	fory "github.com/apache/fory/go/fory"
)

type UserInfo struct {
	UserId     int64
	Name       string
	Age        int32
	Emails     []string
	Properties map[string]string
	Scores     []int32
	Active     bool
	CreatedAt  int64
	Avatar     []byte
}

func createSampleUserInfo() *UserInfo {
	return &UserInfo{
		UserId:     12345,
		Name:       "John Doe",
		Age:        30,
		Emails:     []string{"john.doe@example.com", "johndoe@gmail.com"},
		Properties: map[string]string{"city": "San Francisco", "country": "USA", "role": "Developer"},
		Scores:     []int32{85, 90, 78, 92},
		Active:     true,
		CreatedAt:  1747130612,
		Avatar:     []byte{0, 1, 2, 3, 4, 5},
	}
}

func main() {
	fmt.Println("Go fory Implementation (v0.10.2)")
	user := createSampleUserInfo()
	fmt.Printf("Created user: %+v\n", user)

	outputDir := "../hello-fory-io"
	os.MkdirAll(outputDir, 0755)
	outputFile := filepath.Join(outputDir, "userinfo_go.fory")

	// Create fory instance
	f := fory.Newfory(true)
	f.RegisterTagType("org.feuyeux.fory.UserInfo", UserInfo{})

	// Serialize
	data, err := f.Marshal(user)
	if err != nil {
		fmt.Printf("Failed to serialize: %v\n", err)
		os.Exit(1)
	}
	os.WriteFile(outputFile, data, 0644)
	fmt.Printf("User information serialized to %s\n", outputFile)

	// Deserialize
	var user2 UserInfo
	if err := f.Unmarshal(data, &user2); err != nil {
		fmt.Printf("Failed to deserialize: %v\n", err)
		os.Exit(1)
	}
	fmt.Printf("Deserialized user: %+v\n", user2)
}
