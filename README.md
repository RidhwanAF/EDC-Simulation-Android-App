## Installation Guide

### Prerequisites
*   Android Studio
*   Java Development Kit (JDK) 11
*   Android SDK with a minimum version of 26

### Installation Steps
1.  Clone the repository:
    ```bash
    git clone <https://github.com/RidhwanAF/EDC-Simulation-Android-App.git>
    ```
2.  Open the project in Android Studio.
3.  Let Android Studio download the required Gradle dependencies.

### Configuration
Before running the application, you need to configure the following environment variables.

**IMPORTANT: You must add `BASE_URL` and `SECRET_KEY_STRING` to your `local.properties` file.**

1.  Create a `local.properties` file in the root of the project if it doesn't exist.
2.  Add the following lines to your `local.properties` file:
    ```properties
    BASE_URL="your_base_url_here"
    SECRET_KEY_STRING="your_secret_key_here"
    ```
3.  Replace `"your_base_url_here"` and `"your_secret_key_here"` with your actual base URL and secret key.

### Demo


https://github.com/user-attachments/assets/f5873c2f-29f1-4b72-8d55-f74b8544ff8b

