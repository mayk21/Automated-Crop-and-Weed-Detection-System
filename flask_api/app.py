from flask import Flask, request, jsonify
from utils import detect_objects
from PIL import Image
import io

app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({"error": "No image uploaded"}), 400

    image_file = request.files['image']
    image = Image.open(image_file.stream).convert("RGB")
    results = detect_objects(image)

    return jsonify({"detections": results})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
