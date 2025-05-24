from PIL import ImageDraw, ImageFont
from ultralytics import YOLO

model = YOLO("best.pt")

class_names = {0: "weed", 1: "wheat"}

def detect_objects(image):
    results = model.predict(image, imgsz=640)[0]
    preds = results.boxes.data

    try:
        font = ImageFont.truetype("arial.ttf", 16)
    except:
        font = ImageFont.load_default()

    output = []
    for box in preds.cpu():
        x1, y1, x2, y2, conf, cls = box.tolist()
        if conf >= 0.5:
            class_id = int(cls)
            class_name = class_names.get(class_id, "unknown")
            output.append({
                "x1": int(x1), "y1": int(y1),
                "x2": int(x2), "y2": int(y2),
                "confidence": round(conf, 2),
                "class": class_name
            })

    return output
