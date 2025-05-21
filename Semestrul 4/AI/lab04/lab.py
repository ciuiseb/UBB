import os
from itertools import cycle

import matplotlib.pyplot as plt
from PIL import Image
from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import VisualFeatureTypes
from msrest.authentication import CognitiveServicesCredentials


def get_image_paths(folder_path):
    return [os.path.join(folder_path, filename)
            for filename in os.listdir(folder_path)
            if os.path.isfile(os.path.join(folder_path, filename))]


def get_images(folder_path):
    return [open(path, "rb") for path in get_image_paths(folder_path)]


def clasificare_binara(computervision_client, images):
    real_outputs = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                   0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    computed_outputs = []
    for img in images:
        result = computervision_client.analyze_image_in_stream(img, visual_features=[VisualFeatureTypes.tags,
                                                                                     VisualFeatureTypes.objects])
        tag_names = [tag.name for tag in result.tags]
        if "bike" in tag_names or "bicycle" in tag_names:
            computed_outputs.append(1)
        else:
            computed_outputs.append(0)
    acc = sum([1 if real_outputs[i] == computed_outputs[i] else 0 for i in range(0, len(real_outputs))]) / len(real_outputs)
    print(acc)


def draw_rectangles_on_image(computervision_client, img):
    color_cycle = cycle(['red', 'blue', 'green', 'orange', 'purple', 'cyan', 'magenta'])

    result = computervision_client.analyze_image_in_stream(img, visual_features=[VisualFeatureTypes.objects])
    prediction = None
    img_copy = Image.open(img)
    fig = plt.imshow(img_copy)

    for obj in result.objects:
        if obj.object_property in ["bike", "bicycle"]:
            prediction = [obj.rectangle.x, obj.rectangle.y, obj.rectangle.x + obj.rectangle.w,
                          obj.rectangle.y + obj.rectangle.h]
            fig.axes.add_patch(
                plt.Rectangle(xy=(prediction[0], prediction[1]), width=prediction[2] - prediction[0],
                              height=prediction[3] - prediction[1],
                              fill=False,
                              color=next(color_cycle),
                              linewidth=2)
            )
    if prediction is None:
        print("No bike in given photo")
    plt.show()


def main():
    subscription_key = os.environ["VISION_KEY"]
    endpoint = os.environ["VISION_ENDPOINT"]
    computervision_client = ComputerVisionClient(endpoint, CognitiveServicesCredentials(subscription_key))
    images = get_images("bikes")  # [:10]

    # clasificare_binara(computervision_client, images)
    draw_rectangles_on_image(computervision_client, images[2])

    # a durat putin mai mult manual



if __name__ == '__main__':
    main()
