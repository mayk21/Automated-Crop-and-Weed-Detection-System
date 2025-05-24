<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <h1>Weed Detection System</h1>
    <h2>Overview</h2>
    <p>This project is an Android application for weed detection using machine learning models. The app allows users to upload images and receive results on weed presence . The system integrates an ML model deployed locally, interacting via Flask API. Firebase is used for authentication and data storage.</p>
    <h2>Features</h2>
    <ul>
        <li><strong>User Authentication:</strong> Sign up, login, and profile management via Firebase Authentication.</li>
        <li><strong>Weed Detection:</strong> Upload an image and get results on weed presence.</li>
        <li><strong>Firebase Integration:</strong> Uses Firebase for authentication, database, and storage.</li>
        <li><strong>API Interaction:</strong> Communicates with the deployed model via Flask API.</li>
        <li><strong>User Settings:</strong> Edit user information.</li>
    </ul>
    <h2>Tech Stack</h2>
    <ul>
        <li><strong>Frontend:</strong> Kotlin (Android)</li>
        <li><strong>Backend:</strong> Firebase Authentication, Firebase Firestore</li>
        <li><strong>Machine Learning Model:</strong> Deployed on Hugging Face</li>
        <li><strong>API:</strong> Custom API to interact with the ML model</li>
    </ul>
    <h2>Installation</h2>
    <ol>
        <li>Clone the repository:<br><code>git clone https://github.com/yourusername/weed-detection-app.git</code></li>
        <li>Open the project in Android Studio.</li>
        <li>Configure Firebase by adding your <code>google-services.json</code> file.</li>
        <li>Build and run the app on an emulator or physical device.</li>
    </ol>
    <h2>Usage</h2>
    <ol>
        <li><strong>Sign Up / Login:</strong> Create an account or log in with Firebase Authentication.</li>
        <li><strong>Upload Image:</strong> Navigate to the model page and upload an image.</li>
        <li><strong>View Results:</strong> The model processes the image and displays the weed detection/yield estimation results.</li>
        <li><strong>Update Profile:</strong> Modify user details from the settings page.</li>
    </ol>
    <h2>API Integration</h2>
    <p>The app sends images to the model via Flask API. The response contains weed detection results in JSON containing confidence score and bounding boxe coordinates.</p>
    <h2>Contributing</h2>
    <ol>
        <li>Fork the repository.</li>
        <li>Create a new branch: <code>git checkout -b feature-branch</code></li>
        <li>Commit changes: <code>git commit -m 'Add new feature'</code></li>
        <li>Push to branch: <code>git push origin feature-branch</code></li>
        <li>Create a Pull Request.</li>
    </ol>
    <h2>Contact</h2>
    <p>For any queries, reach out via <a href="mailto:asfandyarf21@nutech.edu.pk">asfandyarf21@nutech.edu.pk</a> or open an issue in the repository.</p>
</body>
</html>

