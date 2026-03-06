# File Fingerprinting POC

This is a simple proof-of-concept (POC) for file fingerprinting in Java.

## Description

The `FileFingerprinter` class calculates the SHA-256 hash of a given file. This hash, also known as a fingerprint, can be used to uniquely identify the file's content. If the file's content changes, the hash will also change.

This is useful for:

*   Verifying file integrity.
*   Detecting changes in files.
*   Comparing files without comparing their content directly.

## How to Run

1.  **Compile the code:**

    You can use Maven to compile the project.

    ```bash
    mvn clean install -f file-fingerprinting-poc/pom.xml
    ```

2.  **Run the application:**

    After compiling, you can run the application from the command line, passing the path to the file you want to fingerprint as an argument.

    ```bash
    java -cp file-fingerprinting-poc/target/classes com.example.fingerprint.FileFingerprinter file-fingerprinting-poc/src/main/resources/sample.txt
    ```

    You can also try it with other files:

    ```bash
    java -cp file-fingerprinting-poc/target/classes com.example.fingerprint.FileFingerprinter file-fingerprinting-poc/pom.xml
    ```
